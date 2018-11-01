
(ns reacher.core)

(defn get-tag-name [tag]
  (cond
    (string? tag) tag
    (keyword? tag) (name tag)
    (symbol? tag) (name tag)
    :else (str tag)))

(defmacro <> [tag props & children]
  (let [tag-name (get-tag-name tag)]
    `(React/createElement ~tag-name (cljs.core/clj->js ~props) ~@children)))

(defmacro <*> [tag props]
  (let [tag-name (get-tag-name tag)]
    `(React/createElement ~tag-name (cljs.core/clj->js ~props))))

(def normal-elements '[a body button canvas code div footer
                       h1 h2 head header html hr i img li
                       option p pre section select span style title
                       ul])

(def self-close-elements '[br input link script textarea])

(def svg-elements '[svg animate circle defs ellipse font font font-face g
                    image line marker mask path pattern polygon polyline rect stop
                    text tspan view])

(defn create-normal-element [tag props children]
  `(React/createElement ~(get-tag-name tag) (cljs.core/clj->js ~props) ~@children))

(defn normal-tag [el]
  `(defmacro ~el [~'props ~'& ~'children]
    (create-normal-element '~el ~'props ~'children)))

(defn create-close-element [tag props]
  `(React/createElement ~(get-tag-name tag) (cljs.core/clj->js ~props)))

(defn self-close-tag [el]
  `(defmacro ~el [~'props]
    (create-close-element '~el ~'props)))

(defmacro gen-normal-elements []
  `(do ~@(clojure.core/map normal-tag normal-elements)))

(defmacro gen-svg-elements []
  `(do ~@(clojure.core/map normal-tag svg-elements)))

(defmacro gen-self-close-elements []
  `(do ~@(clojure.core/map self-close-tag self-close-elements)))

(gen-normal-elements)
(gen-svg-elements)
(gen-self-close-elements)

(defmacro meta' [props & children]
  `(React/createElement "meta" (cljs.core/clj->js ~props) ~@children))
