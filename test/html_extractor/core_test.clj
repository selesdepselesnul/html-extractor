(ns html-extractor.core-test
  (:require [clojure.test :refer :all]
            [html-extractor.core :refer :all]
            [html-extractor.img-extractor-test]
            [html-extractor.util-test]))

(run-tests
 'html-extractor.util-test
 'html-extractor.img-extractor-test
 'html-extractor.txt-extractor-test)
