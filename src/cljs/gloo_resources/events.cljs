(ns gloo-resources.events
  (:require-macros [adzerk.env :as env])
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax :refer [GET POST]]
            [day8.re-frame.http-fx]
            [gloo-resources.db :as db]))

;; DEV KEY

(env/def GLOO_GRAPHQL_API_KEY (System/getenv "GLOO_GRAPHQL_API_KEY"))

(def graphql-endpoint "https://api.graph.cool/simple/v1/cj901vh3j0buy0122sxzyrepn")

(def jenkins-info-endpoint "https://7q3hx47nji.execute-api.us-east-1.amazonaws.com/dev")

(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(rf/reg-event-db
  :set-active-panel
  (fn [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

(rf/reg-event-fx
  :fetch-graph
  (fn [{db :db} [_ db-node query & [token]]]
    {:db         db
     :http-xhrio {:method          :post
                  :headers         {:Authorization (str "Bearer " (or token GLOO_GRAPHQL_API_KEY))}
                  :format          (ajax/json-request-format)
                  :params          {:query query}
                  :uri             graphql-endpoint
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:fetch-graph-success db-node]}}))

(rf/reg-event-db
  :fetch-graph-success
  (fn [db [_ db-node & [{data :data}]]]
    (assoc db db-node (db-node data))))

(rf/reg-event-fx
  :fetch-jenkins-info
  (fn [{db :db} _]
    {:db         db
     :http-xhrio {:method          :get
                  :uri             jenkins-info-endpoint
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:fetch-jenkins-info-success]}}))

(rf/reg-event-db
  :fetch-jenkins-info-success
  (fn [db [_ res]]
    (assoc db :allJenkinsResources (vals res))))

(rf/reg-event-db
  :update-row-state
  (fn [db [_ ref values]]
    (assoc db ref values)))