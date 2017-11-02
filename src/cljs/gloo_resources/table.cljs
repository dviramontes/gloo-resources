(ns gloo-resources.table
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [gloo-resources.date-picker :refer [date-picker]]))

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

(defn claim-resource-btn []
  [:a {:class "f6 link dim ba ph3 pv2 mb2 dib hot-pink"}
   [:span "claim"]])

(defn lock-resource-btn []
  [:a {:class "f6 link dim ba ph3 pv2 mb2 dib dark-pink"}
   [:span "lock"]])

(defn edit-resource-btn []
  [:a {:class "f6 link dim ba ph3 pv2 mb2 dib dark-pink"}
   [:i.fa.fa-pencil]])

(defn row [contents]
  (let [{:keys [name engineer branch startDate endDate url]} contents]
    (fn []
      [:tr {:class "resource-row striped--light-gray"}
       [:td {:class "pv2 ph3"} [edit-resource-btn]]
       [:td {:class "pv2 ph3 light-purple"} name]
       [:td {:class "pv2 ph3 purple"} [:b engineer]]
       [:td {:class "pv2 ph3 light-green b-navy"} branch]
       [:td {:class "pv2 ph3"} [date-picker]]
       [:td {:class "pv2 ph3"} [date-picker]]
       [:td {:class "pv2 ph3"} [:a {:href url :target "_blank"} url]]
       [:td {:class "pv2 ph3"} "..."]
       [:td {:class "pv2 ph3 actions-cell"} [claim-resource-btn] [lock-resource-btn]]])))

(defn table []
  (rf/dispatch [:fetch-graph db-key query])
  (let [resources-subs (rf/subscribe [db-key])]
    (fn []
      (prn @resources-subs)
      [:div#table-container
       [:table {:class "collapse ba br2 b--black-10 pv2 ph3"}
        [:tbody
         [:tr {:class "striped--light-gray ba bw2"}
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba "} "_"]
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba light-purple"} "NAME"]
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba purple"} "ENGINEER"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba light-green b-navy"} "BRANCH"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "START_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "END_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "URL"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "STATUS"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "ACTIONS"]]
         (for [r @resources-subs]
           ^{:key (gensym "row-")}
           [row r])]]])))





