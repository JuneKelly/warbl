(ns warbl.routes.dashboard
  (:use compojure.core)
  (:require [noir.response :as resp]
            [noir.session :as session]
            [warbl.views.layout :as layout]
            [warbl.util :as util]
            [warbl.helpers.auth :as auth]
            [warbl.helpers.routes :refer [kick-to-root]]
            [warbl.models.db :as db]))


(defn dashboard []
  (if (auth/logged-in?)
    (layout/render "dashboard.html"
                   {:user (db/get-user (auth/current-user))})
    (kick-to-root "You must be logged in to view that page")))

(defroutes dashboard-routes
  (GET "/dashboard" []
       (dashboard)))
