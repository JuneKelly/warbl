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


(defn add-small-gravatar [u]
  (if (or (list? u) (vector? u) (seq? u))
    (map add-small-gravatar u)
    (assoc u :g-small (gravatar-small u))))


(defn add-large-gravatar [u]
  (if (or (list? u) (vector? u) (seq? u))
    (map add-large-gravatar u)
    (assoc u :g-large (gravatar-large u))))
