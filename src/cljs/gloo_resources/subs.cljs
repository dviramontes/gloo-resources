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
  :all-jenkins-resources
  (fn [db _]
    (:all-jenkins-resources db)))

(rf/reg-sub
  :on-app-failure
  (fn [db _]
    (:on-app-failure db)))

(rf/reg-sub
  :sort-by-type
  (fn [db _]
    (:sort-by-type db)))