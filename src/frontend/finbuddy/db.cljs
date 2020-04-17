(ns finbuddy.db
  (:require
   [reagent.core :as r]))

(def content (r/atom {:show-page :app
                      :form nil
                      :notifications []}))

(defn set-page!
  [page]
  (swap! content #(assoc % :show-page page)))

(defn set-form!
  [data]
  (swap! content #(assoc % :form data)))

(defn clear-form!
  []
  (swap! content #(assoc % :form nil)))

(defn set-notifications!
  [notifications]
  (swap! content #(assoc % :notifications notifications)))