(ns warbl.models.db
  (:require [warbl.models.schema :as schema])
  (:require [warbl.env :as env])
  (:require [monger.core :as mg])
  (:require [monger.collection :as mc])
  (:import  [org.bson.types ObjectId]
            [com.mongodb DB WriteConcern])
  (:require [monger.query :as mq]))


(mg/connect-via-uri! env/db-url)


;; Users
(defn create-user [id, pass]
  (let [doc {:_id id, :password pass,
             :created (java.util.Date. )}]
    (mc/insert "users" doc)))


(defn update-user [id {:keys [first-name last-name email location]
                    :as user-details}]
  (mc/update-by-id "users" id
    {:$set {:f_name first-name,
            :l_name last-name,
            :email email,
            :location location,
            :updated (new java.util.Date)}}))


(defn user-exists? [id]
  (not (nil?
         (mc/find-map-by-id "users" id))))


(defn get-user [id]
  (mc/find-map-by-id "users" id))


(defn get-all-users []
  (mc/find-maps "users" {}))
