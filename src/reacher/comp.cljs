
(ns reacher.comp
  (:require [reacher.core :refer [create-comp div adorn]] [hsl.core :refer [hsl]]))

(def comp-space
  (create-comp
   {:name :comp-space}
   (fn [[w h] state d! m!]
     (if (some? w)
       (div {:style (adorn {:display :inline-block, :width w})})
       (div {:style {:height h}})))))

(def =< comp-space)

(def comp-inspect
  (create-comp
   {:name :reacher-inspect}
   (fn [[data title] _ _]
     (div
      {:style (adorn
               {:position :fixed,
                :left 8,
                :bottom 8,
                :background-color (hsl 0 0 0 0.4),
                :color "white",
                :padding "0 8px",
                :line-height "20px",
                :border-radius "6px",
                :cursor :pointer}),
       :onClick (fn [] (println title "value:" (pr-str data)))}
      (or title (pr-str data))))))
