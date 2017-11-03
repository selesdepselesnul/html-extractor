(ns html-extractor.core
  (:gen-class)
  (:require [html-extractor.img-extractor :refer :all]
            [html-extractor.txt-extractor :refer :all]
            [org.httpkit.client :as http]
            [clojure.term.colors :refer :all]
            [clojure.string :as cljstr]
            [clojure.tools.cli :refer [parse-opts]]))

(defn extract-text [url file-name]
  (println "extract text")
  (fetch-txt-from-url url
                      file-name
                      {:before-downloading #(println "Please wait...")
                       :after-downloading #(println "Finished, Enjoy !")
                       :on-error #(println "error")}))

(defn extract-image [url]
  (println "extract image")
  (fetch-image-from-url
   url
   {:before-downloading #(println "Please wait...")
    :after-downloading-each-item
    #(let [download-text (str "Saved -> " %)]
       (println
        (if (cljstr/includes?
             (cljstr/lower-case (System/getProperty "os.name")) "windows")
          download-text
          (reverse-color download-text))))
    :after-downloading #(println "Finished, Enjoy !")
    :on-error #(println "error")}))

(def cli-options
  [["-u" "--url url" "url to fetch"]
   ["-i" "--image" "fetch image"]
   ["-t" "--text" "fetch text"]
   ["-n" "--file-name file name" "file name store in fs"]])

(defn -main
  [& args]
  (let [options
        (:options (parse-opts args cli-options))]
    (when-let [url (get options :url)]
      (cond
        (contains? options :image) (extract-image url)
        (and (contains? options :text)
             (contains? options :file-name))
        (extract-text url (:file-name options))))))
