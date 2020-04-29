(ns finbuddy.router
  (:require [finbuddy.db :as db]
            [finbuddy.users :refer [get-current-user]]
            [router5 :as r5]
            [router5-plugin-logger :as r5-logger]
            [router5-plugin-browser :as r5-browser]))

(defn auth-gate
  [router]
  (fn [to-state from-state done]
    (done (when (= nil (get-current-user)) (clj->js {:redirect {:name :login}})))))

(defn guest-gate
  [router]
  (fn [to-state from-state done]
    (done (when (get-current-user) (clj->js {:why "User already logged in"})))))

(def routes [{:name :login 
              :path "/login" 
              :canActivate guest-gate}
             {:name :signup 
              :path "/signup" 
              :canActivate guest-gate}
             {:name :forgot 
              :path "/forgot" 
              :canActivate guest-gate}
             {:name :profile 
              :path "/profile" 
              :canActivate auth-gate}
             {:name :app 
              :path "/" 
              :canActivate auth-gate}])

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