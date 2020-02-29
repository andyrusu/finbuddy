(ns finbuddy.main
  (:require [[reagent.core :as r]
             [finbuddy.components as cmp]]))

(defn stop []
  (js/console.log "Stopping..."))

(defn start []
  (js/console.log "Starting...")
  (r/render [cmp/app]
            (.getElementById js/document "app")))

(defn ^:export init []
  (start))