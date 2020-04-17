(ns finbuddy.users
  (:require
   [firebase :as fb]
   [finbuddy.db :as db]
   [finbuddy.notification :as notify :refer [create]]))

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

(defn init-auth
  []
  (.onAuthStateChanged (get-auth)
                       (fn [user]
                         (if (not (= user nil))
                           (db/set-page! :app)
                           (when (= :app (:show-page @db/content)) (db/set-page! :login))))))

(defn error-handler
  [error type]
  (notify/create (.-message error) type "is-danger"))

(defn login
  [email password]
  (-> (get-auth)
      (.signInWithEmailAndPassword email password)
      (.catch #(error-handler % :login))))

(defn forgot
  [email]
  (-> (get-auth)
      (.sendPasswordResetEmail email)
      (.then (fn []
               (notify/create "Email has been sent." :forgot "is-success")))
      (.catch #(error-handler % :forgot))))

(defn logout []
  (.signOut (get-auth)))