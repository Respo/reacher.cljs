
(ns reacher.example.updater (:require [reacher.example.schema :as schema]))

(defn updater [store op op-data op-id op-time]
  (case op
    :create
      (assoc-in
       store
       [:tasks op-id]
       (merge schema/task {:id op-id, :text op-data, :time op-time}))
    :remove (update store :tasks (fn [tasks] (dissoc tasks op-data)))
    :update (assoc-in store [:tasks (:id op-data) :text] (:text op-data))
    :hydrate-storage op-data
    store))
