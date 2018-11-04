
(ns reacher.example.comp.container
  (:require [hsl.core :refer [hsl]]
            [reacher.example.config :refer [dev?]]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [reacher.core
             :refer
             [create-comp div input span button adorn dispatch! get-state get-value]]
            [respo-ui.core :as ui]
            [reacher.built-in :refer [=<]]))

(def comp-creator
  (create-comp
   {:state "", :name :creator}
   (fn [[] state mutate!]
     (div
      {:style (adorn ui/row-middle)}
      (input
       {:style (adorn ui/input),
        :placeholder "demo",
        :value state,
        :onChange (fn [event] (mutate! (get-value event)))})
      (=< 8 nil)
      (button
       {:style (adorn ui/button), :onClick (fn [] (dispatch! :create nil) (mutate! ""))}
       "Add")))))

(def comp-container
  (create-comp
   {:state nil, :name :app-container}
   (fn [[store] state mutate!]
     (div {:style (adorn ui/global {:color (hsl 200 90 60)})} (comp-creator)))))
