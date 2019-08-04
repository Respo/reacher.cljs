
(ns reacher.example.comp.container
  (:require [hsl.core :refer [hsl]]
            [clojure.string :as string]
            [reacher.example.config :refer [dev?]]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [reacher.core :refer [div input span button a use-effect! defcomp]]
            [respo-ui.core :as ui]
            [reacher.comp :refer [=< comp-inspect]]
            [applied-science.js-interop :as j]
            [reacher.core :refer [use-dispatch use-states use-atom]]))

(defcomp
 comp-creator
 ()
 (let [[draft set-draft!] (React/useState "")
       [states update-states!] (use-states {:draft ""})
       *draft (use-atom "")
       dispatch! (use-dispatch)]
   (use-effect! [] #(.. js/document (querySelector ".box") (focus)))
   (div
    {:style ui/row-middle}
    (input
     {:class-name "box",
      :style ui/input,
      :placeholder "task content",
      :value @*draft,
      :on-change (fn [event] (reset! *draft (.. event -target -value)))})
    (=< (j/obj :w 8))
    (button
     {:style ui/button,
      :on-click (fn []
        (when (not (string/blank? @*draft)) (dispatch! :create @*draft) (reset! *draft "")))}
     "Add"))))

(defcomp
 comp-task
 (props)
 (let [task (j/get props :task), dispatch! (use-dispatch)]
   (div
    {:key (:id task), :style (merge ui/row-parted {:padding "0 8px", :width 320})}
    (str (:text task))
    (div
     {:style (merge
              ui/row-middle
              {:color (hsl 200 40 50), :font-size 12, :font-family ui/font-fancy})}
     (a
      {:style (merge {:cursor :pointer}),
       :on-click (fn []
         (let [content (js/prompt "Change content" (:text task))]
           (when (some? content) (dispatch! :update {:id (:id task), :text content}))))}
      "Edit")
     (=< (j/obj :w 8))
     (a
      {:style (merge {:cursor :pointer}),
       :on-click (fn []
         (let [sure? (js/confirm "Remove it?")] (when sure? (dispatch! :remove (:id task)))))}
      "Remove")))))

(defcomp
 comp-tasks-list
 (props)
 (let [tasks (j/get props :tasks)]
   (div
    {}
    (->> tasks
         (sort-by (fn [[k task]] (unchecked-negate (:time task))))
         (map (fn [[k task]] (comp-task (j/obj :task task :key (:id task)))))))))

(defn comp-container [props]
  (let [store (j/get props :store)]
    (div
     {:style ui/global}
     (comp-creator)
     (comp-tasks-list (j/obj :tasks (:tasks store)))
     (comp-inspect (j/obj :store store :text store)))))
