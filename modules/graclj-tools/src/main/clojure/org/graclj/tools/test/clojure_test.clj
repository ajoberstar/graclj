(ns org.graclj.tools.test.clojure-test
  (:require [clojure.test :as test]
            [clojure.stacktrace :as stack])
  (:import (java.lang.annotation Annotation)
           (org.junit.runner Description)
           (org.junit.runner.notification Failure)
           (junit.framework TestFailure)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; JUnit helpers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn describe-suite [name]
  (Description/createSuiteDescription name (into-array Annotation [])))

(defn describe-test [test-var]
  (let [suite (-> test-var meta :ns str)
        test (-> test-var meta :name str)]
    (Description/createTestDescription suite test (into-array Annotation []))))

(defrecord Test [description var])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Report for scanning for test vars.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:dynamic *tests* nil)

(defmulti scan-report :type)

(defmethod scan-report :begin-test-var [m]
  (println m)
  (let [test-var (:var m)
        description (describe-test test-var)
        test (->Test description test-var)]
    (swap! *tests* conj test)))

(defmethod scan-report :default [m]
  (println m)
  nil)

(defn scan-tests []
  (let [real-test-var test/test-var]
    (binding [*tests* (atom [])
              test/report scan-report
              test/test-var real-test-var]
      (test/run-all-tests)
      @*tests*)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Report for notifying JUnit.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Bound to a org.junit.runner.notification.RunNotifier
(def ^:dynamic *notifier* nil)

(def ^:dynamic *current-test* nil)

(defn fail-message [{:keys [message expected actual]}]
  (with-out-str
    (if message
      (println message)
      (println))
    (println "expected: " (pr-str expected))
    (if-not (instance? Throwable actual)
      (println "actual:   " (prn-str actual)))))

(defn fail-ex [{:keys [actual] :as m}]
  (if (instance? Throwable actual)
    (RuntimeException. (fail-message m) actual)
    (RuntimeException. (fail-message m))))

(defmulti notifier-report :type)

(defmethod notifier-report :begin-test-var [m]
  (let [desc (describe-test (:var m))]
    (reset! *current-test* desc)
    (.fireTestStarted *notifier* desc)))

(defmethod notifier-report :end-test-var [m]
  (let [desc (describe-test (:var m))]
    (reset! *current-test* nil)
    (.fireTestFinished *notifier* desc)))

(defmethod notifier-report :fail [m]
  (let [desc @*current-test*]
    (.fireTestFailure *notifier* (Failure. desc (fail-ex m)))))

(defmethod notifier-report :error [m]
  (let [desc @*current-test*]
    (.fireTestFailure *notifier* (Failure. desc (fail-ex m)))))

(defmethod notifier-report :default [_] nil)

(defmacro with-notifier [notifier & body]
  `(binding [*notifier* ~notifier
             *current-test* (atom nil)
             test/report notifier-report]
     (do ~@body)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; JUnit runner.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(gen-class
  :name org.graclj.tools.test.ClojureTestRunner
  :implements [org.junit.runner.manipulation.Filterable]
  :extends org.junit.runner.Runner
  :constructors {[Class] []}
  :init init
  :state tests)

(defn -init [_]
  (println (seq (.getURLs (ClassLoader/getSystemClassLoader))))
  (println "In init method...")
  [[] (atom (scan-tests))])

(defn -getDescription [this]
  (println "In getDescription method...")
  (let [suite (describe-suite "GracljFakeSuite")]
    (doseq [test @(.tests this)]
      (.addChild (:description test)))
    (println suite)
    suite))

(defn -run [this notifier]
  (println "In run method....")
  (println @(.tests this))
  (let [test-vars (map :var @(.tests this))]
    (println test-vars)
    (with-notifier notifier
       (test/test-vars test-vars))))

(defn -filter [this desc-filter]
  (println "In filter method...")
  (letfn [(run? [test] (->> test :description (.shouldRun desc-filter)))
          (trim [tests] (filter run? tests))]
    (swap! (.tests this) trim)))

(deftype ^{org.junit.runner.RunWith org.graclj.tools.test.ClojureTestRunner} GracljSuite [])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Does this work?
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(test/deftest addition-works2
         (test/is (= (+ 1 2) 3)))

(test/deftest subtraction-fails2
         (test/is (= (- 2 1) 2)))
