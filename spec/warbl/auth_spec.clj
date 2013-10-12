(ns warbl.auth-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [clj-webdriver.taxi :as t]
            [warbl.env :refer [config]]
            [warbl.test-util :as util]))


(def site-root (config :site-url))


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
      (t/to site-root)
      (t/quick-fill-submit {"#id" "userone"}
                           {"#pass" "password"}
                           {"input.btn[value=Login]" t/click})
      (should-contain "userone" (t/text {:tag :body}))
      (should-contain "Dashboard" (t/text {:tag :body}))
      (should-contain "Logged in!!!" (t/text ".alert"))))


(describe "attempt login as invalid user"

  (before-all (t/set-driver! {:browser :firefox}))
  (after-all (t/quit))

  (it "should not log in"
      (t/to site-root)
      (t/quick-fill-submit {"#id" "not-a-user"}
                           {"#pass" "whatever"}
                           {"input.btn[value=Login]" t/click})
      (should-not-contain "not-a-user" (t/text {:tag :body}))
      (should-not-contain "Users" (t/text {:tag :body}))
      (should-not-contain "Feed" (t/text {:tag :body}))))
