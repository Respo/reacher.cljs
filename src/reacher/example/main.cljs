
(ns reacher.example.main
  (:require [reacher.example.updater :refer [updater]]
            [reacher.example.schema :as schema]
            [cljs.reader :refer [read-string]]
            [reacher.example.config :as config]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [reacher.example.comp.container :refer [comp-container]]
            ["shortid" :as shortid]
            [cumulo-util.core :refer [repeat!]]
            [applied-science.js-interop :as j]
            [reacher.core :refer [dispatch-context]]))

(defonce *store (atom schema/store))

(defn dispatch! [op op-data]
  (when config/dev? (println op (pr-str op-data)))
  (let [op-id (.generate shortid), op-time (.now js/Date)]
    (reset! *store (updater @*store op op-data op-id op-time))))

(def mount-target (.querySelector js/document ".app"))

(defn persist-storage! []
  (.setItem js/localStorage (:storage-key config/site) (pr-str @*store)))

(defn render-app! []
  (ReactDOM/render
   (React/createElement
    (j/get dispatch-context :Provider)
    (j/obj :value dispatch!)
    (comp-container (j/obj :store @*store)))
   mount-target))

(def ssr? (some? (js/document.querySelector "meta.respo-ssr")))

(defn main! []
  (if ssr? (do nil))
  (render-app!)
  (add-watch *store :changes (fn [] (render-app!)))
  (.addEventListener js/window "beforeunload" persist-storage!)
  (repeat! 60 persist-storage!)
  (let [raw (.getItem js/localStorage (:storage-key config/site))]
    (when (some? raw) (dispatch! :hydrate-storage (read-string raw))))
  (println "App started."))

(defn reload! [] (println "Code updated.") (render-app!))
