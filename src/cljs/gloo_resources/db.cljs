(ns gloo-resources.db)

(def default-db
  {:auth0-authenticated? false
   :allResources         []
   :allUsers             []
   :allJenkinsResources  []
   :on-app-failure       {:show? false
                          :msg   nil}})
