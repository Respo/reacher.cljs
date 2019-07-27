
(ns reacher.comp
  (:require [reacher.core :refer [div adorn]]
            [hsl.core :refer [hsl]]
            [applied-science.js-interop :as j]))

(defn comp-space [w h]
  (if (some? w)
    (div {:style (adorn {:display :inline-block, :width w})})
    (div {:style {:height h}})))

(def =< comp-space)

(defn comp-inspect [props]
  (let [data (j/get props :data), title (j/get props :title)]
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
     (or title (pr-str data)))))
