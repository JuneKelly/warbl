(ns warbl.routes.dashboard
  (:use compojure.core)
  (:require [noir.response :as resp]
            [noir.session :as session]
            [warbl.views.layout :as layout]
            [warbl.util :as util]
            [warbl.helpers.auth :as auth]
            [warbl.models.db :as db]))


(defn dashboard []
  (if (auth/logged-in?)
    (layout/render "dashboard.html"
                   {:user (db/get-user (auth/current-user))})
    (do
      (session/flash-put!
        :flash-info "You must be logged in to view that page")
      (resp/redirect "/"))))


(defroutes dashboard-routes
  (GET "/dashboard" []
       (dashboard)))
