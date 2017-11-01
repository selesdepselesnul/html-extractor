(ns html-extractor.core
  (:gen-class)
  (:require [html-extractor.html-extractor :refer :all]
            [org.httpkit.client :as http]
            [clojure.term.colors :refer :all]
            [clojure.string :as cljstr]))

(defn -main
  [& args]
  (fetch-url
   (first args)
   {:before-downloading #(println "Please wait...")
    :after-downloading-each-item #(let [download-text (str "Saved -> " %)] 
                                    (println
                                     (if (cljstr/includes? (cljstr/lower-case (System/getProperty "os.name")) "windows")
                                       download-text
                                       (reverse-color download-text))))
    :after-downloading #(println "Finished, Enjoy !")
    :on-error #(println "error")}))


