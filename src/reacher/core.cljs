
(ns reacher.core
  (:require ["react" :as React]
            ["react-dom" :as DOM]
            [clojure.string :as string]
            [medley.core :as medley]
            [applied-science.js-interop :as j])
  (:require-macros [reacher.core :refer [div]]))

(defn dashed->camel
  ([x] (dashed->camel "" x false))
  ([acc piece promoted?]
   (if (= piece "")
     acc
     (let [cursor (get piece 0), piece-followed (subs piece 1)]
       (if (= cursor "-")
         (recur acc piece-followed true)
         (recur
          (str acc (if promoted? (string/upper-case cursor) cursor))
          piece-followed
          false))))))

(def dispatch-context (React/createContext "dispatch"))

(defn map-upper-case [m]
  (->> m (medley/map-keys (fn [k] (keyword (dashed->camel (name k)))))))

(def react-create-element React/createElement)

(defn transform-props [props]
  (let [result (-> (map-upper-case props) (update :style map-upper-case))]
    (cljs.core/clj->js result)))

(defn use-effect! [params f] (React/useEffect f (apply array params)))

(defn use-atom [x0]
  (let [ref-first-run (React/useRef true)
        ref-atom (React/useRef nil)
        [state-x set-x!] (React/useState x0)]
    (if (j/get ref-first-run :current)
      (let [*x (atom x0)]
        (do
         (j/assoc! ref-first-run :current false)
         (j/assoc! ref-atom :current *x)
         (add-watch *x :changes (fn [] (set-x! @*x))))))
    (use-effect! [] (fn [] (fn [] (remove-watch (j/get ref-atom :current) :changes))))
    (j/get ref-atom :current)))

(defn use-callback [params f] (React/useCallback f (apply array params)))

(defn use-dispatch [] (React/useContext dispatch-context))

(defn use-memo [params f] (React/useMemo f (apply array params)))

(defn use-states [s0]
  (let [[state set-state!] (React/useState s0), update-state! (fn [f] (set-state! (f state)))]
    [state update-state!]))
