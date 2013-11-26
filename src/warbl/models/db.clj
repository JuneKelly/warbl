(ns warbl.models.db
  (:require [warbl.models.schema :as schema]
            [warbl.env :as env]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            [warbl.util :refer [datetime uuid]]
            [warbl.helpers.gravatar :refer [get-gravatars]])
  (:import  [org.bson.types ObjectId]
            [com.mongodb DB WriteConcern]))


(mg/connect-via-uri! env/db-url)


;; Users
(defn create-user [id, pass]
  (let [doc {:_id id, :password pass,
             :created (datetime),
             :gravatars (get-gravatars nil),
             :r (rand)}]
    (mc/insert "users" doc)))


(defn update-user [id
                   {:keys [full-name
                           email
                           location]}]
  (mc/update-by-id "users" id
    {:$set {:full_name full-name
            :email email,
            :location location,
            :gravatars (get-gravatars email)
            :r (rand),
            :updated (datetime)}}))


(defn user-exists? [id]
  (not (nil?
         (mc/find-map-by-id "users" id))))


(defn get-user [id]
  (mc/find-map-by-id "users" id))


(defn get-user-safe [id]
  (let [user (get-user id)]
    (dissoc user :password)))


(defn get-all-users []
  (mc/find-maps "users" {}))


(defn get-random-users [& {:keys [maximum]}]
  (let [ra (rand) m (or maximum 16)]
    (mq/with-collection "users"
      (mq/find {:r {"$gte" ra}})
      (mq/limit m))))


;; Contacts
(defn add-contact [user-id contact-id]
  (if (and (user-exists? user-id)
           (user-exists? contact-id))
    (do
      (mc/update "users"
                 {:_id user-id}
                 {:$addToSet {:contacts contact-id}})
      true)
    false))


(defn has-contact [user-id contact-id]
  (let [u (get-user user-id)]
    (if (contains? (set (u :contacts)) contact-id)
      true
      false)))


(defn get-contacts [user-id]
  (if (user-exists? user-id)
    (let [user (get-user user-id)
          contacts (:contacts user)]
      (map get-user-safe contacts))
    nil))


;; Messages
(defn create-message
  [{:keys [from-user-id to-user-id text]}]
  (if (has-contact from-user-id to-user-id)
    (do
      (let [doc {:_id (uuid)
                 :from from-user-id
                 :to to-user-id
                 :text text
                 :created (datetime)}]
        (mc/insert "messages" doc))
      true)
    false))


(defn get-messages [{:keys [from-user-id to-user-id]}]
  (mq/with-collection "messages"
    (mq/find {:$or [{:from from-user-id, :to to-user-id},
                    {:from to-user-id,   :to from-user-id}]})
    (mq/sort {:created 1})))
