(ns finbuddy.auth
  (:require 
   [goog.dom :as gdom]
   [goog.dom.forms :as gform]
   [firebase :as fb]
   [finbuddy.db :as db]
   [finbuddy.validation :as val :refer [login-form forgot-form]]
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

(defn login-handler
  [event]
  (.preventDefault event)
  (let [form (gdom/getElement "login")
        email (gform/getValueByName form "email")
        password (gform/getValueByName form "password")
        remember (boolean (gform/getValueByName form "remember"))
        errors (val/login-form email password)]
    (if errors
      (swap! db/content
             assoc
             :form
             {:email {:value email
                      :error (get errors :email)}
              :password {:value ""
                         :error (get errors :password)}
              :remember {:value remember
                         :error nil}})
      (do
        (db/clear-form!)
        (-> (get-auth)
            (.setPersistence
             (if remember
               (get-persistence :local)
               (get-persistence :session)))
            (.then (fn [] (login email password)))
            (.catch #(error-handler % :login)))))))

(defn forgot
  [email]
  (-> (get-auth)
      (.sendPasswordResetEmail email)
      (.then (fn []
               (notify/create "Email has been sent." :forgot "is-success")))
      (.catch #(error-handler % :forgot))))

(defn forgot-handler
  [event]
  (.preventDefault event)
  (let [email (gform/getValue (gdom/getElement "email"))
        error (val/forgot-form email)]
    (if error
      (swap! db/content
             assoc
             :form
             {:email {:value email
                      :error (:email error)}})
      (do
        (db/clear-form!)
        (forgot email)))))

(defn logout []
  (.signOut (get-auth)))