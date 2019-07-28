
(ns reacher.comp
  (:require [reacher.core :refer [div]]
            [hsl.core :refer [hsl]]
            [applied-science.js-interop :as j]))

(defn comp-space [props]
  (let [w (j/get props :w), h (j/get props :h)]
    (if (some? w)
      (div {:style {:display :inline-block, :width w}})
      (div {:style {:height h}}))))

(def =< comp-space)

(defn comp-inspect [props]
  (let [data (j/get props :data), title (j/get props :title)]
    (div
     {:style {:position :fixed,
              :left 8,
              :bottom 8,
              :background-color (hsl 0 0 0 0.4),
              :color "white",
              :padding "0 8px",
              :line-height "20px",
              :border-radius "6px",
              :cursor :pointer},
      :on-click (fn [] (println title "value:" (pr-str data)))}
     (or title (pr-str data)))))
