;; Copyright (C) 2012-present, Polis Technology Inc. This program is free software: you can redistribute it and/or  modify it under the terms of the GNU Affero General Public License, version 3, as published by the Free Software Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details. You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.


(ns conv-man-tests
  (:use polismath.utils
        test-helpers)
  (:require [clojure.test :refer :all]
            [polismath.conv-man :refer :all]))


(deftest get-or-set-test
  (testing "should work when not set"
    (let [a (atom {})]
      (get-or-set! a :shit (fn [] "stuff"))
      (is (= (:shit @a) "stuff"))))
  (testing "should work when set"
    (let [a (atom {:shit "stuff"})]
      (is (= (get-or-set! a :shit (fn [] "crap"))
             "stuff")))))


; Initialization test
(deftest split-batches-test
  (testing "of a missing param"
    (let [messages [[:votes [{:a 4} {:b 5} {:c 6}]]
                    [:votes [{:d 7} {:e 8} {:f 9}]]]]
      (is (= (split-batches messages)
             {:votes [{:a 4} {:b 5} {:c 6} {:d 7} {:e 8} {:f 9}]}))
      (is (nil? (:moderation (split-batches messages))))))
  (testing "of one message"
    (let [messages [[:votes [{:a 4} {:b 5} {:c 6}]]]]
      (is (= (split-batches messages)
             {:votes [{:a 4} {:b 5} {:c 6}]}))))
  (testing "of combined messages"
    (let [messages [[:votes [{:a 4} {:b 5} {:c 6}]]
                    [:votes [{:d 7} {:e 8} {:f 9}]]
                    [:moderation [{:g 4} {:h 5} {:i 6}]]
                    [:moderation [{:j 7} {:k 8} {:l 9}]]]]
      (is (= (split-batches messages)
             {:votes [{:a 4} {:b 5} {:c 6} {:d 7} {:e 8} {:f 9}]
              :moderation [{:g 4} {:h 5} {:i 6} {:j 7} {:k 8} {:l 9}]})))))


