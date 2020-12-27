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
  (io/make-parents css-output)
  (io/copy (io/file "resources/public/css/main.css")
           (io/file css-output)))

(println "Copy images")
(let [source-dir "resources/public/img/"]
  (doseq [f (file-seq (io/file source-dir))]
    (let [file-name (.getName f)
          img-output (str "docs/img/" file-name)]
      (when (.isFile f)
        (io/make-parents img-output)
        (io/copy (io/file (str source-dir file-name))
                 (io/file img-output))))))

(println "Done!")
