
(ns app.reacher
  (:require ["react" :as React] ["react-dom" :as DOM])
  (:require-macros [app.reacher :refer [div]]))

(defn grab-dispatcher! [] (.-dispatcherFunction React))

(defn create-comp [state renderer]
  (let [Child (fn [props context updater]
                (this-as
                 this
                 (.call React/Component this props context updater)
                 (set! (.-state this) (js-obj "$0" state))
                 this))
        dispatch! (fn [op op-data] ((grab-dispatcher!) op op-data))]
    (set! (.-prototype Child) (.create js/Object (.-prototype React/Component)))
    (set! (.. Child -prototype -constructor) React/Component)
    (set!
     (.. ^js Child -prototype -shouldComponentUpdate)
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
         (aget (.-state this) "$0")
         dispatch!
         (fn [result] (.setState this (js-obj "$0" result)))))))
    (fn [& args] (React/createElement Child (js-obj "$0" args)))))

(defn register-dispatcher! [f] (set! (.-dispatcherFunction React) f))
