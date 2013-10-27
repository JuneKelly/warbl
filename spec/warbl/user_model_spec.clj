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


(describe "user creation"
  (before-all (util/drop-database!)
              (db/create-user "testuser" "fakepasswordhash"))
  (after-all  (util/drop-database!))
          
  (it "should have expected fields set"
      (let [u (db/get-user "testuser")]
        (should-contain :_id u))))
