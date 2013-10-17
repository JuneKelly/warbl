(ns warbl.test-util
  (:require [warbl.models.db :as db]
            [warbl.env :refer [config]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            [monger.db :as md]
            [noir.util.crypt :as crypt]
            [clj-webdriver.taxi :as t])
  (:import [org.bson.types ObjectId]))


(def site-root (config :site-url))


;; DB helpers
(mg/connect-via-uri! (config :db-url))


(defn drop-database! []
  (md/drop-db (mg/get-db (config :db-name))))


(defn populate-users []
  (mc/insert "users" {:_id "userone",
                      :password (crypt/encrypt "password")})
  (mc/insert "users" {:_id "usertwo",
                      :password (crypt/encrypt "password")}))


(defn login-userone []
  (t/to site-root)
  (t/quick-fill-submit {"#id" "userone"}
                       {"#pass" "password"}
                       {"input.btn[value=Login]" t/click}))
