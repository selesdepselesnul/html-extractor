(ns html-extractor.img-extractor
  (:gen-class)
  (:require [org.httpkit.client :as http]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [clojure.repl :as repl]
            [clojure.java.io :as io]
            [lambdaisland.uri :refer [uri relative?]]
            [clojure.pprint :refer [pprint]]
            [html-extractor.util :as hte-util])
  (:import (java.io.StringReader)
           (java.net URL)))

(defn select-image [resource]
  (->> (html/select resource [:img])
       (map #(:src (:attrs %)))
       (filter #(not (string/blank? %)))))

(defn get-image-link [html-string]
  (-> html-string
      hte-util/get-resource
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

(defn map-to-valid-image-url [base-url url]
  (str base-url url))

(defn get-only-valid-image-name [image-links]
  (->> image-links
       (filter is-valid-image-link?)))

(defn fetch-no-protocol-image
  ([image-name url]
   (fetch-no-protocol-image image-name url ["https:" "https://" "http:" "http://"]))
  ([image-name url protocol-patterns]
   (when (and (not-empty protocol-patterns)
              (not (fetch-image image-name (str (first protocol-patterns) url))))
     (recur image-name url (rest protocol-patterns)))))

(defn make-full-url [url relative-url]
  (let [uri-obj (uri url)
        {scheme :scheme
         port :port
         host :host} uri-obj]
    (str scheme "://" host  (string/replace relative-url #"\.\." ""))))

(defn fetch-relative-url-image [file fetch-url relative-url]
  (->> (make-full-url fetch-url relative-url)
       (fetch-image file)))

(defn fetch-images [url after-downloading-completed image-links]
  (doseq [x image-links]
    (let [image-name (get-image-name x)]
      (if (hte-util/is-url-relative? x)
        (when (not (fetch-relative-url-image image-name
                                             url
                                             x))
          (fetch-no-protocol-image image-name x))
        (fetch-image (get-image-name x) x))
      (when (.exists (io/as-file image-name))
        (after-downloading-completed image-name)))))

(defn fetch-image-from-url
  [url action]
  (let [{before-downloading :before-downloading
         after-downloading-each-item :after-downloading-each-item
         after-downloading :after-downloading
         on-error :on-error} action]
    (hte-util/fetch-url url
                        (fn [_ body]
                          (before-downloading)
                          (->> (get-image-link body)
                               (fetch-images url after-downloading-each-item))
                          (after-downloading))
                        on-error)))
