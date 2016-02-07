(ns org.graclj.samples.library-test2
  (:require [org.graclj.samples.library :refer :all]
            [clojure.test :refer [deftest is]]))

(deftest palindrome-works
  (is (= "carrac" (palindrome "car"))))
