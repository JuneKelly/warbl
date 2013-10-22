(ns warbl.env
  (require [environ.core :as environ]))


(def config environ/env)


(def db-url (config :db-url))
(def db-name (config :db-name))
(def site-url (config :site-url))
