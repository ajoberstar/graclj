 (ns org.graclj.tools.test.easy-test
   (:require [clojure.test :refer :all]))

 (deftest addition-works
   (is (= (+ 1 2) 3)))

 (deftest subtraction-fails
   (is (= (- 2 1) 2)))
