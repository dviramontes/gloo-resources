(ns gloo-resources.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax :refer [POST]]
            [day8.re-frame.http-fx]
            [gloo-resources.db :as db]))

(def GRAPHQL_API_KEY "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MDg1MTQ1MDksImNsaWVudElkIjoiY2o1aXozaHRsNzRtdDAxMjJjN3Z0d3BlOSIsInByb2plY3RJZCI6ImNqOTAxdmgzajBidXkwMTIyc3h6eXJlcG4iLCJwZXJtYW5lbnRBdXRoVG9rZW5JZCI6ImNqOTAybDljcjBkNTAwMTIyNTQwNjVscGIifQ.SS5eZxBLkXqK3my9mdGDzWn-Ch-X6Gn1rru8XvVrQO4")

(def graphql-endpoint "https://api.graph.cool/simple/v1/cj901vh3j0buy0122sxzyrepn")

(rf/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(rf/reg-event-fx
  :get-profile-data
  (fn [{db :db} [_ query]]
    ;; TODO: add loading state...
    {:db         db
     :http-xhrio {:method          :post
                  :headers         {:Authorization (str "Bearer " GRAPHQL_API_KEY)}
                  :format          (ajax/json-request-format)
                  :params          {:query query}
                  :uri             graphql-endpoint
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:get-success]}}))


(rf/reg-event-db
  :get-success
  (fn [db [_ & [{data :data}]]]
    (prn db)
    db))
