
Reacher React (in prototype)
----

WIP...

> React toolkits in ClojureScript based on [React component in pure cljs using ES6 class inheritance](https://gist.github.com/pesterhazy/39c84224972890665b6bec3addafdf5a)

Demo http://repo.respo-mvc.org/reacher/

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/reacher.svg)](https://clojars.org/respo/reacher)

```edn
[respo/reacher "0.3.0-a1"]
```

Example:

```clojure
(defn comp-space [props]
  (let [w (j/get props :w), h (j/get props :h)]
    (if (some? w)
      (div {:style {:display :inline-block, :width w}})
      (div {:style {:height h}}))))

(defn comp-creator []
  (let [[draft set-draft!] (React/useState "")
        [states update-states!] (use-states {:draft ""})
        dispatch! (use-dispatch)]
    (React/useEffect (fn [] (.. js/document (querySelector ".box") (focus))) (array))
    (div
     {:style ui/row-middle}
     (input
      {:class-name "box",
       :style ui/input,
       :placeholder "task content",
       :value (:draft states),
       :on-change (fn [event]
         (update-states! (fn [s] (assoc s :draft (.. event -target -value)))))})
     (=< (j/obj :w 8))
     (button
      {:style ui/button,
       :on-click (fn []
         (when (not (string/blank? (:draft states)))
           (dispatch! :create (:draft states))
           (update-states! (fn [s] (assoc s :draft "")))))}
      "Add"))))
```

Public APIs:

```clojure
reacher.core/div
reacher.core/span ; and more
reacher.core/tag*

reacher.core/defcomp

reacher.core/use-dispatch
reacher.core/use-states
reacher.core/use-atom
reacher.core/use-memo
reacher.core/use-callback
```

### Workflow

Based on [calcit-workflow](https://github.com/mvc-works/calcit-workflow).

### License

MIT
