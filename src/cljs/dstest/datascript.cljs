(ns datest.datascript
  (:require 
   [datascript.core :as ds]))

;; testing datasccript schemas adding and updates

(def schema {
             :firstnames {:db/cardinality :db.cardinality/many}
             :aka {:db/cardinality :db.cardinality/many}
             :paintings {:db/cardinality :db.cardinality/many}
             }
  )

(def conn (ds/create-conn schema))

(def artists [
              {:db/id -1
               :lastname "Jackson"
               :firstnames  ["Alexander", "Young"]
               :birthdate   "1882-10-03"
               :deathdate "1974-04-05"
               :aka   ["A.Y. Jackson", "Alexander Young Jackson"]
               :paintings []
               }
              {:db/id -2
               :lastname "Morris"
               :firstnames  ["Kathleen", "Moir"]
               :birthdate   "1893-12-02"
               :deathdate "1986-12-20"
               :aka   ["Kathleen Moir Morris", "Kathleen Kay Moir Morris"]
               :paintings []
               }
              {:db/id -3
               :lastname "Robinson"
               :firstnames  ["Albert", "Henry"]
               :birthdate   "1893-12-02"
               :deathdate "1986-12-20"
               :aka   ["A.H. Robinson"]
               :paintings []}
              ]
  )

(def goelettes [
                {:db/id -3000
                 :title "Goélettes in the ice, Baie St. Paul, 1927"
                 :creationyear "1927"
                 :medium "oil on canvas"
                 :size-height-inches 27
                 :size-width-inches 33
                 }
                ]
  )

(comment 
  
  ;; add the artists to the db
  (ds/transact! conn artists) ()
  
  ;; add the painting goelettes in the ice to A.H Robinson
  (ds/transact! conn [[:db/add 5 :paintings goelettes]])
  
  ;; Find all paintings by A.H. Robinson
  (ds/q '[:find ?p
          :where [?e :aka "A.H. Robinson"]
          [?e :lastname ?n]
          [?e :birthdate  ?a]
          [?e :paintings ?p]]
        @conn)
  
  (ds/q '[:find  ?n ?a
          :where [?e :aka "Kathleen Moir Morris"]
          [?e :lastname ?n]
          [?e :birthdate  ?a] ]
        @conn)
  
  )