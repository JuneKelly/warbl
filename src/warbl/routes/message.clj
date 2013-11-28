(ns warbl.routes.message
  (:use compojure.core)
  (:require [warbl.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [warbl.models.db :as db]
            [warbl.helpers.auth :as auth]
            [warbl.helpers.routes :refer [kick-to-root]]))


(defn create-message [to-user-id text]
  (if (auth/logged-in?)
    (let [current-user-id (auth/current-user)]
      (do
        (db/create-message
          {:from-user-id current-user-id
           :to-user-id to-user-id
           :text text})
        (session/flash-put! :flash-success
                            (str "Message sent to " to-user-id))
        (resp/redirect (str "/conversation/" to-user-id))))
    (do
      (kick-to-root "You should log in or create an account first"))))


(defn show-conversation [with-user-id]
  (if (auth/logged-in?)
    (let [current-user-id (auth/current-user)
          messages (db/get-messages
                      {:from-user-id current-user-id
                       :to-user-id with-user-id})]
      (do
        (layout/render "conversation.html"
                       {:messages messages
                        :current-user-id current-user-id
                        :target-user-id with-user-id})))
    (do
      (kick-to-root))))


(defroutes message-routes
  (POST "/message/to/:user"   [user text] (create-message user text))
  (GET  "/conversation/:user" [user] (show-conversation user)))
