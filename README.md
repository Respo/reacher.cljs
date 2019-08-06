
Reacher: run React hooks on ClojureScript.
----

Demo http://repo.respo-mvc.org/reacher/

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/reacher.svg)](https://clojars.org/respo/reacher)

```edn
[respo/reacher "0.3.0-a1"]
```

APIs for creating elements and components:

```clojure
reacher.core/div
reacher.core/span ; and more
reacher.core/tag*

reacher.core/defcomp
```

```clojure
[applied-science.js-interop :as j]
[reacher.core :refer [div defcomp tag* span]]

(tag* :header {}
  (span {}))

(defcomp comp-space [props]
  (let [w (j/get props :w), h (j/get props :h)]
    (if (some? w)
      (div {:style {:display :inline-block, :width w}})
      (div {:style {:height h}}))))

(comp-space (j/obj :w 100))
```

Wrapped APIs:

```clojure
reacher.core/use-memo
reacher.core/use-callback
```

```clojure
[reacher.core :refer [div defcomp use-memo use-callback]]

(defcomp com-a []
  (let [child (use-memo [x y] (fn [] (div)))
        handler (use-callback [x y] (fn [] (println "event")))])
    (div {:on-click handler}
      child))
```

Interact with states:

```clojure
reacher.core/use-states
reacher.core/use-atom
```

```clojure
[reacher.core :refer [div defcomp use-atom use-states]]

(defcomp comp-a []
  (let [*s (use-atom 1)])
    (div {:on-click (fn []
                      (swap! *s inc))}))

(defcomp comp-b []
  (let [[s update-s!] (use-states {:count 1}]
    (div {on-click (fn []
                      (update-s! (fn [s1] (update s1 :count inc))))})))
```

Dispatch function:

```clojure
reacher.core/dispatch-context
reacher.core/use-dispatch
```

```clojure
[reacher.core :refer [div defcomp use-dispatch dispatch-context]]

(defn dispatch! [op op-data])

; inject Provider
(ReactDOM/render
 (React/createElement
  (j/get dispatch-context :Provider)
  (j/obj :value dispatch!)
  (comp-container (j/obj :store @*store)))
 mount-target)

(defcomp comp-a []
  (let [dispatch! (use-dispatch)]
    (div {})))
```

Clojure maps are transformed into JavaScript objects dynamically.

### Workflow

Based on [calcit-workflow](https://github.com/mvc-works/calcit-workflow).

### License

MIT
