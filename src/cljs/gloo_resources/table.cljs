(ns gloo-resources.table
  (:require cljsjs.handsontable
            [gloo-resources.queries :as queries]
            [re-frame.core :as rf]
            [reagent.core :as reagent]))

(defn table []
  (rf/dispatch [:fetch-graph :allResources queries/resources])
  (let [resources (rf/subscribe [:allResources])]
    (fn []
      (reagent/create-class
        {:component-did-mount
         (fn []
           (let [re-assign-symbol "[x]"
                 cols #js ["name"
                           "endDate"
                           "url"
                           "branch"
                           "engineer"
                           "startDate"
                           "re-assign"]
                 dom-node (js/document.querySelector "#table")
                 resources (map #(assoc % :re-assign re-assign-symbol) @resources)
                 columns (map #(hash-map :data % :readOnly true :renderer "html") cols)
                 htable (js/Handsontable.
                          dom-node
                          #js {:data       (clj->js resources)
                               :rowHeaders false
                               :colHeaders cols
                               :columns    (clj->js columns)

                               :afterSelection (fn [r c]
                                                 (this-as this-js
                                                   (let [col-info (js->clj (.getDataAtCol this-js c))
                                                         row-info (js->clj (.getDataAtRow this-js r))]
                                                     (when (some #(= re-assign-symbol %) col-info)
                                                       (prn row-info)))))})]))


         :display-name
         "resources-table-component"                              ;; for more helpful warnings & errors

         :reagent-render
         (fn []
           [:div#table])}))))
