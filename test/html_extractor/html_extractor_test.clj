(ns html-extractor.html-extractor-test
  (:require [clojure.test :refer :all]
            [html-extractor.html-extractor :refer :all]))

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

(testing "get-image-link"
  (testing "with html string contains img"
    (= ["http://example.com/image.png" "http://example.com/image.jpg"]
       (vec (get-image-link html-string-contain-img))))
  (testing "with html string without img"
    (= [] (vec (get-image-link html-string-without-img)))))
