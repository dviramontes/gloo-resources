(ns gloo-resources.table
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]))

(def query "
  {
    allResources {
      url
      name
      branch
      endDate
      engineer
      startDate
    }
  }")

(def db-key :allResources)

(def cols ["name"
           "endDate"
           "url"
           "branch"
           "engineer"
           "startDate"
           "re-assign"])

(defn table []
  (rf/dispatch [:fetch-graph db-key query])
  (let [resources-subs (rf/subscribe [db-key])]
    (fn []
      [:div#table "test"])))


