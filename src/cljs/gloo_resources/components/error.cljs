(ns gloo-resources.components.error
  (:use-macros [purnam.core :only [! def*]])
  (:require cljsjs.toastr
            [re-frame.core :as rf]))

(def* toastr-opts
      {:positionClass     "toast-bottom-center"
       :timeOut           0                                 ;; disable fadeout
       :preventDuplicates true
       :progressBar       true})

(defn error []
  (! js/toastr.options toastr-opts)
  (let [{:keys [show? msg]} @(rf/subscribe [:on-app-failure])]
    (when show? (js/toastr.error (str "Error: " (name msg))))
    [:div.error-component]))