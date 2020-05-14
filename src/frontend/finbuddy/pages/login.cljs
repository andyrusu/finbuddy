(ns finbuddy.pages.login
  (:require
   [goog.dom :as gdom]
   [goog.dom.forms :as gform]
   [validator :as v]
   [finbuddy.db :as db]
   [finbuddy.users :as users]
   [finbuddy.notification :as notify]
   [finbuddy.components.form :refer [input-field check-field]]
   [finbuddy.components.app :refer [notification]]
   [finbuddy.components.auth :refer [signup-link forgot-link]]
   [finbuddy.components.layouts :refer [simple]]))

(defn validate
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

(defn do-login
  [email password remember]
  (let [errors (validate email password)]
    (if errors
      (db/set-form! {:email {:value email
                             :error (:email errors)}
                     :password {:value ""
                                :error (:password errors)}
                     :remember {:value remember
                                :error nil}})
      (-> (users/get-auth)
          (.setPersistence
           (if remember
             (users/get-persistence :local)
             (users/get-persistence :session)))
          (.then #(users/login email password))
          (.catch #(notify/add! (notify/as-danger (.-message %) :login)))))))

(defn submit-handler
  [event]
  (.preventDefault event)
  (let [form (gdom/getElement "login")]
    (do-login 
     (gform/getValueByName form "email")
     (gform/getValueByName form "password")
     (boolean (gform/getValueByName form "remember")))))

(defn show
  []
  [simple
   [:div.box
    [:h1.title.has-text-dark.has-text-centered "Login"]
    [:div.is-divider]
    [:a.button.is-fullwidth.is-google
     [:span.icon [:i.fab.fa-google]]
     [:span "Google"]]
    [:br]
    [:a.button.is-fullwidth.is-facebook
     [:span.icon [:i.fab.fa-facebook]]
     [:span "Facebook"]]
    [:br]
    [:a.button.is-fullwidth.is-microsoft
     [:span.icon [:i.fab.fa-microsoft]]
     [:span "Microsoft"]]
    [:div.is-divider {:data-content "OR"}]
    (when-not (empty? @notify/notifications)
      (let [note (->> @notify/notifications
                      (notify/get-by-source :login)
                      (last)
                      (notify/mark-flash!))]
        (js/console.log note)
        [notification (:id note) (:message note) (:severity note)]))
    [:form#login
     {:action "#"}
     (let [{value :value
            error :error} (db/get-form-field :email)]
       [input-field {:label "Email"
                     :value value
                     :error error
                     :input-options {:id "email"
                                     :required "required"
                                     :class (cond
                                              (and value error) "input is-danger"
                                              (and value (= false error)) "input is-success"
                                              :else "input")
                                     :name "email"
                                     :placeholder "e.g. bobsmith@gmail.com"
                                     :type "email"
                                     :value value
                                     :on-change #(db/update-form-field! :email (gform/getValue (.-currentTarget %)))}}])
     (let [{value :value
            error :error} (db/get-form-field :password)]
       [input-field {:label "Password"
                     :value value
                     :error error
                     :input-options {:id "password"
                                     :required "required"
                                     :class (cond
                                              (and value error) "input is-danger"
                                              (and value (= false error)) "input is-success"
                                              :else "input")
                                     :name "password"
                                     :placeholder "**********"
                                     :type "password"
                                     :value value
                                     :on-change #(db/update-form-field! :password (gform/getValue (.-currentTarget %)))}}])
     [check-field {:label "Remember me"
                   :key :remember
                   :name "remember"}]
     [:div.field
      [:button.button.is-success
       {:on-click submit-handler}
       "Login"]]]
    [:div.is-divider {:data-content "OR"}]
    [signup-link]
    " | "
    [forgot-link]]])