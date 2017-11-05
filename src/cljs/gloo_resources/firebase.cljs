(ns gloo-resources.firebase)

(def firebase-app (js/firebase))

(def firebase-db-ref
  "A Firebase ref (a firebase.database.Reference instance) referring to the
	root of the database of this firebase-app."
  (-> firebase-app .database .ref))


