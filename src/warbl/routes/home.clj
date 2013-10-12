(ns warbl.routes.home
  (:use compojure.core)
  (:require [warbl.views.layout :as layout]
            [warbl.util :as util]
            [warbl.helpers.auth :as auth]
            [noir.response :as resp]
            [noir.session :as session]))


(defn home-page []
  (if (auth/logged-in?)
    (resp/redirect "/dashboard")
    (layout/render
      "home.html" {:content (util/md->html "/md/docs.md")
                   :flash-message (session/flash-get :msg)})))


(defn about-page []
  (layout/render "about.html"))


(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
