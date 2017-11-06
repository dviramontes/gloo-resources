(ns gloo-resources.date-picker
  (:require cljsjs.pikaday
            cljsjs.moment
            [gloo-resources.firebase :as fb]
            [reagent.core :as reagent]))

(defn onSelect [this-ref resource type]
  (let [unix (-> this-ref (.getMoment) (.unix))
        write-ref-path (fb/path-str->db-ref (str "jenkins-info/" resource "/" (name type)))]
    (-> write-ref-path (.set unix))))

(defn date-picker [resource type & [date]]
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
                                                    (onSelect this resource type)))})]))


     :reagent-render
     (fn []
       [:input {:class "input-reset ba b--black-20 pa2 mb2 db w-100"
                :type  "text"}])}))

