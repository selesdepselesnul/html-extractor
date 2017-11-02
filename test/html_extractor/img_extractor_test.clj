(ns html-extractor.img-extractor-test
  (:require [clojure.test :refer :all]
            [html-extractor.img-extractor :refer :all]))

(def html-string-contain-img
  "<body>
        <div><img src='http://example.com/image.png'></div>
        <span>Mantab</span>
        <img src='http://example.com/image.jpg'>
  </bod")

(def html-string-without-img
  "<body>
        <span>Mantab</span>
        <img src=''>
  </bod")

(deftest get-image-link-test
  (testing "contains img"
    (is (= ["http://example.com/image.png" "http://example.com/image.jpg"]
           (vec (get-image-link html-string-contain-img)))))
  (testing "without img"
    (is (= [] (vec (get-image-link html-string-without-img))))))
