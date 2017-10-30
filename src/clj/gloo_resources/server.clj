(ns gloo-resources.server
  (:require [gloo-resources.handler :refer [handler]]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]])

 (defn -main [& args]
   (let [port (Integer/parseInt (or (env :port) "5000"))]
     (run-jetty handler {:port port :join? false}))))
