(ns warbl.auth-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [clj-webdriver.taxi :as t]
            [warbl.env :refer [config]]
            [warbl.test-util :as util]))


(describe "login form on homepage"

  (before-all (t/set-driver! {:browser :firefox}))
  (after-all (t/quit))

  (it "should have a login button visible"
      (t/exists? "input.btn[value=Login]"))

  (it "should have a register link visible"
      (t/exists? "a[href='/register']")))


(describe "login as existing user"

  (before-all (t/set-driver! {:browser :firefox}))
  (after-all (t/quit))

  (before (util/drop-database!)
          (util/populate-users))

  (it "should allow a user to log in"
      (util/login-userone)
      (should-contain "userone" (t/text "#main-menu"))
      (should-contain "Dashboard" (t/text "#main-content"))
      (should-contain "Logged in!!!" (t/text "div.alert"))
      (should (t/exists? "a[href='/logout']"))))


(describe "attempt login as invalid user"

  (before-all (t/set-driver! {:browser :firefox}))
  (after-all (t/quit))

  (it "should not log in"
      (t/to util/site-root)
      (t/quick-fill-submit {"#id" "not-a-user"}
                           {"#pass" "whatever"}
                           {"input.btn[value=Login]" t/click})
      (should-not-contain "not-a-user" (t/text {:tag :body}))
      (should-not-contain "Users" (t/text {:tag :body}))
      (should-not-contain "Feed" (t/text {:tag :body}))
      (should-contain "Incorrect credentials" (t/text "div.alert"))))


(describe "without logging in"

  (before-all (t/set-driver! {:broser :firefox}))
  (after-all (t/quit))

  (it "should not allow access to the dashboard page"
      (t/to (str util/site-root "/dashboard"))
      (should-not-contain "Dashboard"
                          (t/text {:tag :body}))
      (should-contain "You must be logged in"
                      (t/text "div.alert"))))
