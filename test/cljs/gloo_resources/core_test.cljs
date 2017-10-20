(ns gloo-resources.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [gloo-resources.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
