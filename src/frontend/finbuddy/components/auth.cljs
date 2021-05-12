(ns finbuddy.components.auth
  (:require
   [goog.dom.forms :as gform]
   [finbuddy.db :as db]
   [finbuddy.components.app :refer [link]]))

(defn login-link
  []
  [link {:routeName :login} "Login"])

(defn signup-link
  []
  [link {:routeName :signup} "Signup"])

(defn forgot-link
  []
  [link {:routeName :forgot} "Forgot password"])

(defn field
  [label name type placeholder key]
  (let [value (get-in @db/content [:form key :value] nil)
        error (get-in @db/content [:form key :error] nil)]
    [:div.field
     [:label.label {:for name} label]
     [:div.control.has-icons-left.has-icons-right
      [:input
       {:id name
        :required "required"
        :class (cond
                 (and value error) "input is-danger"
                 (and value (= false error)) "input is-success"
                 :else "input")
        :name name
        :placeholder placeholder
        :type type
        :value value
        :on-change #(swap!
                     db/content
                     assoc-in
                     [:form key :value]
                     (gform/getValue (.-currentTarget %)))}]
      [:span.icon.is-small.is-left [:i.fa.fa-envelope]]
      (if error
        [:span.icon.is-small.is-right [:i.fa.fa-exclamation-triangle]]
        (when (and value (= false error)) [:span.icon.is-small.is-right [:i.fa.fa-check]]))
      (when error [:p.help.is-danger error])]]))

(defn check-field
  [label name key]
  [:div.field
   [:label.checkbox
    {:for name}
    [:input {:type "checkbox"
             :name name
             :checked (get-in @db/content [:form key :value] false)
             :on-change #(swap!
                         db/content
                         assoc-in
                         [:form key :value]
                         (not (boolean (get-in @db/content [:form key :value]))))}]
    label]])



