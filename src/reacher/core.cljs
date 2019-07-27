
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

(def dispatch-context (React/createContext "dispatch"))

(defn react-create-element [el props & children]
  (apply (partial React/createElement el props) children))

(defn use-dispatch [] (React/useContext dispatch-context))

(defn use-rex-data [] )
