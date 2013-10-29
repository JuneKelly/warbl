(ns warbl.user-pages-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [clj-webdriver.taxi :as t]
            [warbl.spec-helpers :as util]
            [warbl.models.db :as db]))


(describe "user profile"
  (before-all
    (util/start-browser)
    (util/drop-database!)
    (util/populate-users)
    (util/login-userone))
  (after-all (util/stop-browser))

  (it "should have basic labels"
      (t/to (str util/site-root "/profile"))
      (should-contain "userone" (t/text "h2#username"))))


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
      (should-contain "Retype Password" (t/text "label[for=pass1]")))

  (it "should have form inputs"
      (should (t/exists? "input[name=id]"))
      (should (t/exists? "input[name=pass]"))
      (should (t/exists? "input[name=pass1]"))
      (should (t/exists? "input.btn[id=create-account]"))
      (should-contain "Create Account!"
                       (t/attribute "input.btn[id=create-account]"
                                    :value))))


(describe "registration process with valid inputs"
  (before-all (util/start-browser)
              (t/to (str util/site-root "/register"))
              (do (t/quick-fill-submit
                    {"#id" "userthree"}
                    {"#pass" "password"}
                    {"#pass1" "password"}
                    {"input.btn[id=create-account]" t/click})))
  (after-all  (util/stop-browser))

  (it "should take the user to their dashboard"
    (should-contain "Dashboard" (t/text "#main-content")))

  (it "should show a confirmation message"
    (should-contain "Account Created!" (t/text "div.alert")))

  (it "should have the user logged in"
    (should-contain "userthree" (t/text "#main-menu")))

  (it "should have created a new user"
    (let [new-u (db/get-user "userthree")]
      (should-not-be-nil new-u)
      (should (= (new-u :_id) "userthree")))))


(describe "registration process with existing username"
  (before-all (util/drop-database!)
              (util/populate-users)
              (util/start-browser)
              (t/to (str util/site-root "/register"))
              (do (t/quick-fill-submit
                    {"#id" "userone"}
                    {"#pass" "password"}
                    {"#pass1" "password"}
                    {"input.btn[id=create-account]" t/click})))
  (after-all  (util/stop-browser))

  (it "should redirect to the registration page"
    (should-not-contain "Dashboard" (t/text "#main-content"))
    (should-contain "Register A New Account" (t/text "#main-content")))

  (it "should show a warning"
    (should-contain "That username is not available" (t/text "#main-content"))))
