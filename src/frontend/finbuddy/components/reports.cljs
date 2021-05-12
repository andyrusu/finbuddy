(ns finbuddy.components.reports)

(defn summary
  []
  [:div.box
   [:h2.subtitle "This month's report"]
   [:div.columns
    [:div.column
     [:div.notification.has-text-centered.is-success
      [:h2.subtitle "Income:"]
      [:h1.title "2500"]]]
    [:div.column
     [:div.notification.has-text-centered.is-warning
      [:h2.subtitle "Expenses:"]
      [:h1.title "-2600"]]]
    [:div.column
     [:div.notification.has-text-centered.is-danger
      [:h2.subtitle "Savings:"]
      [:h1.title "-100"]]]]])