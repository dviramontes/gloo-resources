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
  (let [{:keys [name engineer branch startDate endDate url]} contents]
    [:tr {:class "striped--light-gray"}
     [:td {:class "pv2 ph3"} name]
     [:td {:class "pv2 ph3"} [:b engineer]]
     [:td {:class "pv2 ph3"} branch]
     [:td {:class "pv2 ph3"} startDate]
     [:td {:class "pv2 ph3"} endDate]
     [:td {:class "pv2 ph3"} [:a {:href url :target "_blank"} url]]]))

(defn table []
  (rf/dispatch [:fetch-graph db-key query])
  (let [resources-subs (rf/subscribe [db-key])]
    (fn []
      (prn @resources-subs)
      [:div#table-container
       [:table {:class "collapse ba br2 b--black-10 pv2 ph3"}
        [:tbody
         [:tr {:class "striped--light-gray ba bw2"}
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba"} "NAME"]
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba"} "ENGINEER"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "BRANCH"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "START_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "END_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "URL"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "STATUS"]]
         (for [r @resources-subs]
           ^{:key (gensym "row-")}
           [row r])]]])))





