(ns gloo-resources.views
  (:require [re-frame.core :as rf]
            [cljsjs.auth0-lock]
            [gloo-resources.table :refer [table]]
            [re-com.core :as re-com]))

(def lock
  (new js/Auth0Lock
       "UF8w9E28EPVedKxIzLmW1XiSnd9Dupxq"
       "pl-switchboard.auth0.com"
       (clj->js {:auth {:redirect false}
                 :scope "openid profile"})))

(def gloo-dev-resources-alloc-token "gloo-dev-resources-alloc-token")

(let []
  (.on lock
       "authenticated"
       (fn [profile]
         (let [token (aget profile "accessToken")]
           (prn profile)
           (js/window.localStorage.setItem
             gloo-dev-resources-alloc-token
             token)))))

(defn home-title []
  [re-com/title
   :label "Gloo Development Resource Allocations"
   :level :level1])

(defn login-btn []
  [:button.btn.btn-login.btn-sm
   {:type     "button"
    :on-click #(.show lock)}
   "Log in"])

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

(def allUsersQuery "{ allUsers { id } }")

(defn main-panel []
  (let [active-panel (rf/subscribe [:active-panel])]
    (when-let [token (js/window.localStorage.getItem "gloo-dev-resources-alloc-token")]
      (rf/dispatch [:fetch-graph :allUsers allUsersQuery token]))
    (fn []
      [re-com/v-box
       :height "100%"
       :children [[panels @active-panel]]])))