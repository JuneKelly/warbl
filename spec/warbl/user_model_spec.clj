(ns warbl.user-pages-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [warbl.spec-helpers :as util]
            [warbl.spec-helpers :as env]
            [monger.collection :as mc]
            [monger.query :as mq]
            [warbl.models.db :as db]))


(defn setup-user []
  (util/drop-database!)
  (db/create-user "testuser" "fakepasswordhash"))


(describe "user creation"
  (before-all (setup-user))
  (after-all  (util/drop-database!))
          
  (it "should have expected fields set"
      (let [u (db/get-user "testuser")]
        (should-contain :_id u)
        (should-contain :password u)
        (should-contain :created u)
        (should (= "testuser" (u :_id)))
        (should (= java.util.Date (class (u :created)))))))
