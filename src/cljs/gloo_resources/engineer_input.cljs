(ns gloo-resources.engineer-input
  (:require [re-frame.core :as rf]
            [gloo-resources.firebase :as fb]
            [reagent.core :as reagent]))

(defn- input-comp [value resource callback]
  (let [write-ref-path (fb/path-str->db-ref (str "jenkins-info/" resource "/engineer"))]
    [:input {:type        "text"
             :class       "input-reset ba b--black-20 pa2 mb2 db w-100"
             :placeholder "name"
             :value       value
             :on-blur     #(-> write-ref-path (.set value))
             :on-change   #(callback (-> % .-target .-value))}]))

(defn engineer-input [resource]
  (let [resource-sub (rf/subscribe [(keyword resource)])
        state (reagent/atom nil)]
    (fn []
      (let [{engineer :engineer} @resource-sub
            callback #(reset! state %)]
        [input-comp (or @state engineer) resource callback]))))

