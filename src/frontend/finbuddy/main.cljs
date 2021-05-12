(ns finbuddy.main
  (:require 
   [firebase :as fb]
   [reagent.core :as r]
   [reagent.dom :as rd]
   [react-router5 :as rr5]
   [finbuddy.pages.login :as login]
   [finbuddy.pages.forgot :as forgot]
   [finbuddy.pages.signup :as signup]
   [finbuddy.pages.transactions :as trans]
   [finbuddy.components.app :as appc]
   [finbuddy.router :as router]
   [finbuddy.users :as users]
   [finbuddy.db :as db]))

;; Do we need this?
(defonce fb-app (fb/initializeApp #js {:apiKey "AIzaSyBRVEXRmxn8ubyLjPTK_2f3nAXnT-lVww4"
                                       :authDomain "finbuddy-3f573.firebaseapp.com"
                                       :databaseURL "https://finbuddy-3f573.firebaseio.com"
                                       :projectId "finbuddy-3f573"
                                       :storageBucket "finbuddy-3f573.appspot.com"
                                       :messagingSenderId "307661346910"
                                       :appId "1:307661346910:web:77a489fab2642b0509bccf"
                                       :measurementId "G-CTGVJ5X1SB"}))

(add-watch db/content :log (fn [_key _atom old-state new-state]
                             (js/console.group "State switch:")
                             (js/console.log
                              (clj->js old-state)
                              (clj->js new-state))
                             (js/console.groupEnd)))

(defn app
  []
  (let [route (.-route (rr5/useRouteNode ""))]
    (r/as-element 
     (case (router/top-name-from-route route)
       :app [trans/show]
       :login [:f> login/show]
       :signup [signup/show]
       :forgot [forgot/show]))))

(defn start []
  (js/console.log "Starting app...")
  (fb/analytics)
  (js/console.log (users/get-current-user))
  (router/init-auth-observer)
  (.start router/router #(rd/render
                          [:> rr5/RouterProvider
                           {:router router/router}
                           [:> app]]
                          (.getElementById js/document "app")))) 

(defn ^:export init []
  (start))