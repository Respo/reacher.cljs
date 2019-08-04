
(ns reacher.core)

(defn get-tag-name [tag]
  (cond
    (string? tag) tag
    (keyword? tag) (name tag)
    (symbol? tag) (name tag)
    :else (str tag)))

(defmacro tag* [tag props & children]
  (let [tag-name (get-tag-name tag)]
    `(react-create-element ~(get-tag-name tag-name) (reacher.core/transform-props ~props) ~@children)))

(defmacro meta' [props & children]
  `(react-create-element "meta" (reacher.core/transform-props ~props) ~@children))
(defmacro filter' [props & children]
  `(react-create-element "filter" (reacher.core/transform-props ~props) ~@children))

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
  `(react-create-element ~(get-tag-name tag) (reacher.core/transform-props ~props) ~@children))

(defn normal-tag [el]
  `(defmacro ~el [~'props ~'& ~'children]
    (create-normal-element '~el ~'props ~'children)))

(defmacro gen-normal-elements []
  `(do ~@(clojure.core/map normal-tag normal-elements)))

(gen-normal-elements)

(defmacro defcomp [def-name params & body]
  (let [func-name (symbol (str "$comp_" (name def-name)))]
  `(do
    (defn- ~func-name [~@params] ~@body)
    (defn ~def-name [& args#]
      (apply react-create-element ~func-name args#)))))
