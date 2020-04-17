(ns finbuddy.components.auth
  (:require
   [goog.dom.dataset :as gdata]
   [goog.dom.forms :as gform]
   [finbuddy.db :as db]
   [finbuddy.auth :refer [handle-login]]
   [finbuddy.components.app :refer [notification]]
   [finbuddy.notification :as notify :refer [get-by-type]]))

(defn handle-link
  [event]
  (.preventDefault event)
  (-> event
      (.-currentTarget)
      (gdata/get "page")
      (keyword)
      (db/set-page!)))

(defn login-link
  []
  [:a {:href "#" :on-click handle-link :data-page :login} "Login"])

(defn signup-link
  []
  [:a {:href "#" :on-click handle-link :data-page :signup} "Signup"])

(defn forgot-link
  []
  [:a {:href "#" :on-click handle-link :data-page :forgot} "Forgot password"])

(defn logout-link
  []
  [])

(defn layout
  [page]
  [:section.hero.is-primary.is-fullheight
   [:div.hero-body
    [:div.container
     [:div.columns.is-centered
      [:div.column.is-5-tablet.is-5-desktop.is-5-widescreen
       page]]]]])

(defn field
  [label name type placeholder key]
  (let [value (get-in @db/content [:form key :value] nil)
        error (get-in @db/content [:form key :error] nil)]
    [:div.field
     [:label.label {:for name} label]
     [:div.control.has-icons-left.has-icons-right
      [:input
       {:required "required"
        :class (cond
                 (and value error) "input is-danger"
                 (and value (= false error)) "input is-success"
                 :else "input")
        :name name
        :placeholder placeholder
        :type type
        :value value
        :onChange #(swap!
                    db/content
                    assoc-in
                    [:form key :value]
                    (gform/getValue (.-currentTarget %)))}]
      [:span.icon.is-small.is-left [:i.fa.fa-envelope]]
      (if error
        [:span.icon.is-small.is-right [:i.fa.fa-exclamation-triangle]]
        (when value [:span.icon.is-small.is-right [:i.fa.fa-check]]))
      (when error [:p.help.is-danger error])]]))

(defn check-field
  [label name key]
  [:div.field
   [:label.checkbox
    {:for name}
    [:input {:type "checkbox"
             :name name
             :checked (get-in @db/content [:form key :value] false)
             :onChange #(swap!
                         db/content
                         assoc-in
                         [:form key :value]
                         (not (boolean (get-in @db/content [:form key :value]))))}]
    label]])

(defn login
  []
  [layout 
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
     {:action ""}
     [field "Email" "email" "email" "e.g. bobsmith@gmail.com" :email]
     [field "Password" "password" "password" "********" :password]
     [check-field "Remember me" "remember" :remember]
     [:div.field
      [:button.button.is-success
       {:on-click handle-login}
       "Login"]]]
    [:div.is-divider {:data-content "OR"}]
    [signup-link]
    " | "
    [forgot-link]]])

(defn signup
  []
  [layout
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
    [:br]
    [:a.button.is-fullwidth.is-microsoft
     [:span.icon [:i.fab.fa-microsoft]]
     [:span "Microsoft"]]
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

(defn forgot
  []
  [layout
   [:div.box
    [:h1.title.has-text-dark.has-text-centered "Forgot password"]
    [:hr]
    [:form
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
      [:button.button.is-success
       "Send Email"]]]
    [:hr]
    [signup-link]
    " | "
    [login-link]]])