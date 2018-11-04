
(ns reacher.example.updater )

(defn updater [store op op-data op-id op-time] (case op :hydrate-storage op-data store))
