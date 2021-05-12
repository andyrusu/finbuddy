(ns finbuddy.react-hooks
  (:require [reagent.core :as r]))

(defmacro use-effect
  [effect body]
  `(react/useEffect ~effect)
  ~@body)