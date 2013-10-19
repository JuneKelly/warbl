(ns warbl.user-pages-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [clj-webdriver.taxi :as t]
            [warbl.test-util :as util]))


(describe "user profile"
  (before-all
    (util/start-browser)
    (util/drop-database!)
    (util/populate-users)
    (do (t/to util/site-root)
        (t/quick-fill-submit
          {"#id" "userone"}
          {"#pass" "password"}
          {"input.btn[value=Login]" t/click})))
  (after-all (util/stop-browser))


  (it "should have basic labels"
      (t/to (str util/site-root "/profile"))
      (should-contain "User details for:" (t/text "#main-content"))))
