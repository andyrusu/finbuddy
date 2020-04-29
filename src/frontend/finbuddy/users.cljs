(ns finbuddy.users
  (:require
   [firebase :as fb]
   [finbuddy.db :as db]
   [finbuddy.router :refer [router current-top-name goto]]
   [finbuddy.notification :as notify :refer [as-danger]]))

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
                         (let [route-name (current-top-name)
                               user? (not (= nil user))
                               auth-route? (.indexOf [:app :profile] route-name)
                               guest-route? (.indexOf [:login :signup :forgot] route-name)]
                           (cond
                             (and auth-route? (not user?)) (goto :login)
                             (and guest-route? user?) (goto :app)
                             :else (goto :signup))))))

(defn login
  [email password]
  (-> (get-auth)
      (.signInWithEmailAndPassword email password)
      (.catch #(notify/as-danger (.-message %) :login))))

(defn forgot
  [email]
  (-> (get-auth)
      (.sendPasswordResetEmail email)
      (.then (fn []
               (notify/as-success "Email has been sent." :forgot)))
      (.catch #(notify/as-danger (.-message %) :forgot))))

(defn logout []
  (.signOut (get-auth)))