#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.java.shell :as shell])

(println "Build JS")
(shell/sh "clj" "-m" "figwheel.main" "-bo" "prod")

(println "Copy inde.html")
(let [index-output "docs/index.html"]
  (io/make-parents index-output)
  (io/copy (io/file "resources/public/index.html")
           (io/file index-output)))

(println "Copy main.js")
(let [js-output "docs/js/main.js"]
  (io/make-parents js-output)
  (io/copy (io/file "resources/public/js/prod/main.js")
           (io/file js-output)))

(println "Copy main.css")
(let [css-output "docs/css/main.css"]
  (io/copy (io/file "resources/public/css/main.css")
           (io/file css-output)))

(println "Done!")