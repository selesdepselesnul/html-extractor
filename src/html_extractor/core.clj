(ns html-extractor.core
  (:gen-class)
  (:require [overtone.at-at :as at-at]
            [progrock.core :as pr]
            [clj-progress.core :refer :all]
            [html-extractor.html-extractor :refer :all]
            [org.httpkit.client :as http]))

(defn -main
  [& args]
  (fetch-url (first args)
             #(println "error")
             #(println "Please wait...")
             #(println "Finished, Enjoy !")
             #(println (str "Store -> " %))))

