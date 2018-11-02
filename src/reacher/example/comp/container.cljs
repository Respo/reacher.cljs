
(ns reacher.example.comp.container
  (:require [hsl.core :refer [hsl]]
            [reacher.example.config :refer [dev?]]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [reacher.core :refer [create-comp div input span button]]
            [respo-ui.core :as ui]
            [reacher.core :refer [adorn]]
            [reacher.built-in :refer [comp-space]]))

(def comp-draft-area
  (create-comp
   "draft 0"
   (fn [[p1 p2] state dispatch! mutate!]
     (div
      {}
      (input
       {:value state,
        :style (adorn ui/input),
        :onChange (fn [event] (mutate! (.. event -target -value)))})
      (comp-space 8 nil)
      (span {} state)
      (span {} (str p1))
      (span {} (str p2))))))

(def comp-input-area
  (create-comp
   "initial thingg"
   (fn [_ state dispatch! mutate!]
     (div
      {}
      (input
       {:value state,
        :style (adorn ui/input),
        :onChange (fn [event] (mutate! (.. event -target -value)))})
      (comp-space 8 nil)
      (str state)
      (button {:onClick (fn [event] (dispatch! :action "data"))} "Click")))))

(defn comp-container [store]
  (div
   {:style (adorn ui/global {:color (hsl 200 90 60)})}
   (div {} "A todolist")
   (div {} "DEMO")
   (comp-input-area)
   (comp-draft-area :value 1)))
