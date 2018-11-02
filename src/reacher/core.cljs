
(ns reacher.core
  (:require ["react" :as React] ["react-dom" :as DOM] [clojure.string :as string])
  (:require-macros [reacher.core :refer [div]]))

(defn dashed->camel
  ([x] (dashed->camel "" x false))
  ([acc piece promoted?]
   (if (= piece "")
     acc
     (let [cursor (get piece 0), piece-followed (subs piece 1)]
       (if (= cursor "-")
         (recur acc piece-followed true)
         (recur
          (str acc (if promoted? (string/upper-case cursor) cursor))
          piece-followed
          false))))))

(defn adorn [& styles]
  (->> (apply merge styles) (map (fn [[k v]] [(dashed->camel (name k)) v])) (into {})))

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
