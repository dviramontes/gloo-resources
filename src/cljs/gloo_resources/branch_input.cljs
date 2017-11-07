(ns gloo-resources.branch-input
  (:require [re-frame.core :as rf]
            [gloo-resources.firebase :as fb]
            [reagent.core :as reagent]))

(defn branch-input [resource]
  (let [state (reagent/atom nil)
        resource-sub (rf/subscribe [(keyword resource)])
        write-ref-path (fb/path-str->db-ref (str "jenkins-info/" resource "/branch"))]
    (fn []
      (let [{branch :branch} @resource-sub]
        [:input {:type      "text"
                 :class     "input-reset ba b--black-20 pa2 mb2 db w-100"
                 :placeholder branch
                 :on-blur   #(-> write-ref-path (.set @state))
                 :on-change #(reset! state (-> % .-target .-value))}]))))