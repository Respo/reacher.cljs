
Reacher React (in prototype)
----

WIP...

> React toolkits in ClojureScript based on [React component in pure cljs using ES6 class inheritance](https://gist.github.com/pesterhazy/39c84224972890665b6bec3addafdf5a)

Demo http://repo.respo-mvc.org/reacher/

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/reacher.svg)](https://clojars.org/respo/reacher)

```edn
[respo/reacher "0.1.1"]
```

Example:

```clojure
(defn comp-space [props]
  (let [w (j/get props :w), h (j/get props :h)]
    (if (some? w)
      (div {:style (adorn {:display :inline-block, :width w})})
      (div {:style {:height h}}))))

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
     (=< 8 nil)
     (button
      {:style (adorn ui/button),
       :onClick (fn []
         (when (not (string/blank? draft)) (dispatch! :create draft) (set-draft! "")))}
      "Add"))))
```

Public APIs:

```clojure
reacher.core/adorn

reacher.core/div
reacher.core/span ; and more
reacher.core/tag*
```

### Workflow

Based on [calcit-workflow](https://github.com/mvc-works/calcit-workflow).

### License

MIT
