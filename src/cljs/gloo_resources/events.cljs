(ns gloo-resources.events
  (:require-macros [purnam.core :refer [!>]])
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax :refer [GET POST]]
            [day8.re-frame.http-fx]
            [gloo-resources.firebase :as fb]
            [gloo-resources.db :as db]))

(def jenkins-info-endpoint
  "https://dsmfauznd3.execute-api.us-east-1.amazonaws.com/prod/jenkins-dev-jenkinsInfo")

(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(rf/reg-event-db
  :autheticated!
  (fn [db [_ auth?]]
    (assoc db :auth0-authenticated? auth?)))

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
    (let [coll
          (map #(let [{:keys [type ordinal]} %
                      resource-name (str type ordinal)
                      resource-name-key (keyword resource-name)
                      read-ref (fb/path-str->db-ref (str "jenkins-info/" resource-name))]
                  ;; register all resource subscriptions
                  (rf/reg-sub
                    resource-name-key
                    (fn [{jenkins-resources :jenkins-resources}]
                      (resource-name-key jenkins-resources)))
                  ;; setup firebase on value change callbacks
                  (!> read-ref.on "value"
                      (fn [v]
                        (let [snapshot->clj (-> v .val (js->clj :keywordize-keys true))]
                          (rf/dispatch [:update-row-state resource-name-key snapshot->clj]))))
                  (hash-map resource-name-key %))
               (vals res))
          final-coll (reduce conj {} coll)]
      (assoc db :all-jenkins-resources final-coll
                :jenkins-resources final-coll))))


(rf/reg-event-db
  :fetch-jenkins-info-failure
  (fn [db _]
    (assoc db :on-app-failure {:show? true
                               :msg   "fetching jenkins-info failed"})))

(rf/reg-event-db
  :update-row-state
  (fn [db [_ ref new-values]]
    (update-in db [:jenkins-resources ref] conj new-values)))

(rf/reg-event-db
  :sort-by-type
  (fn [db [_ type]]
    (assoc db :sort-by-type type)))