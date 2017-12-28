(ns gloo-resources.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax :refer [GET POST]]
            [day8.re-frame.http-fx]
            [gloo-resources.db :as db]))

(def jenkins-info-endpoint
  "https://dsmfauznd3.execute-api.us-east-1.amazonaws.com/prod/jenkins-dev-jenkinsInfo")

(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(rf/reg-event-db
  :set-active-panel
  (fn [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

(rf/reg-event-fx
  :fetch-jenkins-info
  (fn [{db :db} _]
    {:db         db
     :http-xhrio {:method          :get
                  :uri             jenkins-info-endpoint
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:fetch-jenkins-info-success]
                  :on-failure      [:fetch-jenkins-info-failure]}}))

(rf/reg-event-db
  :fetch-jenkins-info-success
  (fn [db [_ res]]
    (assoc db :allJenkinsResources (vals res))))

(rf/reg-event-db
  :fetch-jenkins-info-failure
  (fn [db _]
    (assoc db :on-app-failure {:show? true
                               :msg "fetching jenkins-info failed"})))

(rf/reg-event-db
  :update-row-state
  (fn [db [_ ref values]]
    (assoc db ref values)))