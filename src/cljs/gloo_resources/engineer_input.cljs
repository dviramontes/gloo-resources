(ns gloo-resources.engineer-input
  (:require [re-frame.core :as rf]
            [gloo-resources.firebase :as fb]
            [reagent.core :as reagent]))

(defn- input-comp [value callback & [toggle-animation]]
  (let [base-classes "input-reset ba b--black-20 pa2 mb2 db w-100"
        show-anim? (reagent/atom false)]
    (fn [value callback]
      [:div
       [:input {:type        "text"
                :class       "animate"
                :style       (when toggle-animation
                               {:border "1rem solid red"})
                :placeholder "name"
                :value       value
                :on-change   #(callback (-> % .-target .-value))}]])))

(defn engineer-input [resource]
  (let [resource-sub (rf/subscribe [(keyword resource)])
        fb-write-path (fb/path-str->db-ref (str "jenkins-info/" resource "/engineer"))
        state (reagent/atom nil)]
    (fn []
      (let [{engineer :engineer} @resource-sub
            callback (fn [val]
                       (-> fb-write-path (.set val))
                       (reset! state val))
            should-animate? (if engineer true false)]
        [input-comp (or @state engineer) callback should-animate?]))))