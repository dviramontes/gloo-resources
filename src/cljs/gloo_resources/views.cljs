(ns gloo-resources.views
  (:require [re-frame.core :as re-frame]
            [cljsjs.auth0-lock]
            [re-com.core :as re-com]))


(def lock
  (new js/Auth0Lock
       "UF8w9E28EPVedKxIzLmW1XiSnd9Dupxq"
       "pl-switchboard.auth0.com"
       (clj->js {:auth {:redirect false}})))

(def gloo-dev-resources-alloc-token "gloo-dev-resources-alloc-token")

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label "Development Resource Allocations"
       :level :level1])))

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

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [login-btn]]])


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
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [[panels @active-panel]]])))
