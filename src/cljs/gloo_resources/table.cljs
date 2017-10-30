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

(defn row [contents]
  [:tr {:class "striped--light-gray"}
   [:td {:class "pv2 ph3"} contents]
   [:td {:class "pv2 ph3"} contents]
   [:td {:class "pv2 ph3"} contents]])

(defn table []
  (rf/dispatch [:fetch-graph db-key query])
  (let [resources-subs (rf/subscribe [db-key])]
    (fn []
      (prn @resources-subs)
      [:div#table-container
       [:table {:class "collapse ba br2 b--black-10 pv2 ph3"}
        [:tbody
         [:tr {:class "striped--light-gray"}
          [:th {:class "pv2 ph3 tl f6 fw6 ttu"} "NAME"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3"} "END_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3"} "URL"]]
         [row "test"]]]])))





