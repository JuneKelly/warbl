(ns warbl.routes.messages
  (:use compojure.core)
  (:require [warbl.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [warbl.models.db :as db]
            [warbl.helpers.auth :as auth]
            [warbl.helpers.routes :refer [kick-to-root]]))


(defn create-message [to-user-id text]
  (comment "todo"))


(defn get-conversation [with-user-id]
  (comment "todo"))
