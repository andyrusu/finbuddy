(ns finbuddy.pages.forgot
  (:require
   [finbuddy.components.layouts :refer [simple]]
   [finbuddy.components.app :refer [notification]]
   [finbuddy.components.form :refer [input-field]]
   [finbuddy.components.auth :refer [signup-link login-link]]
   [finbuddy.notification :as notify]
   [finbuddy.users :as users]
   [finbuddy.db :as db]
   [validator :as v]
   [goog.dom :as gdom]
   [goog.dom.forms :as gform]))

(defn submit-handler
  [event]
  (.preventDefault event)
  (let [value (gform/getValue (gdom/getElement "email"))
        is-valid (v/isEmail  value)]
    (if is-valid
      (users/forgot value)
      (db/set-form! {:email {:value value
                             :error "Please provide a valid email address."}}))))

(defn show
  []
  [simple
   [:div.box
    [:h1.title.has-text-dark.has-text-centered "Forgot password"]
    [:hr]
    [notification (last (notify/filter-by-source :forgot (:notifications @db/content)))]
    [:form
     {:id "forgot"
      :action "#"}
     (let [{value :value
            error :error} (db/get-form-field :email)]
       [input-field {:label "Email"
                     :error ""
                     :input-options {:id "email"
                                     :name "email"
                                     :placeholder "e.g. bobsmith @gmail.com"
                                     :type "email"
                                     :value value
                                     :class (cond
                                              (and value error) "input is-danger"
                                              (and value (= false error)) "input is-success"
                                              :else "input")
                                     :on-change #(db/update-form-field! :email (gform/getValue (.-currentTarget %)))}}])
     [:div.field
      [:button.button.is-success
       {:on-click submit-handler}
       "Send Email"]]]
    [:hr]
    [signup-link]
    " | "
    [login-link]]])