(ns gloo-resources.date-picker
  (:require cljsjs.pikaday
            cljsjs.moment
            [re-frame.core :as rf]
            [gloo-resources.firebase :as fb]
            [reagent.core :as reagent]))

(defn on-select [this-ref resource type]
  (let [unix (-> this-ref (.getMoment) (.unix))
        write-ref-path (fb/path-str->db-ref (str "jenkins-info/" resource "/" (name type)))]
    (-> write-ref-path (.set unix))))

(defn date-picker [resource _type & [date]]
  (let [pikday-ref (reagent/atom nil)
        resource-sub (rf/subscribe [(keyword resource)])]
    (reagent/create-class
      {:display-name
       "date-picker"
       :component-did-mount
       (fn [comp]
         (let [node (reagent/dom-node comp)
               picker (js/Pikaday. #js {:field    node
                                        :format   "D MMM YYYY"
                                        :onSelect #(this-as this (on-select this resource _type))})]
           (reset! pikday-ref picker)))
       :reagent-render
       (fn [resource _type & [date]]
         (when (and @pikday-ref @resource-sub)
           (let [picker @pikday-ref
                 date (_type @resource-sub)]
             (.setMoment picker (-> js/moment (.unix date)))))
         [:input {:class "input-reset ba b--black-20 pa2 mb2 db w-100"
                  :type  "text"}])})))


