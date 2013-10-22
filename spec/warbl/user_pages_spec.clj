(ns warbl.user-pages-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [clj-webdriver.taxi :as t]
            [warbl.spec-helpers :as util]))


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


(describe "registration page"
  (before-all (util/start-browser)
              (t/to (str util/site-root "/register")))
  (after-all  (util/stop-browser))

  (it "should have heading"
      (should-contain "Register A New Account"
                      (t/text "#main-content")))

  (it "should have form labels"
      (should-contain "User ID" (t/text "label[for=id]"))
      (should-contain "Password" (t/text "label[for=pass]"))
      (should-contain "Retype Password" (t/text "label[for=pass1]"))))
