(ns warbl.models.db
  (:require [warbl.models.schema :as schema])
  (:require [warbl.env :refer [config]])
  (:require [monger.core :as mg])
  (:require [monger.collection :as mc])
  (:import  [org.bson.types ObjectId]
            [com.mongodb DB WriteConcern])
  (:require [monger.query :as mq]))


(mg/connect-via-uri! (config :db-url))


;; Users
(defn create-user [id, pass]
  (let [doc {:_id id, :password pass}]
    (mc/insert "users" doc)))


(defn update-user [id f-name l-name email]
  (mc/update-by-id "users" id
    {:$set {:f_name f-name,
            :l_name l-name,
            :email email}}))


(defn user-exists? [id]
  (not (nil?
         (mc/find-map-by-id "users" id))))


(defn get-user [id]
  (if (user-exists? id)
    (mc/find-map-by-id "users" id)
    nil))


(defn get-all-users []
  (mc/find-maps "users" {}))
