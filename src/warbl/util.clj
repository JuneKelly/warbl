(ns warbl.util
  (:require [noir.io :as io]
            [markdown.core :as md]))


(defn md->html
  "reads a markdown file from public/md and returns an HTML string"
  [filename]
  (->>
    (io/slurp-resource filename)
    (md/md-to-html-string)))


(defn datetime [] (new java.util.Date))


(defn uuid [] (str (java.util.UUID/randomUUID)))
