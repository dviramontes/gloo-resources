(ns gloo-resources.text-input
  (:require [re-frame.core :as rf]
            [gloo-resources.firebase :as fb]
            [reagent.core :as reagent]))

(defn- text-input-wrapper [value callback]
  (let [base-classes "input-reset ba b--black-20 pa2 mb2 db w-100"]
    (fn [value callback]
      [:input {:type        "text"
               :placeholder "name"
               :value       value
               :on-change   #(callback (-> % .-target .-value))}])))

(defn text-input [type-key resource]
  (let [type-name (name type-key)
        resource-sub (rf/subscribe [(keyword resource)])
        fb-write-path (fb/path-str->db-ref (str "jenkins-info/" resource "/" type-name))
        state (reagent/atom nil)]
    (fn []
      (let [type-value (get @resource-sub type-key)
            callback (fn [val]
                       (-> fb-write-path (.set val))
                       (reset! state val))]
        [text-input-wrapper (or @state type-value) callback]))))