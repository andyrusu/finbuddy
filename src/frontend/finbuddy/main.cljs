(ns finbuddy.main
  (:require 
   [firebase :as fb]
   [reagent.core :as r]
   [finbuddy.components.app :as appc]
   [finbuddy.components.auth :as authc]
   [finbuddy.router :as router :refer [router top-name-from-route]]
   [react-router5 :as rr5]
   [finbuddy.users :as users]
   [finbuddy.db :as db]))

(defonce fb-app (fb/initializeApp #js {:apiKey "AIzaSyBRVEXRmxn8ubyLjPTK_2f3nAXnT-lVww4"
                                       :authDomain "finbuddy-3f573.firebaseapp.com"
                                       :databaseURL "https://finbuddy-3f573.firebaseio.com"
                                       :projectId "finbuddy-3f573"
                                       :storageBucket "finbuddy-3f573.appspot.com"
                                       :messagingSenderId "307661346910"
                                       :appId "1:307661346910:web:77a489fab2642b0509bccf"
                                       :measurementId "G-CTGVJ5X1SB"}))

(add-watch db/content :log (fn [_key _atom old-state new-state]
                             (js/console.log 
                              (clj->js old-state) 
                              (clj->js new-state))))

(defn stop []
  (js/console.log "Stopping..."))

(defn app
  []
  (let [route (.-route (rr5/useRouteNode ""))]
    (r/as-element 
     (case (top-name-from-route route)
       :app [:> appc/main]
       :login [authc/login]
       :signup [authc/signup]
       :forgot [authc/forgot]))))

(defn start []
  (js/console.log "Starting app...")
  (fb/analytics)
  (users/init-auth)
  (js/console.log (users/get-current-user))
  (.start router #(r/render [:> rr5/RouterProvider
                             {:router router}
                             [:> app]]
                            (.getElementById js/document "app"))))

(defn ^:export init []
  (start))