(ns finbuddy.router
  (:require [finbuddy.db :as db]
            [finbuddy.users :as users :refer [get-current-user]]
            [finbuddy.notification :refer [delete-by-source!]]
            [router5 :as r5]
            [router5-plugin-browser :as r5-browser]))

(defn auth-gate
  [_router]
  (fn [_to-state _from-state done]
    (done (when (= nil (get-current-user)) (clj->js {:redirect {:name :login}})))))

(defn guest-gate
  [_router]
  (fn [_to-state _from-state done]
    (done (when (get-current-user) (clj->js {:why "User already logged in"})))))

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
    (when
     (or
      (nil? to-state)
      (nil? from-state)
      (not (.areStatesEqual router to-state from-state))) 
      (db/clear-form!))
    (done)))

(defn clear-notifications-middleware
  [router]
  (fn [to-state from-state done]
    (cond
      (nil? from-state) (db/clear-notifications!)
      (not (.areStatesEqual router from-state to-state)) (delete-by-source! (keyword (.-name from-state))))
    (done)))

(defn create
  [routes]
  (let [router (r5/createRouter (clj->js routes) #js {:defaultRoute :app})]
    (.usePlugin router (r5-browser))
    (.useMiddleware router clear-form-middleware)
    (.useMiddleware router clear-notifications-middleware)
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
   (goto route params #js {} nil))
  ([route params options]
   (goto route params options nil))
  ([route params options done]
   (.navigate router
              (name route)
              (clj->js params)
              (clj->js options)
              done)))

(defn init-auth-observer
  []
  (users/observe-state-changed
   (fn [user]
     (let [route-name (current-top-name)
           user? (not (= nil user))
           auth-route? (> (.indexOf [:app :profile] route-name) -1)
           guest-route? (> (.indexOf [:login :signup :forgot] route-name) -1)]
       (cond
         (and auth-route? (not user?)) (goto :login)
         (and guest-route? user?) (goto :app))))))