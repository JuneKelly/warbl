(ns warbl.helpers.gravatar
  (:require
    [clavatar.core :refer [gravatar]]))


(defn gravatar-large [user]
  (gravatar (user :email)
                  :default :mm
                  :size 300))


(defn gravatar-small [user]
  (gravatar (user :email)
                  :default :mm
                  :size 100))


(defn add-small-gravatar [user]
  (assoc user :g-small (gravatar-small user)))


(defn add-small-gravatars [users]
  (map add-small-gravatar users))
