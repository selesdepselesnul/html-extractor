(ns html-extractor.txt-extractor
  (:gen-class)
  (:require [html-extractor.util :as hte-util]
            [net.cgrand.enlive-html :as html]))

(defn html->p [body]
  (-> body
      hte-util/get-resource
      (html/select [:p])))

(defn p->text [x]
  (:content x))

(defn html->text-seq [body]
  (->> (html->p body)
       (map p->text)
       (filter (comp not nil?))
       flatten
       (map  #(if (map? %) (p->text %) %))
       flatten
       (filter string?)))

(defn html->content-string [body]
  (->> (html->text-seq body)
       (clojure.string/join "")))

(defn fetch-txt-from-url
  [url file-name action]
  (let [{before-downloading :before-downloading
         after-downloading :after-downloading
         on-error :on-error} action]
    (hte-util/fetch-url url
                        (fn [_ body]
                          (before-downloading)
                          (->> (html->content-string body)
                               (spit file-name))
                          (after-downloading))
                        on-error)))
