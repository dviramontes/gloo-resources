(ns gloo-resources.firebase
  (:require cljsjs.firebase))

(defonce firebase-app-init
         {:apiKey            "AIzaSyBqb3R0mER0oSVS31B5RvhNV7b8pl1OT4s"
          :authDomain        "glooresourceallocation.firebaseapp.com"
          :databaseURL       "https://glooresourceallocation.firebaseio.com"
          :storageBucket     "glooresourceallocation.appspot.com"
          :projectId         "glooresourceallocation"
          :messagingSenderId "59532946679"})

(defonce firebase-app
         (.initializeApp js/firebase (clj->js firebase-app-init)))

(def firebase-db-ref
  "A Firebase ref (a firebase.database.Reference instance) referring to the
	root of the database of this firebase-app."
  (-> firebase-app .database .ref))

(defn path-str->db-ref
  "Returns a Firebase ref for the node at the given path string relative to
	firebase-db-ref, or to the first argument if called with two args."
  ([rel-path] (path-str->db-ref firebase-db-ref rel-path))
  ([db-ref rel-path] (.child db-ref rel-path)))