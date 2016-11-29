;; Copyright (C) 2012-present, Polis Technology Inc. This program is free software: you can redistribute it and/or  modify it under the terms of the GNU Affero General Public License, version 3, as published by the Free Software Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details. You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.


(ns polismath.email
  (:require [polismath.env :as env]
            [clj-http.client :as client]))

(def ^:dynamic *mailgun-key* (:mailgun-api-key env/env))
(def ^:dynamic *mailgun-url* (:mailgun-url env/env))

(defn send-email!
  ([{:keys [from to subject text html] :as params}]
   (try
     (client/post *mailgun-url*
                  {:basic-auth ["api" *mailgun-key*]
                   :query-params params})
     (catch Exception e (.printStackTrace e))))
  ([from to subject text html] (send-email! {:from from :to to :subject subject :text text :html html}))
  ([from to subject text] (send-email! {:from from :to to :subject subject :text text})))

