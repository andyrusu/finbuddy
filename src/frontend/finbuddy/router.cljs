(ns finbuddy.router
  (:require [clojure.walk :refer [stringify-keys]]
            [router5 :as r5]
            [router5-plugin-logger :as r5-logger]))

(defn create
  [routes]
  (let [router (r5/createRouter (clj->js routes) #js {:defaultRoute "login"})]
    (.usePlugin router r5-logger)
    router))

(defn top-route-name-from-route
  [route]
  (-> route (.-name) (.split '.') (get 0) (keyword)))