
(ns reacher.example.comp.container
  (:require [hsl.core :refer [hsl]]
            [clojure.string :as string]
            [reacher.example.config :refer [dev?]]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [reacher.core :refer [div input span button a adorn]]
            [respo-ui.core :as ui]
            [reacher.comp :refer [=< comp-inspect]]
            [applied-science.js-interop :as j]
            [reacher.core :refer [use-dispatch]]))

(defn comp-creator []
  (let [[draft set-draft!] (React/useState ""), dispatch! (use-dispatch)]
    (React/useEffect (fn [] (.. js/document (querySelector ".box") (focus))) (array))
    (div
     {:style (adorn ui/row-middle)}
     (input
      {:className "box",
       :style (adorn ui/input),
       :placeholder "task content",
       :value draft,
       :onChange (fn [event] (set-draft! (.. event -target -value)))})
     (=< (j/obj :w 8))
     (button
      {:style (adorn ui/button),
       :onClick (fn []
         (when (not (string/blank? draft)) (dispatch! :create draft) (set-draft! "")))}
      "Add"))))

(defn comp-task [props]
  (let [task (j/get props :task), dispatch! (use-dispatch)]
    (div
     {:key (:id task), :style (adorn ui/row-parted {:padding "0 8px", :width 320})}
     (str (:text task))
     (div
      {:style (adorn
               ui/row-middle
               {:color (hsl 200 40 50), :font-size 12, :font-family ui/font-fancy})}
      (a
       {:style (adorn {:cursor :pointer}),
        :onClick (fn []
          (let [content (js/prompt "Change content" (:text task))]
            (when (some? content) (dispatch! :update {:id (:id task), :text content}))))}
       "Edit")
      (=< (j/obj :w 8))
      (a
       {:style (adorn {:cursor :pointer}),
        :onClick (fn []
          (let [sure? (js/confirm "Remove it?")] (when sure? (dispatch! :remove (:id task)))))}
       "Remove")))))

(defn comp-tasks-list [props]
  (let [tasks (j/get props :tasks)]
    (div
     {}
     (apply
      array
      (->> tasks
           (sort-by (fn [[k task]] (unchecked-negate (:time task))))
           (map
            (fn [[k task]]
              (React/createElement comp-task (j/obj :task task :key (:id task))))))))))

(defn comp-container [props]
  (let [store (j/get props :store)]
    (div
     {:style (adorn ui/global {})}
     (React/createElement comp-creator)
     (React/createElement comp-tasks-list (j/obj :tasks (:tasks store)))
     (React/createElement comp-inspect (j/obj :store store :text store)))))
