(ns finbuddy.auth
  (:require 
   [goog.dom :as gdom]
   [goog.dom.forms :as gform]
   [firebase :as fb]
   [finbuddy.db :as db]
   [finbuddy.validation :as val]
   [finbuddy.notification :as notify :refer [create]]))

(defn get-auth
  []
  (fb/auth))

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

(defn login 
  [email password]
  (-> (get-auth)
      (.signInWithEmailAndPassword email password)
      (.catch (fn [error]
                (notify/create (.-message error) :login "is-danger")))))

(defn handle-login
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
      (login email password))))

(defn logout []
  (.signOut (get-auth)))