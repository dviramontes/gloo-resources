(ns gloo-resources.date-picker
  (:require cljsjs.pikaday
            cljsjs.moment
            [reagent.core :as reagent]))

(defn date-picker [& [date]]
  (reagent/create-class
    {:display-name
     "date-picker"
     :component-did-mount
     (fn [comp]
       (let [node (reagent/dom-node comp)
             picker (js/Pikaday. #js {:field node
                                      :format "D MMM YYYY"
                                      :onSelect (fn []
                                                  (this-as this
                                                    (prn (-> this
                                                             (.getMoment)
                                                             (.format "D MMMM YYYY")))))})]))

     :reagent-render
     (fn []
       [:input {:class "input-reset ba b--black-20 pa2 mb2 db w-100"
                :type "text"}])}))

