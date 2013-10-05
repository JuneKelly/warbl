(ns warbl.homepage-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [clj-webdriver.taxi :as t]
            [warbl.env :refer [config]]))


(def site-root (config :site-url))


(describe "homepage, with guest user"

  (before-all (t/set-driver! {:browser :firefox}))
  (after-all (t/quit))

  (before (t/to site-root))

  (it "should have warbl name in brand link"
      (should-contain "warbl" (t/text {:tag :a, :class "navbar-brand"})))

  (it "should have a welcome message somewhere on the page"
      (should-contain "Welcome to warbl" (t/text {:tag :body})))

  (it "should have a login button visible"
      (t/exists? "input.btn[value=Login]"))

  (it "should have a register link visible"
      (t/exists? "a[href='/register']")))
