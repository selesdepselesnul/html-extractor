(defproject html_extractor "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [enlive "1.1.6"]
                 [lambdaisland/uri "1.1.0"]
                 [clojure-term-colors "0.1.0-SNAPSHOT"]
                 [org.clojure/tools.cli "0.3.5"]]
  :main ^:skip-aot html-extractor.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
