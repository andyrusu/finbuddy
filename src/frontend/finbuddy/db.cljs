(ns finbuddy.db
  (:require
   [reagent.core :as r]
   [cljs.reader :refer [read-string]])
  (:import goog.storage.mechanism.HTML5SessionStorage))

;; (defn save-to-session
;;   [notifications]
;;   (let [store (HTML5SessionStorage.)]
;;     (.set store "notifications" (pr-str notifications))
;;     notifications))

;; (defn load-from-session
;;   []
;;   (let [store (HTML5SessionStorage.)]
;;     (read-string (.get store "notifications"))))

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