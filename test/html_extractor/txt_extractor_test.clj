(ns html-extractor.txt-extractor-test
  (:require [html-extractor.txt-extractor :as sut]
            [clojure.test :as t]))

(def html-string-contain-p
  "<body>
        <div><img src='http://example.com/image.png'></div>
        <span>Mantab</span>
        <img src='http://example.com/image.jpg'>
        <p>This is content</p>
        <br/>
  </bod")

(def html-string-without-p
  "<body>
        <span>Mantab</span>
        <img src=''>
  </bod")

(t/deftest html->content-string-test
  (t/testing "contains p"
    (t/is (= "This is content" (sut/html->content-string html-string-contain-p))))
  (t/testing "without p"
    (t/is (= "" (sut/html->content-string html-string-without-p)))))
