(ns warbl.helpers.routes
  (:require [noir.session :as session]
            [noir.response :as resp]))


(defn kick-to-root
  ([]
    (kick-to-root "You can't do that"))
  ([msg]
    (do (session/flash-put! :flash-info
                            (str msg))
        (resp/redirect "/"))))
