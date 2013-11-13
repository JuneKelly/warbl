(ns warbl.routes.user
  (:use compojure.core)
  (:require [warbl.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [warbl.models.db :as db]
            [warbl.helpers.auth :as auth]
            [warbl.helpers.gravatar :refer [gravatar-large]]))


(defn is-current-user? [id]
  (= (auth/current-user) id))


(defn profile [id]
  (let [user (db/get-user id)]
    (layout/render "profile.html"
      {:user user
       :is-current-user (is-current-user? id)
       :gravatar-url (gravatar-large user)})))


(defn update-profile [user-details]
  (if (auth/logged-in?)
    (do
      (db/update-user (auth/current-user)
                      user-details)
      (session/flash-put! :flash-success
                          "Profile updated!")
      (resp/redirect (str "/profile/" (auth/current-user))))))


(defroutes user-routes
  (GET "/profile/:id" [id] (profile id))
  (POST "/update-profile" {params :params} (update-profile params)))
