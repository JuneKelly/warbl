(ns warbl.test-util
  (:require [warbl.models.db :as db]
            [warbl.env :refer [config]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            [monger.db :as md]
            [noir.util.crypt :as crypt])
  (:import [org.bson.types ObjectId]))


;; DB helpers
(mg/connect-via-uri! (config :db-url))


(defn drop-database! []
  (md/drop-db (mg/get-db (config :db-name))))


(defn populate-users []
  (mc/insert "users" {:_id "userone",
                      :password (crypt/encrypt "password")})
  (mc/insert "users" {:_id "usertwo",
                      :password (crypt/encrypt "password")}))
