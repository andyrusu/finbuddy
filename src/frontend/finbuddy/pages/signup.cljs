(ns finbuddy.pages.signup
  (:require
   [validator :as v]
   [finbuddy.components.layouts :refer [simple]]
   [finbuddy.components.auth :refer [login-link]]))

(defn validate-form
  [email password repeated-password]
  (cond
    (not (v/isEmail email)) {}))

(defn show
  []
  [simple
   [:div.box
    [:h1.title.has-text-dark.has-text-centered "Signup"]
    [:div.is-divider]
    [:a.button.is-fullwidth.is-google
     [:span.icon [:i.fab.fa-google]]
     [:span "Google"]]
    [:br]
    [:a.button.is-fullwidth.is-facebook
     [:span.icon [:i.fab.fa-facebook]]
     [:span "Facebook"]]
    [:div.is-divider {:data-content "OR"}]
    [:form#signup
     {:action ""}
     [:div.field
      [:label.label {:for "email"} "Email"]
      [:div.control.has-icons-left
       [:input.input
        {:required "required"
         :name "email"
         :placeholder "e.g. bobsmith@gmail.com"
         :type "email"}]
       [:span.icon.is-small.is-left [:i.fa.fa-envelope]]]]
     [:div.field
      [:label.label {:for "password"} "Password"]
      [:div.control.has-icons-left
       [:input.input
        {:required "required"
         :name "password"
         :placeholder "*******"
         :type "password"}]
       [:span.icon.is-small.is-left [:i.fa.fa-lock]]]]
     [:div.field
      [:label.label {:for "password-repeated"} "Repeat Password"]
      [:div.control.has-icons-left
       [:input.input
        {:required "required"
         :name "password-repeated"
         :placeholder "*******"
         :type "password"}]
       [:span.icon.is-small.is-left [:i.fa.fa-lock]]]]
     [:div.field
      [:button.button.is-success
       "Register"]]]
    [:div.is-divider {:data-content "OR"}]
    [login-link]]])