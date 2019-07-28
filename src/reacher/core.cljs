
(ns reacher.core
  (:require ["react" :as React]
            ["react-dom" :as DOM]
            [clojure.string :as string]
            [medley.core :as medley])
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

(def dispatch-context (React/createContext "dispatch"))

(defn map-upper-case [m]
  (->> m (medley/map-keys (fn [k] (keyword (dashed->camel (name k)))))))

(defn react-create-element [el props & children]
  (apply (partial React/createElement el props) children))

(defn transform-props [props]
  (let [result (-> (map-upper-case props) (update :style map-upper-case))]
    (cljs.core/clj->js result)))

(defn use-dispatch [] (React/useContext dispatch-context))

(defn use-rex-data [] )

(defn use-states [s0]
  (let [[state set-state!] (React/useState s0), update-state! (fn [f] (set-state! (f state)))]
    [state update-state!]))
