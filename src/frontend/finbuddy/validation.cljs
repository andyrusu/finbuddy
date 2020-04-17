(ns finbuddy.validation
  (:require
   [validator :as v]))

(defn login-form
  [email password]
  (let [email-msg (if (v/isEmpty email)
                    "Please write your email address."
                    (if (not (v/isEmail email))
                      "Please write a valid email address."
                      false))
        psw-msg (if (v/isEmpty password)
                  "Please write your password."
                  false)]
    (if (or email-msg psw-msg)
      {:email email-msg
       :password psw-msg}
      false)))