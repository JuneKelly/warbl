(ns warbl.models.schema
  (:require [noir.io :as io]
            [monger.core :as mg]
            [monger.collection :as mc]
            [warbl.env :refer [config]]))


(mg/connect-via-uri! (config :db-url))


(defn create-indexes []
  (mc/ensure-index "users"
                   {:l_name 1}))
