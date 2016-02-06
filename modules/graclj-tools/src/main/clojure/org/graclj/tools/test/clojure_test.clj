(ns org.graclj.tools.test.clojure-test
  (:require [clojure.test :as test]
            [clojure.java.classpath :as cp]
            [clojure.java.io :as io]
            [clojure.tools.namespace.find :refer [find-namespaces]])
  (:import (java.lang.annotation Annotation)
           (org.junit.runner Description)
           (org.junit.runner.notification Failure)
           (java.io File)))

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
  (let [test-var (-> m :var meta :old-var)
        description (describe-test test-var)
        test (->Test description test-var)]
    (swap! *tests* conj test)))

(defmethod scan-report :default [m]
  nil)

(defn suppress-test-var [real-test-var]
  (fn [v]
    (let [old-meta (meta v)
          new-meta (assoc old-meta :test (fn [& _] nil) :old-var v)
          suppressed (with-meta @v new-meta)]
      (real-test-var suppressed))))

(defn scan-tests [namespaces]
  (let [real-test-var test/test-var]
    (binding [*tests* (atom [])
              test/report scan-report
              test/test-var (suppress-test-var real-test-var)]
      (apply test/run-tests namespaces)
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
  :state state)

(defn -init [clazz]
  (let [test-dirs (map #(File. ^String %)
                       (.split (System/getProperty "clojure.test.dirs")
                               (File/pathSeparator)))
        namespaces (find-namespaces test-dirs)]
    (doseq [namespace namespaces]
      (require namespace))
    [[] (atom {:parent clazz :tests (scan-tests namespaces)})]))

(defn -getDescription [this]
  (let [suite (describe-suite (-> this .state deref :parent str))]
    (doseq [test (-> this .state deref :tests)]
      (.addChild suite (:description test)))
    suite))

(defn -run [this notifier]
  (let [test-vars (map :var (-> this .state deref :tests))]
    (with-notifier notifier
       (test/test-vars test-vars))))

(defn -filter [this desc-filter]
  (letfn [(run? [test] (->> test :description (.shouldRun desc-filter)))
          (trim [tests] (filter run? tests))]
    (swap! (.state this) update :tests trim)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Generate suite stub.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(gen-class
  :name ^{org.junit.runner.RunWith org.graclj.tools.test.ClojureTestRunner} clojure.test)
