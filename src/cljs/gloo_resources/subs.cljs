(ns gloo-resources.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :name
 (fn [db]
   (:name db)))

(rf/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

(rf/reg-sub
  :autheticated?
  (fn [db _]
    (:auth0-authenticated? db)))

(rf/reg-sub
  :allResources
  (fn [db _]
    (:allResources db)))

(rf/reg-sub
  :allJenkinsResources
  (fn [db _]
    (:allJenkinsResources db)))

(rf/reg-sub
  :on-app-failure
  (fn [db _]
    (:on-app-failure db)))