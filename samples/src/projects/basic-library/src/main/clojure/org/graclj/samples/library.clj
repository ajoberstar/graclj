(ns org.graclj.samples.library
  (:require [clojure.string :as str]))

(defn palindrome [string]
  (str string (str/reverse string)))
