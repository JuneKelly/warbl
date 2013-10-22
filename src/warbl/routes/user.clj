(ns warbl.routes.user
  (:use compojure.core)
  (:require [warbl.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [warbl.models.db :as db]
            [warbl.helpers.auth :as auth]))


(defn profile []
  (if (auth/logged-in?)
    (layout/render
      "profile.html"
      {:user (db/get-user (auth/current-user))})
    (resp/redirect "/")))


(defn update-profile [{:keys [first-name last-name email]}]
  (if (auth/logged-in?)
    (do
      (db/update-user (auth/current-user) first-name last-name email)
      (session/flash-put! :flash-success "Profile updated!")
      (resp/redirect "/profile"))))


(defroutes user-routes
  (GET "/profile" [] (profile))
  (POST "/update-profile" {params :params} (update-profile params)))
