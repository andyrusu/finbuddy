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

(defn as-primary
  ([message] (as-primary message :app))
  ([message type] (create message type "is-primary"))
  ([message type is-light] (create message type "is-primary is-light")))

(defn as-link
  ([message] (as-link message :app))
  ([message type] (create message type "is-link"))
  ([message type is-light] (create message type "is-link is-light")))

(defn as-info
  ([message] (as-info message :app))
  ([message type] (create message type "is-info"))
  ([message type is-light] (create message type "is-info is-light")))

(defn as-success
  ([message] (as-success message :app))
  ([message type] (create message type "is-success"))
  ([message type is-light] (create message type "is-success is-light")))

(defn as-warning
  ([message] (as-warning message :app))
  ([message type] (create message type "is-warning"))
  ([message type is-light] (create message type "is-warning is-light")))

(defn as-danger
  ([message] (as-danger message :app))
  ([message type] (create message type "is-danger"))
  ([message type is-light] (create message type "is-danger is-light")))

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