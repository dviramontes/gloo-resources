(ns gloo-resources.table
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [gloo-resources.date-picker :refer [date-picker]]
            [gloo-resources.branch-input :refer [branch-input]]
            [gloo-resources.engineer-input :refer [engineer-input]]
            [gloo-resources.firebase :as fb]))

(def db-key :allJenkinsResources)

(defn launch-btn []
  [:a {:class "f6 link dim ba ph3 pv2 mb2 dib hot-pink"}
   [:span "launch"]])

(defn lock-btn []
  [:a {:class "f6 link dim ba ph3 pv2 mb2 dib dark-pink"}
   [:span "lock"]])

(defn edit-btn []
  [:a {:class "f6 link dim ba ph3 pv2 mb2 dib dark-pink b-animate"}
   [:i.fa.fa-pencil]])

(defn row [contents]
  (let [{:keys [name engineer startDate endDate color type ordinal]} contents
        url (str "http://" name) ;; urls coming from jenkins point to jenkins pages not the resource's URL
        resource-name (str type ordinal)
        resource-name-key (keyword resource-name)
        read-ref (fb/path-str->db-ref (str "jenkins-info/" resource-name))]
    (fn []
      (rf/reg-sub resource-name-key #(resource-name-key %))
      (-> read-ref
          (.on "value" #(let [snapshot->clj (-> % .val (js->clj :keywordize-keys true))]
                          (rf/dispatch [:update-row-state resource-name-key snapshot->clj]))))
      [:tr {:class "resource-row striped--light-gray"}
       [:td {:class "pv2 ph3"} [edit-btn]]
       [:td {:class "pv2 ph3 light-purple animate"} [:a {:href url :target "_blank"} url]]
       [:td {:class "pv2 ph3 purple b-red"} [engineer-input resource-name]]
       [:td {:class "pv2 ph3"} [branch-input resource-name]]
       [:td {:class "pv2 ph3"} [date-picker resource-name :start-time]]
       [:td {:class "pv2 ph3"} [date-picker resource-name :end-time]]
       [:td {:class "pv2 ph3"} [:i {:class (str "fa fa-circle status-dot " color)}]]
       [:td {:class "pv2 ph3 actions-cell"} [launch-btn]]])))

(defn table []
  (rf/dispatch [:fetch-jenkins-info])
  (let [resources-subs (rf/subscribe [db-key])]
    (fn []
      [:div#table-container
       [:table#table {:class "collapse ba br2 b--black-10 pv2 ph3"}
        [:tbody
         [:tr {:class "striped--light-gray ba bw2"}
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba"} "_"]
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba light-purple"} "NAME"]
          [:th {:class "pv2 ph3 tl f6 fw6 ttu ba purple b-yellow"} "ENGINEER"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba light-green b-navy"} "BRANCH"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "START_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "END_DATE"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "STATUS"]
          [:th {:class "tr f6 ttu fw6 pv2 ph3 ba"} "ACTIONS"]]
         (for [r @resources-subs]
           ^{:key (gensym "row-")} [row r])]]])))