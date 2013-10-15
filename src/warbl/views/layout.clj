(ns warbl.views.layout
  (:require [selmer.parser :as parser]
            [clojure.string :as s]
            [ring.util.response :refer [content-type response]]
            [noir.session :as session])
  (:import compojure.response.Renderable))

(def template-path "warbl/views/templates/")

(deftype
  RenderableTemplate
  [template params]
  Renderable
  (render
    [this request]
    (content-type
      (->>
        (assoc
          params
          (keyword (s/replace template #".html" "-selected"))
          "active"
          :servlet-context
          (:context request)
          :user-id
          (session/get :user-id)
          :flash-success (session/flash-get :flash-success)
          :flash-info    (session/flash-get :flash-info)
          :flash-warning (session/flash-get :flash-warning)
          :flash-danger  (session/flash-get :flash-danger))
        (parser/render-file (str template-path template))
        response)
      "text/html; charset=utf-8")))

(defn render [template & [params]]
  (RenderableTemplate. template params))

