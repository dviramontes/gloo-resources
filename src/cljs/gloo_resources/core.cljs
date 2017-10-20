(ns gloo-resources.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [gloo-resources.events]
            [gloo-resources.subs]
            [gloo-resources.routes :as routes]
            [gloo-resources.views :as views]
            [gloo-resources.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
