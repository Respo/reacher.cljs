
(ns app.reacher (:require ["react" :as React]))

(defn create-comp [] )

(defn div [props & children]
  (let [f (partial React/createElement "div" (clj->js props))] (apply f children)))

(defn input [props] (React/createElement "input" (clj->js props)))
