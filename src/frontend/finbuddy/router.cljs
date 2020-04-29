(ns finbuddy.router
  (:require [finbuddy.db :as db]
            ;[finbuddy.users :refer [get-current-user]]
            [router5 :as r5]
            [router5-plugin-logger :as r5-logger]
            [router5-plugin-browser :as r5-browser]))

;; (defn auth-gate
;;   [router]
;;   (fn [to-state from-state done]
;;     (done (when (= nil (get-current-user)) (clj->js {:redirect {:name :login}})))))

;; (defn guest-gate
;;   [router]
;;   (fn [to-state from-state done]
;;     (js/console.log "gate: " (get-current-user))
;;     (done (when (get-current-user) (clj->js {:why "User already logged in"})))))

(def routes [{:name :login :path "/login"}
             {:name :signup :path "/signup"}
             {:name :forgot :path "/forgot"}
             {:name :profile :path "/profile"}
             {:name :app :path "/"}])

(defn top-name-from-route
  [route]
  (-> route (.-name) (.split '.') (get 0) (keyword)))

(defn clear-form-middleware
  [router]
  (fn [to-state from-state done]
    (let [from-name (when from-state (.-name from-state))
          to-name (when to-state (.-name to-state))]
      (when (not (= to-name from-name)) (db/clear-form!))
      (done))))

(defn create
  [routes]
  (let [router (r5/createRouter (clj->js routes) #js {:defaultRoute "app"})]
    (.usePlugin router r5-logger)
    (.usePlugin router (r5-browser))
    (.useMiddleware router clear-form-middleware)
    router))

(def router (create routes))

(defn current-state
  []
  (.getState router))

(defn current-route-name
  []
  (keyword (.-name (current-state))))

(defn current-top-name
  []
  (top-name-from-route (current-state)))

(defn goto
  ([route]
   (goto route #js {} #js {} nil))
  ([route params]
   (goto route #js {} #js {} nil))
  ([route params options]
   (goto route params options nil))
  ([route params options done]
   (.navigate router
              (name route)
              (clj->js params)
              (clj->js options)
              done)))