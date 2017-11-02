(ns html-extractor.util-test
  (:require [html-extractor.util :as sut]
            [clojure.test :refer :all]))

(deftest is-url-relative?-test
  (testing "non relative url"
    (is (= false (sut/is-url-relative? "http://example.com/image.png"))))
  (testing "non relative url"
    (is (= true (sut/is-url-relative? "example.com/image.png")))))
