(ns finbuddy.pages.login
  (:require
   [goog.dom :as gdom]
   [goog.dom.forms :as gform]
   [finbuddy.db :as db]
   [finbuddy.users :as users]
   [finbuddy.notification :as notify :refer [as-danger get-by-type]]
   [finbuddy.components.form :refer [input-field check-field]]
   [finbuddy.components.app :refer [notification]]
   [finbuddy.components.auth :refer [signup-link forgot-link]]
   [finbuddy.validation :as val :refer [login-form forgot-form]]
   [finbuddy.components.layouts :refer [simple]]))

(defn show-errors
  [email password remember]
  )

(defn do-login
  [email password remember]
  (let [errors (val/login-form email password)]
    (if errors
      (db/set-form! {:email {:value email
                             :error (:email errors)}
                     :password {:value ""
                                :error (:password errors)}
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
            (.catch #(notify/as-danger (.-message %) :login)))))))

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
    (when-not (empty? (notify/get-by-type :login))
      (let [note (last (:notifications @db/content))]
        [notification (:id note) (:message note) (:class note)]))
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