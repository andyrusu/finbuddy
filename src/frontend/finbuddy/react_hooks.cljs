(ns finbuddy.react-hooks 
  (:require-macros [finbuddy.react-hooks])
  (:require [goog.dom :as gd]))

(defn title
  [text]
  #(gd/setTextContent
    (gd/getElementByTagNameAndClass "title")
    (.-children text)))