(ns frontend.validator-test
    (:require [cljs.test :refer-macros [deftest is testing]]
              [finbuddy.validator :as v]))

(defonce form-data-simple {:name "Andy"})
(defonce form-data-medium {:name "Andy"
                           :email "Rusu.Andy1@gmail.com"
                           :content "   <test>   "
                           :is-good "1"})

(deftest sanitize-nil-test
  (testing "Sanitize function with nil as rule"
    (is (= {:name "Andy"} (v/sanitize form-data-simple nil)))))

(deftest sanitize-min-test
  (testing "Sanitize function minimun requirement"
    (is (= {:name "And"} (v/sanitize form-data-simple {::v/blacklist {::v/fields [:name]
                                                                      ::v/chars "y"}})))))

(deftest sanitize-full-test
  (testing "Sanitize function complete"
    (is (= {:name "And"
            :email "rusuand1@gmail.com"
            :content "&lt;test&gt;"
            :is-good true}
           (v/sanitize form-data-medium
                       {::v/blacklist {::v/fields [:name :email]
                                       ::v/chars "y"}
                        ::v/normalize-email {::v/fields [:email]
                                             ::v/opts {:gmail-lowercase true
                                                       :gmail-remove-dots true
                                                       :gmail-remove-subaddress true
                                                       :gmail-convert-googleemaildotcom true}}
                        ::v/trim {::v/fields [:content]}
                        ::v/escape {::v/fields [:content]}
                        ::v/to-boolean {::v/fields [:is-good]}})))))