(ns finbuddy.db
  (:require
   [reagent.core :as r]))

(def content (r/atom {:form nil
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

(defn get-form-field
  ([field] (get-form-field field nil))
  ([field default]
   (get-in @content [:form field] default)))

(defn update-form-field!
  [field value]
  (swap! content assoc-in [:form field :value] value))