(ns finbuddy.main
  (:require 
   [firebase :as fb]
   [reagent.core :as r]
   [finbuddy.components.app :as ac]))

(def fb-config #js {
  :apiKey "AIzaSyBRVEXRmxn8ubyLjPTK_2f3nAXnT-lVww4",
  :authDomain "finbuddy-3f573.firebaseapp.com",
  :databaseURL "https://finbuddy-3f573.firebaseio.com",
  :projectId "finbuddy-3f573",
  :storageBucket "finbuddy-3f573.appspot.com",
  :messagingSenderId "307661346910",
  :appId "1:307661346910:web:77a489fab2642b0509bccf",
  :measurementId "G-CTGVJ5X1SB"
})

(defonce fb-app (fb/initializeApp fb-config))
;; (def gg-auth (fb/auth))

(defn stop []
  (js/console.log "Stopping..."))

(defn start []
  (js/console.log "Starting...")
  (r/render [ac/app :app]
            (.getElementById js/document "app")))
 
(defn ^:export init []
  (start))