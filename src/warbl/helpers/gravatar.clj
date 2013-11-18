(ns warbl.helpers.gravatar
  (:require
    [clavatar.core :refer [gravatar]]))


(defn gravatar-large [email]
  (gravatar email
            :default :mm
            :size 300))


(defn gravatar-small [email]
  (gravatar email
            :default :mm
            :size 100))


(defn add-small-gravatar [u]
  (if (or (list? u) (vector? u) (seq? u))
    (map add-small-gravatar u)
    (assoc u :g-small (gravatar-small (u :email)))))


(defn add-large-gravatar [u]
  (if (or (list? u) (vector? u) (seq? u))
    (map add-large-gravatar u)
    (assoc u :g-large (gravatar-large (u :email)))))


(defn get-gravatars [email]
  {:small (gravatar-small email)
   :large (gravatar-large email)})
