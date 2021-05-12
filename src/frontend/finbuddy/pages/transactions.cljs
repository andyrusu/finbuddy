(ns finbuddy.pages.transactions
  (:require [finbuddy.components.layouts :refer [complete]]
            [finbuddy.components.transactions :refer [controls transactions]]
            [finbuddy.components.reports :refer [summary]]))

(defn show []
  [complete
   [:section.section
    [:div.container
     [controls]
     [summary]
     [transactions]]]])