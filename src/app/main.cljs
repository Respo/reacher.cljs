
(ns app.main
  (:require [app.updater :refer [updater]]
            [app.schema :as schema]
            [cljs.reader :refer [read-string]]
            [app.config :as config]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [app.comp.container :refer [comp-container]]
            [app.reacher :refer [register-dispatcher!]]))

(def *store (atom {}))

(defn dispatch! [op op-data] (println "Dispatch:" op op-data))

(def mount-target (.querySelector js/document ".app"))

(defn persist-storage! []
  (.setItem js/localStorage (:storage config/site) (pr-str @*store)))

(defn render-app! [] (ReactDOM/render (React/createElement comp-container) mount-target))

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
