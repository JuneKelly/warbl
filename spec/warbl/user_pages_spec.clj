(ns warbl.user-pages-spec
  (:use [speclj.core]
        [clojure.test]
        [ring.mock.request]
        [warbl.handler])
  (:require [clj-webdriver.taxi :as t]
            [warbl.env :refer [config]]
            [warbl.test-util :as util]))


(def site-root (config :site-url))


(describe "user profile"
  (before-all
    (t/set-driver! {:browser :firefox})
    (util/drop-database!)
    (util/populate-users)
    (do (t/to site-root)
        (t/quick-fill-submit
          {"#id" "userone"}
          {"#pass" "password"}
          {"input.btn[value=Login]" t/click})))
  (after-all (t/quit))


  (it "should have basic labels"
      (t/to (str site-root "/profile"))
      (should-contain "User details for:" (t/text {:tag :body}))))
