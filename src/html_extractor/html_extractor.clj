(ns html-extractor.html-extractor
  (:gen-class)
  (:require [org.httpkit.client :as http]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [clojure.repl :as repl])
  (:import java.io.StringReader))

(defn get-body [url]
  (:body @(http/get url)))

(get-body "https://9gag.com/")

(def response @(http/get "https://9gag.com/"))

(http/get "http://host.com/path" options
          (fn [{:keys [status headers body error]}] ;; asynchronous response handling
            (if error
              (println "Failed, exception is " error)
              (println "Async HTTP GET: " status))))

(defn get-resource [html-string]
  (-> html-string
      java.io.StringReader.
      html/html-resource))

(defn select-image [resource]
  (->> (html/select resource [:img])
       (map #(:src (:attrs %)))
       (filter #(not (string/blank? %)))))

(defn get-image-link [html-string]
  (-> html-string
      get-resource
      select-image))

(def image-links (get-image-link (:body response)))
