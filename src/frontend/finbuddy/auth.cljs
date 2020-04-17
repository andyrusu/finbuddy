(ns finbuddy.auth
  (:require 
   [goog.dom :as gdom]
   [goog.dom.forms :as gform]
   [finbuddy.db :as db]
   [finbuddy.users :as users]
   [finbuddy.validation :as val :refer [login-form forgot-form]]))

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
        (-> (users/get-auth)
            (.setPersistence
             (if remember
               (users/get-persistence :local)
               (users/get-persistence :session)))
            (.then (fn [] (users/login email password)))
            (.catch #(users/error-handler % :login)))))))

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
        (users/forgot email)))))