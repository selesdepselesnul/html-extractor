(ns html-extractor.util  (:gen-class)
    (:require [org.httpkit.client :as http]
              [net.cgrand.enlive-html :as html]
              [clojure.string :as string]
              [clojure.repl :as repl]
              [clojure.java.io :as io]
              [lambdaisland.uri :refer [uri relative?]]
              [clojure.pprint :refer [pprint]])
    (:import (java.io.StringReader)
             (java.net URL)))

(defn get-resource [html-string]
  (-> html-string
      java.io.StringReader.
      html/html-resource))

(defn is-url-relative? [url]
  (relative? (uri url)))

(defn fetch-url [url on-success on-error]
  (let [{:keys [error body] :as resp} @(http/get url)]
    (if error
      (on-error error)
      (on-success resp body))))

(defn ext-seq->trimmed-ext-seq [ext-seq]
  (map clojure.string/trim ext-seq))

(defn string-exts->set [string-exts]
  (when-not (nil? string-exts)
    (-> string-exts
        (string/split #",")
        ext-seq->trimmed-ext-seq
        set)))
