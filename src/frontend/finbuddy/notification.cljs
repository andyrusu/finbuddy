(ns finbuddy.notification
  (:require
   [finbuddy.db :as db]
   [nano-id.core :refer [nano-id]]))

;; (def priority-map {:primary 4
;;                    :link 6
;;                    :info 5
;;                    :success 3
;;                    :warning 2
;;                    :danger 1})

(defn filter-by-source
  [source notifications]
  (filter #(= (:source %) source) notifications))

(defn remove-by-id
  [id notifications]
  (remove #(= id (:id %) notifications)))

(defn remove-by-source
  [source notifications]
  (remove #(= source (:source %)) notifications))

(defn add!
  [notification]
  (let [new-notifications (conj (:notifications @db/content) notification)]
    (swap! db/content #(assoc % :notifications new-notifications))))

(defn delete!
  [id]
  (swap! db/content (fn [{:keys [notifications] :as content}]
                      (assoc content :notifications (remove-by-id id notifications)))))

(defn delete-by-source!
  [source]
  (swap! db/content (fn [{:keys [notifications] :as content}]
                      (assoc content :notifications (remove-by-source source notifications)))))

(defn create
  "Create a new notification and save it in the atom."
  ([message] (create message :app :flash :primary (nano-id)))
  ([message source] (create message source :flash :primary (nano-id)))
  ([message source type] (create message source type :primary (nano-id)))
  ([message source type severity] (create message source type severity (nano-id)))
  ([message source type severity id]
   {:id id
    :message message
    :source source
    :type type
    :severity severity
    :seen? false}))

(defn get-all
  []
  (:notifications db/content))

(defn get-by-source
  [source]
  (filter-by-source source (get-all)))

(defn has-by-source?
  [source]
  (boolean (seq (filter-by-source source (get-all)))))

(defn as-primary
  ([message] (create message :app :flash :primary (nano-id)))
  ([message source] (create message source :flash :primary (nano-id)))
  ([message source type] (create message source type :primary (nano-id))))

(defn as-link
  ([message] (create message :app :flash :link (nano-id)))
  ([message source] (create message source :flash :link (nano-id)))
  ([message source type] (create message source type :link (nano-id))))

(defn as-info
  ([message] (create message :app :flash :info (nano-id)))
  ([message source] (create message source :flash :info (nano-id)))
  ([message source type] (create message source type :info (nano-id))))

(defn as-success
  ([message] (create message :app :flash :success (nano-id)))
  ([message source] (create message source :flash :success (nano-id)))
  ([message source type] (create message source type :success (nano-id))))

(defn as-warning
  ([message] (create message :app :flash :warning (nano-id)))
  ([message source] (create message source :flash :warning (nano-id)))
  ([message source type] (create message source type :warning (nano-id))))

(defn as-danger
  ([message] (create message :app :flash :danger (nano-id)))
  ([message source] (create message source :flash :danger (nano-id)))
  ([message source type] (create message source type :danger (nano-id))))