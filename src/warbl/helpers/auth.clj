(ns warbl.helpers.auth
  (:require [noir.session :as session]))


(defn logged-in? []
  (not (nil? (session/get :user-id))))


(defn current-user []
  (if (logged-in?)
    (session/get :user-id)
    nil))


(defn log-in [id]
  (session/put! :user-id id))


(defn log-out []
  (session/clear!))
