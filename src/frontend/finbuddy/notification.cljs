(ns finbuddy.notification
  (:require 
   [finbuddy.db :as db]
   [nano-id.core :refer [nano-id]]))

(defn create
  ([message] (create message :app "is-primary" (nano-id)))
  ([message type] (create message type "is-primary" (nano-id)))
  ([message type class] (create message type class (nano-id)))
  ([message type class id]
   (let [notifications (:notifications @db/content)]
     (db/set-notifications!
      (conj notifications {:id id
                           :message message
                           :class class
                           :type type})))))

(defn delete
  [id]
  (let [notifications (:notifications @db/content)]
    (db/set-notifications!
     (remove #(= (:id %) id) notifications))))

(defn get-by-type
  [type]
  (filter #(= (:type %) type) (:notifications @db/content)))

(defn get-by-id
  [id]
  (first (filter #(= (:id %) id) (:notifications @db/content))))

;; (defn first
;;   []
;;   (clojure.core/first (:notifications @db/content)))

;; (defn last
;;   []
;;   (clojure.core/last (:notifications @db/content)))

;; (defn empty?
;;   []
;;   (clojure.core/empty (:notifications @db/content)))