
(ns reacher.example.comp.container
  (:require [hsl.core :refer [hsl]]
            [reacher.example.config :refer [dev?]]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [reacher.core
             :refer
             [create-comp div input span button adorn dispatch! get-state]]
            [respo-ui.core :as ui]
            [reacher.built-in :refer [comp-space]]))

(def comp-draft-area
  (create-comp
   {:state "draft 0", :name :draft-area}
   (fn [[p1 p2] state mutate!]
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
   {:state "initial thingg",
    :name :demo-area,
    :mount (fn [] (this-as this (println "Mounted with state" (pr-str (get-state this))))),
    :unmount (fn [] (println "Tearing down"))}
   (fn [_ state mutate!]
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
