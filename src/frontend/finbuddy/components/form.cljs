(ns finbuddy.components.form
  (:require [finbuddy.db :as db]))

(defn input-field
  [{:keys [error label input-options]}]
  [:div.field
   [:label.label {:for (:name input-options)} label]
   [:div.control.has-icons-left.has-icons-right
    [:input input-options]
    [:span.icon.is-small.is-left [:i.fa.fa-envelope]]
    (if error
      [:span.icon.is-small.is-right [:i.fa.fa-exclamation-triangle]]
      (when (and (:value input-options) (= false error)) [:span.icon.is-small.is-right [:i.fa.fa-check]]))
    (when error [:p.help.is-danger error])]])

(defn check-field
  [{:keys [label key name]}]
  [:div.field
   [:label.checkbox
    {:for name}
    [:input {:type "checkbox"
             :name name
             :checked (get-in @db/content [:form key :value] false)
             :on-change #(swap!
                          db/content
                          assoc-in
                          [:form key :value]
                          (not (boolean (get-in @db/content [:form key :value]))))}]
    label]])