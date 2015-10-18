(ns org.graclj.tools.clojure
  (:require [clojure.java.io :as io]
            [clojure.tools.namespace.find :refer [find-namespaces]]))

(defn -main [& args]
  (let [[source-path compile-path] args]
    (println "Source: " source-path)
    (println "Compile: " compile-path)
    (binding [*compile-path* compile-path]
      (let [source-dir (io/file source-path)
            namespaces (find-namespaces [source-dir])]
        (doseq [namespace namespaces]
          (println "Compiling " namespace)
          (compile namespace))))))
