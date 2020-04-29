(ns finbuddy.components.layouts)

(defn simple
  [page]
  [:section.hero.is-primary.is-fullheight
   [:div.hero-body
    [:div.container
     [:div.columns.is-centered
      [:div.column.is-5-tablet.is-5-desktop.is-5-widescreen
       page]]]]])