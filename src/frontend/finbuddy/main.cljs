(ns finbuddy.main
  (:require 
   [firebase :as fb]
   [reagent.core :as r]
   [finbuddy.components.app :as appc]
   [finbuddy.components.auth :as authc]
   [finbuddy.auth :as auth]
   [finbuddy.db :as db]))

(defonce fb-app (fb/initializeApp #js {:apiKey "AIzaSyBRVEXRmxn8ubyLjPTK_2f3nAXnT-lVww4"
                                       :authDomain "finbuddy-3f573.firebaseapp.com"
                                       :databaseURL "https://finbuddy-3f573.firebaseio.com"
                                       :projectId "finbuddy-3f573"
                                       :storageBucket "finbuddy-3f573.appspot.com"
                                       :messagingSenderId "307661346910"
                                       :appId "1:307661346910:web:77a489fab2642b0509bccf"
                                       :measurementId "G-CTGVJ5X1SB"}))

(defn stop []
  (js/console.log "Stopping..."))

(defn app
  [content]
  (case (:show-page @content)
    :app [:> appc/main]
    :login [authc/login]
    :signup [authc/signup]
    :forgot [authc/forgot]))

(defn start []
  (js/console.log "Starting...")
  (fb/analytics)
  (auth/init-auth)
  ;(js/console.log @db/content)
  ;(auth/logout)
  (js/console.log (auth/get-current-user))
  (r/render [app db/content]
            (.getElementById js/document "app")))
 
(defn ^:export init []
  (start))