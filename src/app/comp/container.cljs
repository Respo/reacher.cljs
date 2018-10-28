
(ns app.comp.container
  (:require [hsl.core :refer [hsl]]
            [app.config :refer [dev?]]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [app.reacher :refer [create-comp div input span]]))

(def comp-draft-area
  (create-comp
   "draft 0"
   (fn [state mutate!]
     (div
      {}
      (input {:value state, :onChange (fn [event] (mutate! (.. event -target -value)))})
      (span {} state)))))

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
          {:value (^js .-draft (^js .-state this)),
           :onChange (fn [event]
             (.setState this (clj->js {:draft (.. event -target -value)})))})
         (^js .-draft (^js .-state this))))))
    Child))

(defn comp-container [store]
  (div
   {:style {:color (hsl 200 90 60)}}
   (div {} "A todolist")
   (div {} "DEMO")
   (React/createElement comp-input-area)
   (React/createElement comp-draft-area)))
