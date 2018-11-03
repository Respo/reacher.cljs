
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

(defn unit-get [x] (aget x "$0"))

(defn get-props [this] (unit-get (.-props this)))

(defn get-state [this] (unit-get (.-state this)))

(defn unit-obj [data] (js-obj "$0" data))

(defn set-state! [this data] (.setState this (unit-obj data)))

(defn create-comp [state renderer]
  (let [Child (fn [props context updater]
                (this-as
                 this
                 (.call React/Component this props context updater)
                 (set! (.-state this) (unit-obj state))
                 this))]
    (set! (.-prototype Child) (.create js/Object (.-prototype React/Component)))
    (set! (.. Child -prototype -constructor) React/Component)
    (set!
     (.. ^js Child -prototype -shouldComponentUpdate)
     (fn [prevProps prevState]
       (this-as
        this
        (or (not= (unit-get prevProps) (get-props this))
            (not= (unit-get prevState) (get-state this))))))
    (set!
     (.. Child -prototype -render)
     (fn []
       (this-as
        this
        (renderer (get-props this) (get-state this) (fn [result] (set-state! this result))))))
    (fn [& args] (React/createElement Child (unit-obj args)))))

(defn dispatch! [op op-data] ((.-dispatcherFunction React) op op-data))

(defn react-create-element [el props & children]
  (apply (partial React/createElement el props) children ))

(defn register-dispatcher! [f] (set! (.-dispatcherFunction React) f))
