(ns warbl.models.db
  (:require [warbl.models.schema :as schema]
            [warbl.env :as env]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            [warbl.util :refer [datetime uuid]])
  (:import  [org.bson.types ObjectId]
            [com.mongodb DB WriteConcern]))


(mg/connect-via-uri! env/db-url)


;; Users
(defn create-user [id, pass]
  (let [doc {:_id id, :password pass,
             :created (datetime),
             :r (rand)}]
    (mc/insert "users" doc)))


(defn update-user [id {:keys [first-name last-name email location]
                    :as user-details}]
  (mc/update-by-id "users" id
    {:$set {:f_name first-name,
            :l_name last-name,
            :email email,
            :location location,
            :r (rand),
            :updated (datetime)}}))


(defn user-exists? [id]
  (not (nil?
         (mc/find-map-by-id "users" id))))


(defn get-user [id]
  (mc/find-map-by-id "users" id))


(defn get-all-users []
  (mc/find-maps "users" {}))


(defn get-random-users [& {:keys [maximum]}]
  (let [ra (rand) m (or maximum 16)]
    (mq/with-collection "users"
      (mq/find {:r {"$gte" ra}})
      (mq/limit m))))


;; Messages
(defn create-message
  [{:keys [from-user-id to-user-id text]}]
  (let [doc {:_id (uuid)
             :from from-user-id
             :to to-user-id
             :text text
             :created (datetime)}]
    (mc/insert "messages" doc)))


(defn get-messages [{:keys [from-user-id to-user-id]}]
  (mq/with-collection "messages"
    (mq/find {:$or [{:from from-user-id, :to to-user-id},
                    {:from to-user-id,   :to from-user-id}]})
    (mq/sort {:created 1})))
