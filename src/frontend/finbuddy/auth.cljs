(ns finbuddy.auth
  (:require 
   [goog.dom :as gdom]
   [goog.dom.forms :as gform]
   [finbuddy.db :as db]
   [finbuddy.users :as users]
   [finbuddy.validation :as val :refer [login-form forgot-form]]))


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