(ns finbuddy.notification
  (:require
   [reagent.core :as r]
   [nano-id.core :refer [nano-id]])
  (:import goog.structs.PriorityQueue))

(def notifications (r/atom []))

(add-watch notifications :note #(js/console.log %))

(def priority-map {:primary 4
                   :link 6
                   :info 5
                   :success 3
                   :warning 2
                   :danger 1})

(defn get-by-type
  "TO BE REMOVED!"
  [_]
  [])

(defn mark-flash-batch!
  [notifications-to-mark]
  (let [ids (set (map #(:id %) notifications-to-mark))]
    (swap! notifications 
           (fn [notifications ids]
             (vec
              (for [notification notifications
                    :let [truify (constantly true)]]
                (if (and (= nil (ids (:id notification))) 
                         (= :flash (:type notification)))
                  notification
                  (update notification :seen? truify)))))
           ids)))

(defn mark-flash!
  [notification]
  (mark-flash-batch! [notification])
  notification)

(defn get-by-source
  [source notifications]
  (filter #(= (:source %) source) notifications))

(defn add!
  [notification]
  (swap! notifications conj notification))

(defn delete!
  [id]
  (reset! notifications (remove #(= id (:id %)) @notifications)))

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