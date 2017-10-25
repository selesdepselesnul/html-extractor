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

(http/get "https://techcrunch.com/2017/10/24/here-are-the-cars-that-support-iphone-8-and-iphone-x-wireless-charging/"
          (fn [{:keys [status headers body error]}] ;; asynchronous response handling
            (if error
              (println "Failed, exception is " error)
              (do
                (println (str "mantab" body))
                (swap! html-string (fn [_] body))))))

(defn- fetch-photo!
  [url]
  (let [req (http/get url {:as :byte-array :throw-exceptions false})]
    (if (= (:status req) 200)
      (:body req))))

(defn- save-photo!
  [photo]
  (let [p (fetch-photo! (:url photo))]
    (if (not (nil? p))
      (with-open [w (io/output-stream (str "photos/" (:id photo) ".jpg"))]
        (.write w p)))))

(defn fetch-to-file [url file]
  (with-open [in (io/input-stream url) 
              out (io/output-stream file)]
    (io/copy in out)))

@html-string

(get-image-link @html-string)

(def image-links (get-image-link @html-string))

image-links

(doseq [x image-links]
  (print x))
