(ns gloo-resources.views
  (:require cljsjs.handsontable
            [re-frame.core :as rf]
            [cljsjs.auth0-lock]
            [reagent.core :as reagent]
            [re-com.core :as re-com]))

(def lock
  (new js/Auth0Lock
       "UF8w9E28EPVedKxIzLmW1XiSnd9Dupxq"
       "pl-switchboard.auth0.com"
       (clj->js {:auth {:redirect false}})))

(def gloo-dev-resources-alloc-token "gloo-dev-resources-alloc-token")

(defn home-title []
  [re-com/title
   :label "Gloo Development Resource Allocations"
   :level :level1])

(defn login-btn []
  (let [_ (.on lock
               "authenticated"
               (fn [profile]
                 (let [token (aget profile "accessToken")]
                   (js/window.localStorage.setItem
                     gloo-dev-resources-alloc-token
                     token))))]
    [:button.btn.btn-login.btn-sm
     {:type     "button"
      :on-click #(.show lock)}
     "Log in"]))

(defn table []
  (let []
    (reagent/create-class
      {:component-did-mount
       (fn []
         (let [data [["2017", 10, 11, 12, 13 12]
                     ["2018", 20, 11, 14, 13 12]
                     ["2019", 30, 15, 12, 13 12]]
               dom-node (js/document.querySelector "#table")]
           (js/Handsontable.
             dom-node
             #js {:data       (clj->js data)
                  :rowHeaders false
                  :colHeaders #js ["Site-Name"
                                   "Engineer"
                                   "Start"
                                   "Finish"
                                   "Branch"
                                   "Last-DB-load"]})))

       :display-name
       "resources-table-component"                             ;; for more helpful warnings & errors

       :reagent-render
       (fn []
         [:div#table])})))



(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[re-com/box
               :class "fl w-100 pa2"
               :child [home-title]]
              [re-com/box
               :class "fl w-100 pa2"
               :child [login-btn]]
              [re-com/box
               :class "fl w-100 pa2"
               :child [table]]]])



;; about

(defn about-title []
  [re-com/title
   :label "This is the About Page."
   :level :level1])

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "go to Home Page"
   :href "#/"])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title] [link-to-home-page]]])


;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (rf/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [[panels @active-panel]]])))
