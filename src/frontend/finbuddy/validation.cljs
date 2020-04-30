(ns finbuddy.validation
  (:require
   [validator :as v]))



(defn forgot-form
  [email]
  (let [email-msg (if (v/isEmpty email)
                    "Please write your email address."
                    (if (not (v/isEmail email))
                      "Please write a valid email address."
                      false))]
    (if email-msg
      {:email email-msg}
      false)))