(ns warbl.routes.auth
  (:use compojure.core)
  (:require [warbl.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [warbl.models.db :as db]
            [warbl.helpers.auth :as auth]))


(defn valid? [id pass pass1]
  (vali/rule (vali/has-value? id)
             [:id "user ID is required"])
  (vali/rule (vali/min-length? pass 5)
             [:pass "password must be at least 5 characters"])
  (vali/rule (= pass pass1)
             [:pass1 "entered passwords do not match"])
  (not (vali/errors? :id :pass :pass1)))


(defn register [& [id]]
  (layout/render
    "registration.html"
    {:id id
     :id-error (vali/on-error :id first)
     :pass-error (vali/on-error :pass first)
     :pass1-error (vali/on-error :pass1 first)}))


(defn handle-registration [id pass pass1]
  (if (valid? id pass pass1)
    (try
      (do
        (db/create-user id (crypt/encrypt pass))
        (auth/log-in id)
        (resp/redirect "/"))
      (catch Exception ex
        (vali/rule false [:id (.getMessage ex)])
        (register)))
    (register id)))


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
      (resp/redirect "/profile"))))


(defn handle-login [id pass]
  (if-let [user (db/get-user id)]
    (if (and user (crypt/compare pass (:password user)))
      (do
        (auth/log-in id)
        (session/flash-put! :msg "Logged in!!!")
        (resp/redirect "/dashboard"))
      (do
        (session/flash-put! :msg "Incorrect credentials")
        (resp/redirect "/")))
    (do
      (session/flash-put! :msg "Incorrect credentials")
      (resp/redirect "/"))))


(defn logout []
  (auth/log-out)
  (resp/redirect "/"))


(defroutes auth-routes
  (GET "/register" []
       (register))

  (POST "/register" [id pass pass1]
        (handle-registration id pass pass1))

  (GET "/profile" [] (profile))

  (POST "/update-profile" {params :params} (update-profile params))

  (POST "/login" [id pass]
        (handle-login id pass))

  (GET "/logout" []
        (logout)))
