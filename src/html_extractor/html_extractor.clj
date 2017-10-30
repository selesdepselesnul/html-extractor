(ns html-extractor.html-extractor
  (:gen-class)
  (:require [org.httpkit.client :as http]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [clojure.repl :as repl]
            [clojure.java.io :as io]
            [lambdaisland.uri :refer [uri]]
            [clojure.pprint :refer [pprint]])
  (:import (java.io.StringReader)
           (java.net URL)))

(defn copy [uri file]
  (with-open [in (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))

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

(defn fetch-image [file url]
  (try (with-open [in (io/input-stream url) 
                   out (io/output-stream file)]
         (io/copy in out)
         url)
       (catch Exception e)))

(defn get-image-name [url]
  (last (string/split url #"/")))

(defn get-image-ext [image]
  (last (string/split image #"\.")))

(defn is-extension-valid? [extension]
  (contains? #{"jpg" "png" "svg" "gif"} extension))

(defn is-valid-image-link? [image-link]
  (-> image-link 
      get-image-name
      get-image-ext
      is-extension-valid?))

(defn is-full-url? [url]
  (.getPath (URL. url)))

(defn map-to-valid-image-url [base-url url]
  (str base-url url))

(defn get-only-valid-image-name [image-links]
  (->> image-links
       (filter is-valid-image-link?)))

(defn fetch-no-protocol-image
  ([image-name url] (fetch-no-protocol-image image-name url ["https:" "https://" "http:" "http://"]))
  ([image-name url protocol-patterns]
   (when (and (not-empty protocol-patterns)
              (not (fetch-image image-name (str (first protocol-patterns) url))))
     (recur image-name url (rest protocol-patterns)))))

(defn fetch-images [url image-links]
  (doseq [x (get-only-valid-image-name image-links)]
    (let [image-name (get-image-name x)] 
      (when (not (fetch-image (get-image-name x) x))  
        (fetch-no-protocol-image image-name x)))))

(defn fetch-url [url]
  (http/get url
            (fn [{:keys [status headers body error]}] ;; asynchronous response handling
              (if error
                (println "Failed, exception is " error)
                (do
                  (->> (get-image-link body)
                       (fetch-images url)))))))
