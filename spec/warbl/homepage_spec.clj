(ns warbl.homepage-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [clj-webdriver.taxi :as t]
            [warbl.test-util :as util]))


(describe "homepage, with guest user"

  (before-all (util/start-browser))
  (after-all (util/stop-browser))

  (before (t/to util/site-root))

  (it "should have warbl name in brand link"
      (should-contain "warbl" (t/text {:tag :a, :class "navbar-brand"})))

  (it "should have a welcome message somewhere on the page"
      (should-contain "Welcome to warbl" (t/text "#main-content")))

  (it "should have a login button visible"
      (t/exists? "input.btn[value=Login]"))

  (it "should have a register link visible"
      (t/exists? "a[href='/register']")))
