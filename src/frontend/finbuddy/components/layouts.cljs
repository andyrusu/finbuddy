(ns finbuddy.components.layouts
  (:require [finbuddy.components.app :refer [hero footer]]))

(defn simple
  [page]
  [:section.hero.is-primary.is-fullheight
   [:div.hero-body
    [:div.container
     [:div.columns.is-centered
      [:div.column.is-5-tablet.is-5-desktop.is-5-widescreen
       page]]]]])

(defn complete
  [page]
  [:div
   [hero "FinanceBuddy" "Your home finance helper!"]
   page
   [footer "FinanceBuddy" "Andy Rusu"]])
