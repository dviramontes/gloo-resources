(ns gloo-resources.views
  (:require cljsjs.handsontable
            [re-frame.core :as rf]
            [gloo-resources.queries :as queries]
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

(defn table [resources]
  (reagent/create-class
    {:component-did-mount
     (fn []
       (let [re-assign-symbol "[x]"
             cols #js ["name"
                       "endDate"
                       "url"
                       "branch"
                       "engineer"
                       "startDate"
                       "re-assign"]
             dom-node (js/document.querySelector "#table")
             resources (map #(assoc % :re-assign re-assign-symbol) resources)
             columns (map #(hash-map :data % :readOnly true :renderer "html") cols)
             htable (js/Handsontable.
                      dom-node
                      #js {:data       (clj->js resources)
                           :rowHeaders false
                           :colHeaders cols
                           :columns    (clj->js columns)
                           :afterSelection (fn [r c]
                                             (this-as this-js
                                               (let [col-info (js->clj (.getDataAtCol this-js c))
                                                     row-info (js->clj (.getDataAtRow this-js r))]
                                                 (when (some #(= re-assign-symbol %) col-info)
                                                   (prn row-info)))))})]))


     :display-name
     "resources-table-component"                              ;; for more helpful warnings & errors

     :reagent-render
     (fn []
       [:div#table])}))




(defn home-panel [resources]
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
               :child [table resources]]]])



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

(defn- panels [panel-name resources]
  (case panel-name
    :home-panel [home-panel resources]
    :about-panel [about-panel]
    [:div]))

(defn main-panel []
  (rf/dispatch [:fetch-graph :allResources queries/resources])
  (let [active-panel (rf/subscribe [:active-panel])
        allResources (rf/subscribe [:allResources])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [[panels @active-panel @allResources]]])))
