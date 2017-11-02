(ns html-extractor.txt-extractor
  (:gen-class)
  (:require [html-extractor.util :as hte-util]
            [net.cgrand.enlive-html :as html]))

(defn get-p [body]
  (-> body
      hte-util/get-resource
      (html/select [:p])))

(defn process-body [resp body]
  (->> (get-p body)
       (map #(get % :content))
       (filter (comp not nil?))
       flatten))

(hte-util/fetch-url "https://en.wikipedia.org/wiki/Alonzo_Church"
                    process-body
                    (fn [error] (println "error")))



