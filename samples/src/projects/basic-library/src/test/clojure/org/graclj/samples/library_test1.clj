(ns org.graclj.samples.library-test1
  (:require [org.graclj.samples.library :refer :all]
            [clojure.test :refer [deftest is]]))

(deftest palindrome-works
  (is (= "testtset" (palindrome "test"))))

(deftest this-fails
  (is (= "blah" (palindrome "blah"))))
