(ns frontend.rules-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [finbuddy.validator :as v]))

(deftest rule-valid-test
  (testing "Test if rule-valid? return true on corect rules."
    (is (= true (v/rule-valid? ::v/contains {::v/fields [:email :name]
                                             ::v/opts {:ignoreCase true}})))))

(deftest rule-valid-inverse-test
  (testing "Test if rule-valid? return false on incorect rules"
    (is (= false (v/rule-valid? ::v/contains {::v/opts {:ignoreCase true}})))))
