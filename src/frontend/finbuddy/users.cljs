(ns finbuddy.users
  (:require
   [firebase :as fb]
   [finbuddy.db :as db]
   [finbuddy.notification :as notify :refer [add! as-danger]]))

(defn get-auth
  []
  (fb/auth))

(defn get-persistence
  [type]
  (aget fb/auth "Auth" "Persistence" (case type
                                       :local "LOCAL"
                                       :session "SESSION"
                                       "NONE")))

(defn get-current-user
  []
  (.-currentUser (get-auth)))

(defn observe-state-changed
  [observer]
  (.onAuthStateChanged (get-auth) observer))

(defn login
  [email password]
  (-> (get-auth)
      (.signInWithEmailAndPassword email password)
      (.catch #(notify/add! (notify/as-danger (.-message %) :login)))))

(defn forgot
  [email]
  (-> (get-auth)
      (.sendPasswordResetEmail email)
      (.then #(notify/add! (notify/as-success "Email has been sent." :forgot)))
      (.catch #(notify/add! (notify/as-danger (.-message %) :forgot)))))

(defn logout []
  (.signOut (get-auth)))