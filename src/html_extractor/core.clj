(ns html-extractor.core
  (:gen-class)
  (:require [html-extractor.html-extractor :refer :all]
            [org.httpkit.client :as http]
            [clojure.term.colors :refer :all]))

(defn -main
  [& args]
  (fetch-url (first args)
             #(println "error")
             #(println "Please wait...")
             #(println "Finished, Enjoy !")
             #(println (reverse-color (str "Store -> " %)))))

