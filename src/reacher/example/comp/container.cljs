
(ns reacher.example.comp.container
  (:require [hsl.core :refer [hsl]]
            [clojure.string :as string]
            [reacher.example.config :refer [dev?]]
            ["react" :as React]
            ["react-dom" :as ReactDOM]
            [reacher.core
             :refer
             [create-comp div input span button a adorn dispatch! get-state get-value]]
            [respo-ui.core :as ui]
            [reacher.comp :refer [=< comp-inspect]]))

(def comp-creator
  (create-comp
   {:state "",
    :name :creator,
    :mount (fn [] (.. js/document (querySelector ".box") (focus)))}
   (fn [[] state mutate!]
     (div
      {:style (adorn ui/row-middle)}
      (input
       {:className "box",
        :style (adorn ui/input),
        :placeholder "task content",
        :value state,
        :onChange (fn [event] (mutate! (get-value event)))})
      (=< 8 nil)
      (button
       {:style (adorn ui/button),
        :onClick (fn []
          (when (not (string/blank? state)) (dispatch! :create state) (mutate! "")))}
       "Add")))))

(def comp-task
  (create-comp
   {:name :task, :key-fn (fn [task] (:id task))}
   (fn [[task] _ _]
     (div
      {:key (:id task), :style (adorn ui/row-parted {:padding "0 8px", :width 320})}
      (:text task)
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
       (=< 8 nil)
       (a
        {:style (adorn {:cursor :pointer}),
         :onClick (fn []
           (let [sure? (js/confirm "Remove it?")] (when sure? (dispatch! :remove (:id task)))))}
        "Remove"))))))

(def comp-tasks-list
  (create-comp
   {:name :tasks-list}
   (fn [[tasks] _ _]
     (div
      {}
      (apply
       array
       (->> tasks
            (sort-by (fn [[k task]] (unchecked-negate (:time task))))
            (map (fn [[k task]] (comp-task task)))))))))

(def comp-container
  (create-comp
   {:state nil, :name :app-container}
   (fn [[store] state mutate!]
     (div
      {:style (adorn ui/global {})}
      (comp-creator)
      (comp-tasks-list (:tasks store))
      (comp-inspect store "Store")))))
