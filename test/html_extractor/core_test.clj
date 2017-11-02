(ns html-extractor.core-test
  (:require [clojure.test :refer :all]
            [html-extractor.core :refer :all]
            [html-extractor.img-extractor-test]
            [html-extractor.util-test]))


(run-tests 'html-extractor.img-extractor-test
           'html-extractor.util-test)
