(ns gloo-resources.branch-input
  (:require [re-frame.core :as rf]
            [gloo-resources.firebase :as fb]
            [reagent.core :as reagent]))

(defn- input-comp [value callback]
  (let [base-classes "input-reset ba b--black-20 pa2 mb2 db w-100"]
    (fn [value callback]
      [:div
       [:input {:type        "text"
                :placeholder "name"
                :value       value
                :on-change   #(callback (-> % .-target .-value))}]])))

(defn branch-input [resource]
  (let [resource-sub (rf/subscribe [(keyword resource)])
        fb-write-path (fb/path-str->db-ref (str "jenkins-info/" resource "/branch"))
        state (reagent/atom nil)]
    (fn []
      (let [{branch :branch} @resource-sub
            callback (fn [val]
                       (-> fb-write-path (.set val))
                       (reset! state val))]
        [input-comp (or @state branch) callback]))))