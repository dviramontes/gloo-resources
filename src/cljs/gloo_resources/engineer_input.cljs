(ns gloo-resources.engineer-input
  (:require [re-frame.core :as rf]
            [gloo-resources.firebase :as fb]
            [reagent.core :as reagent]))

(defn- input-comp [value fb-write-path callback]
  [:input {:type        "text"
           :class       "input-reset ba b--black-20 pa2 mb2 db w-100"
           :placeholder "name"
           :value       value
           :on-blur     #(-> fb-write-path (.set value))
           :on-change   #(callback (-> % .-target .-value))}])

(defn engineer-input [resource]
  (let [resource-sub (rf/subscribe [(keyword resource)])
        fb-write-path (fb/path-str->db-ref (str "jenkins-info/" resource "/engineer"))
        state (reagent/atom nil)]
    (fn []
      (let [{engineer :engineer} @resource-sub
            callback #(reset! state %)]
        [input-comp (or @state engineer) fb-write-path callback]))))