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

(defn claim-resource-btn []
  [:a {:class "f6 link dim br1 ph3 pv2 mb2 dib white bg-purple"}
   [:span.fa-stack.fa-lg
    [:i.fa.fa-flag.fa-stack-1x.fa-inverse]]])

(defn row [contents]
  (let [{:keys [name engineer branch startDate endDate url]} contents]
    [:tr {:on-mouse-enter #(prn "kdsa")
          :class "resource-row striped--light-gray"}
     [:td {:class "pv2 ph3 light-purple"} name]
     [:td {:class "pv2 ph3 purple"} [:b engineer]]
     [:td {:class "pv2 ph3 light-green b-navy"} branch]
     [:td {:class "pv2 ph3"} startDate]
     [:td {:class "pv2 ph3"} endDate]
     [:td {:class "pv2 ph3"} [:a {:href url :target "_blank"} url]]
     [:td {:class "pv2 ph3"} "..."]
     [:td {:class "pv2 ph3"} [claim-resource-btn]]]))

(defn table []
  (rf/dispatch [:fetch-graph db-key query])
  (let [resources-subs (rf/subscribe [db-key])]
    (fn []
      (prn @resources-subs)
      [:div#table-container
       [:table {:class "collapse ba br2 b--black-10 pv2 ph3"}
        [:tbody
         [:tr {:class "striped--light-gray ba bw2"}
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba light-purple"} "NAME"]
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba purple"} "ENGINEER"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba light-green b-navy"} "BRANCH"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "START_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "END_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "URL"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "STATUS"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "CLAIM"]]
         (for [r @resources-subs]
           ^{:key (gensym "row-")}
           [row r])]]])))





