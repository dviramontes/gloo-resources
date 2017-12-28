(ns gloo-resources.table
  (:require [re-frame.core :as rf]
            [gloo-resources.components.date-picker :refer [date-picker]]
            [gloo-resources.components.text-input :refer [text-input]]
            [gloo-resources.components.error :refer [error]]
            [gloo-resources.firebase :as fb]))

(def db-key :allJenkinsResources)

(defn launch-btn []
  [:a.f6link.dim.ba.ph3.pv2.mb2.dib.hot-pink
   [:span "launch"]])

(defn edit-btn []
  [:a.f6.link.dim.ba.ph3.pv2.mb2.dib.dark-pink.b-animate
   [:i.fa.fa-pencil]])

(defn row [contents]
  (let [{:keys [name engineer startDate endDate color type ordinal]} contents
        url (str "http://" name)                                ;; urls coming from jenkins point to jenkins pages not the resource's URL
        resource-name (str type ordinal)
        resource-name-key (keyword resource-name)
        read-ref (fb/path-str->db-ref (str "jenkins-info/" resource-name))]
    (fn []
      (rf/reg-sub resource-name-key #(resource-name-key %))
      (-> read-ref
          (.on "value" #(let [snapshot->clj (-> % .val (js->clj :keywordize-keys true))]
                          (rf/dispatch [:update-row-state resource-name-key snapshot->clj]))))
      [:tr.resource-row.striped--light-gray
       [:td.pv2.ph3
        [edit-btn]]
       [:td.pv2.ph3.light-purple
        [:a {:href url :target "_blank"} url]]
       [:td.pv2.ph3.purple.b-red
        [text-input :engineer resource-name]]
       [:td.pv2.ph3
        [text-input :branch resource-name]]
       [:td.pv2.ph3
        [date-picker resource-name :start-time]]
       [:td.pv2.ph3
        [date-picker resource-name :end-time]]
       [:td.pv2.ph3
        [:i.fa.fa-circle.status-dot {:class color}]]
       [:td.pv2.ph3.actions-cell [launch-btn]]])))

(defn table []
  (rf/dispatch [:fetch-jenkins-info])
  (let [resources-subs (rf/subscribe [db-key])]
    (fn []
      [:div#table-container
       [error]
       [:table#table.collapse.ba.br2.b--black-10.pv2.ph3
        [:tbody
         [:tr.striped--light-gray.ba.bw2
          [:th.pv2.ph3.tl.f6.fw6.ttu.ba "_"]
          [:th.pv2.ph3.tl.f6.fw6.ttu.ba.light-purple "NAME"]
          [:th.pv2.ph3.tl.f6.fw6.ttu.ba.purple.b-yellow "ENGINEER"]
          [:th.tr.f6.ttu.fw6.pv2.ph3.ba.light-green.b-navy "BRANCH"]
          [:th.tr.f6.ttu.fw6.pv2.ph3.ba "START_DATE"]
          [:th.tr.f6.ttu.fw6.pv2.ph3.ba "END_DATE"]
          [:th.tr.f6.ttu.fw6.pv2.ph3.ba "STATUS"]
          [:th.tr.f6.ttu.fw6.pv2.ph3.ba "ACTIONS"]]
         (for [r @resources-subs]
           ^{:key (gensym "row-")} [row r])]]])))