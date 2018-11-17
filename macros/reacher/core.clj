
(ns reacher.core)

(defn get-tag-name [tag]
  (cond
    (string? tag) tag
    (keyword? tag) (name tag)
    (symbol? tag) (name tag)
    :else (str tag)))

(defmacro tag* [tag props & children]
  (let [tag-name (get-tag-name tag)]
    `(react-create-element ~(get-tag-name tag-name) (cljs.core/clj->js ~props) ~@children)))

(defmacro meta' [props & children]
  `(react-create-element "meta" (cljs.core/clj->js ~props) ~@children))
(defmacro filter' [props & children]
  `(react-create-element "filter" (cljs.core/clj->js ~props) ~@children))

(def normal-elements '[a body button canvas code div footer
                       h1 h2 head header html hr i img li
                       option p pre section select span style title ul
                       ; self-closing
                       br input link script textarea
                       ; svg
                       svg animate circle defs ellipse font font font-face g
                       image line marker mask path pattern polygon polyline rect stop
                       text tspan view
                       clipPath feBlend feOffset])

(defn create-normal-element [tag props children]
  `(react-create-element ~(get-tag-name tag) (cljs.core/clj->js ~props) ~@children))

(defn normal-tag [el]
  `(defmacro ~el [~'props ~'& ~'children]
    (create-normal-element '~el ~'props ~'children)))

(defmacro gen-normal-elements []
  `(do ~@(clojure.core/map normal-tag normal-elements)))

(gen-normal-elements)
