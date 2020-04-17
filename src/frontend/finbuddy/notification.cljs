(ns finbuddy.notification
  (:require 
   [finbuddy.db :as db]
   [nano-id.core :refer [nano-id]]))

(defn create
  ([message] (create (nano-id) message "is-primary"))
  ([message class] (create (nano-id) message class))
  ([id message class]
   (let [notifications (:notifications @db/content)]
     (db/set-notifications!
      (conj notifications {:id id
                           :message message
                           :class class})))))

(defn delete
  [id]
  (let [notifications (:notifications @db/content)]
    (db/set-notifications!
     (remove #(= (:id %) id) notifications))))

;; (defn get
;;   [id]
;;   (let [notifications (:notifications @db/content)]
;;     (clojure.core/first
;;      (remove #(not (= (:id %) id)) notifications))))

;; (defn first
;;   []
;;   (clojure.core/first (:notifications @db/content)))

;; (defn last
;;   []
;;   (clojure.core/last (:notifications @db/content)))

;; (defn empty?
;;   []
;;   (clojure.core/empty (:notifications @db/content)))