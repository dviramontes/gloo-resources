(ns gloo-resources.db)

(def default-db
  {:auth0-authenticated?  false
   :jenkins-resources     {}
   :all-jenkins-resources {}
   :sort-by-type          :all
   :on-app-failure        {:show? false
                           :msg   nil}})
