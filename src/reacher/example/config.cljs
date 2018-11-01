
(ns reacher.example.config (:require [reacher.example.util :refer [get-env!]]))

(def bundle-builds #{"release" "local-bundle"})

(def dev?
  (if (exists? js/window)
    (do ^boolean js/goog.DEBUG)
    (not (contains? bundle-builds (get-env! "mode")))))

(def site
  {:storage "reacher",
   :dev-ui "http://localhost:8100/main.css",
   :release-ui "http://cdn.tiye.me/favored-fonts/main.css",
   :cdn-url "http://cdn.tiye.me/reacher/",
   :cdn-folder "tiye.me:cdn/reacher",
   :title "Reacher",
   :icon "http://cdn.tiye.me/logo/respo.png",
   :upload-folder "tiye.me:repo/Respo/reacher/"})
