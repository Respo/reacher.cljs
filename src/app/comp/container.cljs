
(ns app.comp.container
  (:require [hsl.core :refer [hsl]]
            [app.config :refer [dev?]]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [app.reacher :refer [create-comp div input span]]))

(def comp-draft-area
  (create-comp
   "draft 0"
   (fn [[p1 p2] state mutate!]
     (div
      {}
      (input {:value state, :onChange (fn [event] (mutate! (.. event -target -value)))})
      (span {} state)
      (span {} (str p1))
      (span {} (str p2))))))

(def comp-input-area
  (create-comp
   "initial thingg"
   (fn [_ state mutate!]
     (div
      {}
      (input {:value state, :onChange (fn [event] (mutate! (.. event -target -value)))})
      (str state)))))

(defn comp-container [store]
  (div
   {:style {:color (hsl 200 90 60)}}
   (div {} "A todolist")
   (div {} "DEMO")
   (React/createElement comp-input-area)
   (comp-draft-area :value 1)))
