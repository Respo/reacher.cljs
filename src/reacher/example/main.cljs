
(ns reacher.example.main
  (:require [reacher.example.updater :refer [updater]]
            [reacher.example.schema :as schema]
            [cljs.reader :refer [read-string]]
            [reacher.example.config :as config]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [reacher.example.comp.container :refer [comp-container]]
            [reacher.core :refer [register-dispatcher!]]))

(def *store (atom schema/store))

(defn dispatch! [op op-data] (println "Dispatch:" op op-data))

(def mount-target (.querySelector js/document ".app"))

(defn persist-storage! []
  (.setItem js/localStorage (:storage config/site) (pr-str @*store)))

(defn render-app! [] (ReactDOM/render (comp-container @*store) mount-target))

(def ssr? (some? (js/document.querySelector "meta.respo-ssr")))

(defn main! []
  (if ssr? (do nil))
  (register-dispatcher! #(dispatch! %1 %2))
  (render-app!)
  (add-watch *store :changes (fn [] (render-app!)))
  (.addEventListener js/window "beforeunload" persist-storage!)
  (js/setInterval persist-storage! (* 1000 60))
  (let [raw (.getItem js/localStorage (:storage config/site))]
    (when (some? raw) (dispatch! :hydrate-storage (read-string raw))))
  (println "App started."))

(defn reload! [] (println "Code updated.") (render-app!))
