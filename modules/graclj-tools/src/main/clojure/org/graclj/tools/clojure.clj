(ns org.graclj.tools.clojure
  (:require [clojure.tools.namespace.find :as find]))

(defn sample [x]
  (println x))

(defn -main [& args]
  (compile 'sample.yay))
