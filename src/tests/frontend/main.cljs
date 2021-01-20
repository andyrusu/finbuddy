(ns frontend.main
  (:require [cljs.test :refer-macros [run-tests]]
            [cljs-test-display.core]
            [frontend.validator-test]))

(enable-console-print!)

(defn start
  []
  (run-tests (cljs-test-display.core/init! "app")
             'frontend.validator-test
             'frontend.rules-test))

(defn stop
  [done]
  (js/console.log "done f:" done)
  (done)) 

(defn ^:export init
  []
  (start))

(init)