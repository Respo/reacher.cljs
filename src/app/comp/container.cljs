
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
  (let [Child (fn [props context updater]
                (this-as
                 this
                 (.call React/Component this props context updater)
                 (set! (.-state this) (clj->js {:draft "initial thing"}))
                 this))]
    (set! (.-prototype Child) (.create js/Object (.-prototype React/Component)))
    (set! (.. Child -prototype -constructor) React/Component)
    (set!
     (.. Child -prototype -render)
     (fn []
       (this-as
        this
        (div
         {}
         (input
          {:value (.-draft (.-state ^js this)),
           :onChange (fn [event]
             (.setState this (clj->js {:draft (.. event -target -value)})))})
         (.-draft (.-state ^js this))))))
    Child))

(defn comp-container [store]
  (div
   {:style {:color (hsl 200 90 60)}}
   (div {} "A todolist")
   (div {} "DEMO")
   (React/createElement comp-input-area)
   (comp-draft-area :value 1)))
