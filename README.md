
Reacher React (in prototype)
----

> React toolkits in ClojureScript based on [React component in pure cljs using ES6 class inheritance](https://gist.github.com/pesterhazy/39c84224972890665b6bec3addafdf5a)

Demo http://repo.respo-mvc.org/reacher/

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/reacher.svg)](https://clojars.org/respo/reacher)

```edn
[respo/reacher "0.1.1"]
```

Example:

```clojure
(def comp-space
  (create-comp
   {:name :comp-space}
   (fn [[w h] state mutate!]
     (if (some? w)
       (div {:style (adorn {:display :inline-block, :width w})})
       (div {:style {:height h}})))))

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
      (comp-space 8 nil)
      (button
       {:style (adorn ui/button),
        :onClick (fn []
          (when (not (string/blank? state)) (dispatch! :create state) (mutate! "")))}
       "Add")))))
```

Public APIs:

```clojure
reacher.core/create-comp
reacher.core/adorn
reacher.core/get-value

reacher.core/get-state
reacher.core/set-state!
reacher.core/dispatch!
reacher.core/register-dispatcher!

reacher.core/div
reacher.core/span ; and more
reacher.core/tag*
```

Supported options:

* `:state`
* `:key-fn`
* `:name`
* `:mount`
* `:unmount`
* `:update`

### Workflow

Based on [calcit-workflow](https://github.com/mvc-works/calcit-workflow).

### License

MIT
