
(ns app.reacher (:require ["react" :as React]))

(defn create-comp [state renderer]
  (let [Child (fn [props context updater]
                (this-as
                 this
                 (.call React/Component this props context updater)
                 (set! (.-state this) (js-obj "$0" state))
                 this))]
    (set! (.-prototype Child) (.create js/Object (.-prototype React/Component)))
    (set! (.. Child -prototype -constructor) React/Component)
    (set!
     (.. Child -prototype -shouldComponentUpdate)
     (fn [prevProps prevState]
       (this-as
        this
        (or (not= (aget prevProps "$0") (aget (.-props this) "$0"))
            (not= (aget prevState "$0") (aget (.-state this) "$0"))))))
    (set!
     (.. Child -prototype -render)
     (fn []
       (this-as
        this
        (renderer
         (aget (.-props this) "$0")
         (aget (^js .-state this) "$0")
         (fn [result] (.setState this (js-obj "$0" result)))))))
    (fn [& args] (React/createElement Child (js-obj "$0" args)))))

(defn div [props & children]
  (let [f (partial React/createElement "div" (clj->js props))] (apply f children)))

(defn input [props] (React/createElement "input" (clj->js props)))

(defn span [props & children]
  (let [f (partial React/createElement "span" (clj->js props))] (apply f children)))
