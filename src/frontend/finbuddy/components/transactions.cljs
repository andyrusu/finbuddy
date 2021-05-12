(ns finbuddy.components.transactions
  (:require [finbuddy.components.app :refer [modal]]))

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