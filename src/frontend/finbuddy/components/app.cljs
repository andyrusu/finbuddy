(ns finbuddy.components.app
  (:require [reagent.core :as r]
            [react :as react]
            [goog.dom :as gd]
            [finbuddy.auth :refer [logout]]
            [finbuddy.notification :as notify :refer [delete]]))

(defn notification
  ([id message] (notification id message "is-primary"))
  ([id message class]
   [:div
    {:key id
     :class (str "notification " class)}
    [:button {:class "delete" :on-click #(notify/delete id)} nil]
    message]))

(defn hero
  [title subtitle]
  "This is the hero component, acts like a page header."
  [:section.hero.is-dark
   [:div.hero-head
    [:nav.navbar
     [:div.navbar-menu
      [:div.navbar-end
       [:a.navbar-item
        {:href "#"
         :on-click #(do (.preventDefault %) (logout))}
        "Logout"]]]]]
   [:div.hero-body
    [:div.container
     [:h1.title title]
     [:h2.subtitle subtitle]]]])

(defn footer
  [name author]
  "This is the page footer."
  [:footer.footer
   [:div.content.has-text-centered
    [:p [:strong name] " by " author "."]]])

(defn modal
  ([body] (modal {} body))
  ([{:keys [title accept cancel is-active?]
     :or {title ""
          accept [:button.button.is-danger "Yes"]
          cancel [:button.button "No"]
          is-active? false}}
    & body]
   [:div {:class (str "modal" (when is-active? " is-active"))}
    [:div.modal-background]
    [:div.modal-card
     [:header.modal-card-head
      [:p.modal-card-title title]
      [:button.delete {:aria-label "close"}]]
     (if (vector? body)
       body
       [:section.modal-card-body body])
     [:footer.modal-card-foot accept cancel]]]))

(defn trans-form
  []
  [:form
   [:div.field
    [:label.label "Name"]
    [:div.control
     [:input.input
      {:placeholder "e.g. Salary", :name "name", :type "text"}]]]
   [:div.field
    [:label.label "Amount"]
    [:div.control
     [:input.input
      {:placeholder "e.g. 100", :name "amount", :type "number"}]]]
   [:div.field
    [:div.control
     [:label.checkbox
      [:input {:name "is-expense", :type "checkbox"}]
      "Is this an expense?"]]]
   [:div.field
    [:label.label "When will it occure?"]
    [:div.control
     [:input.input
      {:placeholder "e.g. 15/02/2020", :name "date", :type "text"}]]]
   [:div.field
    [:div.control
     [:label.checkbox
      [:input {:name "is-reaccurring", :type "checkbox"}]
      "Is this reaccurring?"]]]
   [:div.field
    [:label.label "Notes"]
    [:div.control
     [:textarea.textarea {:placeholder "e.g. Yay salary day!"}]]]])

(defn confirm-delete
  "Modal that asks user to confirm that they want to delete a transaction."
  ([name date amount] (confirm-delete {} name date amount))
  ([options name date amount]
   [modal (merge {:title (str "You are deleting transaction " name ".")} options)
    "Are you sure you wish to delete transaction" name " (from " date " in the amount of " amount ")?"]))

(defn add-transaction
  "Modal that shows the form that is used to add transactions."
  ([] (add-transaction {}))
  ([options]
   [modal (merge {:title "Add new Transaction"} options)
    [trans-form]]))

(defn controls
  []
  [:div.level
   [:div.level-left
    [:div.level-item
     [:button.button [:span.icon [:i.fas.fa-angle-double-left]]]]
    [:div.level-item
     [:button.button [:span.icon [:i.fas.fa-angle-left]]]]
    [:div.level-item
     [:div.select
      [:select
       [:option {:value "1"} "January"]
       [:option {:value "2"} "February"]
       [:option {:value "3"} "March"]
       [:option {:value "4"} "April"]
       [:option {:value "5"} "May"]
       [:option {:value "6"} "June"]
       [:option {:value "7"} "July"]
       [:option {:value "8"} "August"]
       [:option {:value "9"} "September"]
       [:option {:value "10"} "October"]
       [:option {:value "12"} "November"]
       [:option {:value "13"} "December"]]]]
    [:div.level-item
     [:div.select
      [:select
       [:option {:value "2020"} "2020"]
       [:option {:value "2021"} "2021"]]]]
    [:div.level-item
     [:button.button [:span.icon [:i.fas.fa-angle-right]]]]
    [:div.level-item
     [:button.button [:span.icon [:i.fas.fa-angle-double-right]]]]]
   [:div.level-right
    [:div.level-item
     [:button.button.is-primary
      [:span.icon [:i.fas.fa-plus]]
      [:span "Add new transaction"]]]]])

(defn summary
  []
  [:div.box
   [:h2.subtitle "This month's report"]
   [:div.columns
    [:div.column
     [:div.notification.has-text-centered.is-success
      [:h2.subtitle "Income:"]
      [:h1.title "2500"]]]
    [:div.column
     [:div.notification.has-text-centered.is-warning
      [:h2.subtitle "Expenses:"]
      [:h1.title "-2600"]]]
    [:div.column
     [:div.notification.has-text-centered.is-danger
      [:h2.subtitle "Savings:"]
      [:h1.title "-100"]]]]])

(defn transaction
  ([data] (transaction {} data))
  ([options {:keys [day name amount notes]
             :or {amount 0
                  notes ""}}]
   [:tr options
    [:th day]
    [:td name]
    [:td amount]
    [:td notes]
    [:td
     [:button.button.is-small
      [:span.icon.is-small [:i.far.fa-edit]]]
     [:button.button.is-small.is-danger
      [:span.icon.is-small [:i.far.fa-trash-alt]]]]]))

(defn transactions
  []
  [:div.box
   [:table.table.is-stripped.is-fullwidth
    [:thead
     [:tr
      [:th "Day"]
      [:th "Transaction Name"]
      [:th "Amount"]
      [:th "Notes"]
      [:th "Actions"]]]
    [:tfoot
     [:tr
      [:th "Day"]
      [:th "Transaction Name"]
      [:th "Amount"]
      [:th "Notes"]
      [:th "Actions"]]]
    [:tbody
     [transaction {:day "05"
                   :name "Salary"
                   :amount 2500}]
     [transaction {:class "is-selected"} {:day "06"
                                          :name "Shopping"
                                          :amount -2600}]]]])

(defn main
  [title]
  (react/useEffect 
   #(gd/setTextContent 
     (gd/getElementByTagNameAndClass "title") 
     (.-children title)))
  (r/as-element
   [:div
    [hero "FinanceBuddy" "Your home finance helper!"]
    [:section.section
     [:div.container
      [controls]
      [summary]
      [transactions]]]
    [footer "FinanceBuddy" "Andy Rusu"]]))
