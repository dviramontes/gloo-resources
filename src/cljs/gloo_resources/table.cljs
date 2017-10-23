(ns gloo-resources.table
  (:require cljsjs.handsontable
            [re-frame.core :as rf]
            [reagent.core :as reagent]))

(def query "
  {
    allResources {
      url
      name
      branch
      endDate
      engineer
      startDate
    }
  }")

(def db-key :allResources)

(defn table []
  (let [resources-subs (rf/subscribe [db-key])
        re-assign-symbol "[x]"
        cols #js ["name"
                  "endDate"
                  "url"
                  "branch"
                  "engineer"
                  "startDate"
                  "re-assign"]
        resources (map #(assoc % :re-assign re-assign-symbol) @resources-subs)
        columns (map #(hash-map :data % :readOnly true :renderer "html") cols)]
    (if-let [dom-node (js/document.querySelector "#table")]
      (let [h-table
            (js/Handsontable.
              dom-node
              #js {:data           (clj->js resources)
                   :columns        (clj->js columns)
                   :rowHeaders     false
                   :colHeaders     cols
                   :afterSelection (fn [r c]
                                     (this-as this-js
                                       (let [col-info (js->clj (.getDataAtCol this-js c))
                                             row-info (js->clj (.getDataAtRow this-js r))]
                                         (when (some #(= re-assign-symbol %) col-info)
                                           (prn row-info)))))})])
      (rf/dispatch [:fetch-graph db-key query]))
    [:div#table]))
