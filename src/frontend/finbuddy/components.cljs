(ns finbuddy.components
  (:require [reagent.core :as r]))

(defn app
  []
  [:div
   [hero]])

(defn hero
  [title subtitle]
  "This is the hero component, acts like a page header."
  [:section.hero.is-dark
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
  [title body cancel accept]
  [:div.modal
   [:div.modal-background]
   [:div.modal-card
    [:header.modal-card-head
     [:p.modal-card-title title]
     [:button.delete {:aria-label "close"}]]
    (if (vector? body)
      body
      [:section.modal-card-body body])
    [:footer.modal-card-foot accept cancel]]])

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