;; Copyright (C) 2012-present, Polis Technology Inc. This program is free software: you can redistribute it and/or  modify it under the terms of the GNU Affero General Public License, version 3, as published by the Free Software Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details. You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.


(ns polismath.metrics)


(defn make-socket 
	([] (new java.net.DatagramSocket))
	([port] (new java.net.DatagramSocket port)))


(defn send-data [send-socket ip port data]
  (let [ipaddress (java.net.InetAddress/getByName ip),
        send-packet (new java.net.DatagramPacket (.getBytes data) (.length data) ipaddress port)]
  (.send send-socket send-packet)))


(defn receive-data [receive-socket]
  (let [receive-data (byte-array 1024),
       receive-packet (new java.net.DatagramPacket receive-data 1024)]
  (.receive receive-socket receive-packet)
  (new java.lang.String (.getData receive-packet) 0 (.getLength receive-packet))))


(defn make-receive [receive-port]
	(let [receive-socket (make-socket receive-port)]
		(fn [] (receive-data receive-socket))))


(defn make-send [hostname port]
	(let [send-socket (make-socket)]
    (fn [data]
      (println "sending " data " to " hostname ":" port)
      (send-data send-socket hostname port data))))


(defn make-metric-sender [hostname port carbon-api-key]
  (let [dosend (make-send hostname port)]
    (fn
      ([name value optionalTimestampMillis]
         (let [s (str carbon-api-key "." name " " value " " (long (/ optionalTimestampMillis 1000)) "\n")]
           (println s)
           (dosend s)))
      ([name value]
         (let [s (str carbon-api-key "." name " " value "\n")]
           (println s)
           (dosend s))))))


