(ns monkey.oci.core
  "Core library for accessing the core services api"
  (:require [martian.core :as mc]
            [monkey.oci.common
             [martian :as cm]
             [pagination :as p]
             [utils :as cu]]
            [schema.core :as s]))

(def version "20160918")
(def json ["application/json"])

(def Id s/Str)

(defn api-route [opts]
  (assoc opts :produces json))

(s/defschema PrivateIpQuery
  {(s/optional-key :ipAddress) s/Str
   (s/optional-key :subnetId) Id
   (s/optional-key :vnicId) Id
   (s/optional-key :vlanId) Id})

(def routes
  [(p/paged-route
    (api-route
     {:route-name :list-private-ips
      :method :get
      :path-parts ["privateIps"]
      :query-schema PrivateIpQuery}))])

(defn make-client [conf]
  (cm/make-context conf (comp (partial format (str "https://iaas.%s.oraclecloud.com/" version)) :region) routes))

(cu/define-endpoints *ns* routes mc/response-for)
