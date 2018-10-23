
(ns app.main
  (:require [app.updater :refer [updater]]
            [app.schema :as schema]
            [cljs.reader :refer [read-string]]
            [app.config :as config]))

(defn dispatch! [op op-data] (comment println "Dispatch:" op))

(def mount-target (.querySelector js/document ".app"))

(defn persist-storage! []
  (.setItem js/localStorage (:storage config/site) (pr-str (:store @*reel))))

(defn render-app! [renderer]
  (comment renderer mount-target (comp-container @*reel) #(dispatch! %1 %2)))

(def ssr? (some? (js/document.querySelector "meta.respo-ssr")))

(defn main! []
  (if ssr? (render-app! realize-ssr!))
  (render-app! render!)
  (add-watch *reel :changes (fn [] (render-app! render!)))
  (.addEventListener js/window "beforeunload" persist-storage!)
  (js/setInterval persist-storage! (* 1000 60))
  (let [raw (.getItem js/localStorage (:storage config/site))]
    (when (some? raw) (dispatch! :hydrate-storage (read-string raw))))
  (println "App started."))

(defn reload! [] (println "Code updated."))

(set! (.-onload js/window) main!)
