(ns org.graclj.tools.compiler.clojure
  (:require [clojure.java.io :as io]
            [clojure.tools.namespace.find :refer [find-namespaces]])
  (:gen-class))

(defn -main [source-path compile-path & args]
  (binding [*compile-path* compile-path]
    (let [source-dir (io/file source-path)
          namespaces (find-namespaces [source-dir])]
      (doseq [namespace namespaces]
        (compile namespace)))))
