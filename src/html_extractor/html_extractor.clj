(ns html-extractor.html-extractor
  (:gen-class)
  (:require [org.httpkit.client :as http]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [clojure.repl :as repl]
            [clojure.java.io :as io])
  (:import java.io.StringReader))

(def html-string (atom ""))

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

(http/get "http://www.livescience.com/"
          (fn [{:keys [status headers body error]}] ;; asynchronous response handling
            (if error
              (println "Failed, exception is " error)
              (do
                (println (str "mantab" body))
                (swap! html-string (fn [_] body))))))

(defn fetch-to-file [url file]
  (with-open [in (io/input-stream url) 
              out (io/output-stream file)]
    (io/copy in out)))

(def image-links (get-image-link @html-string))

(defn is-valid-image-link? [image-link]
  (-> image-link 
      get-image-name
      get-image-ext
      is-extension-valid?))

(defn get-image-name [url]
  (last (string/split url #"/")))

(defn get-image-ext [image]
  (last (string/split image #"\.")))

(defn is-extension-valid? [extension]
  (contains? #{"jpg" "png" "svg"} extension))

(defn get-only-valid-image-name [image-links]
  (->> image-links
       (filter is-valid-image-link?)))

(doseq [x (get-only-valid-image-name image-links)]
  (print x)
  (fetch-to-file x (get-image-name x)))
