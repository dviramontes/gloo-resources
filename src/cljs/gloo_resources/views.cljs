(ns gloo-resources.views
  (:require [re-frame.core :as rf]
            [cljsjs.auth0-lock]
            [gloo-resources.components.table :refer [table]]
            [gloo-resources.firebase :as fb]
            [re-com.core :as re-com]))

(def lock
  (new js/Auth0Lock
       "UF8w9E28EPVedKxIzLmW1XiSnd9Dupxq"
       "pl-switchboard.auth0.com"
       (clj->js {:auth  {:redirect false}
                 :scope "openid profile"})))

(defonce gloo-dev-resources-alloc-token "gloo-dev-resources-alloc-token")

(let []
  (.on lock
       "authenticated"
       (fn [res]
         (let [token (aget res "accessToken")]
           (rf/dispatch [:autheticated! true])
           (js/window.localStorage.setItem gloo-dev-resources-alloc-token token)))))


(defn title []
  [re-com/title
   :label "Gloo Development Resource Allocations"
   :level :level1])

(defn login-btn []
  (fn []
    (if @(rf/subscribe [:autheticated?])
      [:a {:class    "f6 link dim br1 ba bw2 ph3 pv2 mb2 dib light-red bg-black-90"
           :on-click (fn []
                       (js/window.localStorage.removeItem gloo-dev-resources-alloc-token)
                       (rf/dispatch [:autheticated! false]))} "LOG-OUT"]
      [:a {:class    "f6 link dim br1 ba bw2 ph3 pv2 mb2 dib light-purple bg-black-90"
           :on-click #(.show lock)} "LOG-IN"])))

(defn sort-by-type-btn [type]
  [:a {:class    "f6 link dim br1 ba bw2 ph3 pv2 mb2 dib light-green bg-black-90"
       :on-click #(rf/dispatch [:sort-by-type type])}
   (-> type name .toUpperCase)])

(defn button-row [& btns]
  (fn []
    [re-com/h-box
     :gap "1em"
     :children (apply vector btns)]))

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[re-com/box
               :class "fl center"
               :child [title]]
              [re-com/box
               :class "fl center"
               :child [button-row [login-btn]]]
              [re-com/box
               :class "fl center"
               :child [button-row [sort-by-type-btn :all]
                                  [sort-by-type-btn :qa]
                                  [sort-by-type-btn :ui-test]
                                  [sort-by-type-btn :restricted]]]
              [re-com/box
               :class "fl center"
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

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn main-panel []
  (let [active-panel (rf/subscribe [:active-panel])]
    (fn []
      (when-let [token (js/window.localStorage.getItem gloo-dev-resources-alloc-token)]
        (.getUserInfo lock token (fn [error profile]
                                   (when profile (rf/dispatch [:autheticated! true])))))
      [re-com/v-box
       :height "100%"
       :children [[panels @active-panel]]])))