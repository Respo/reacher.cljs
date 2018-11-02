
(ns reacher.built-in (:require [reacher.core :refer [create-comp div adorn]]))

(def comp-space
  (create-comp
   nil
   (fn [[w h] state d! m!]
     (if (some? w)
       (div {:style (adorn {:display :inline-block, :width w})})
       (div {:style {:height h}})))))
