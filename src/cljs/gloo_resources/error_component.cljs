(ns gloo-resources.error-component
  (:use-macros [purnam.core :only [! def*]])
  (:require cljsjs.toastr
            [re-frame.core :as rf]))

(def* toastr-opts
      {:positionClass "toast-bottom-center"
       :timeOut       5000
       :progressBar   true})

(defn error-component []
  (let [_ (! js/toastr.options toastr-opts)
        {:keys [show? msg]} @(rf/subscribe [:on-app-failure])]
    (when show? (js/toastr.error (str "Error: " (name msg))))
    [:div.error-component]))