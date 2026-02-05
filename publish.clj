(ns publish
  (:require [babashka.fs :as fs]
            [babashka.http-client :as http-client]))

(def ^{:dynamic true} *mikrobloggeriet-root*
  "https://mikrobloggeriet.no")

(defn load-cohort-docs [cohort]
  (->> (fs/list-dir (:cohort/root cohort))
       (keep (fn [doc-dir]
               (when (and (fs/exists? (fs/file doc-dir "index.md"))
                          (fs/exists? (fs/file doc-dir "meta.edn")))
                 {:doc/slug (fs/file-name doc-dir)})))
       (sort-by :doc/slug)))

(defn mikrobloggeriet-edn->request [{:keys [cohorts]}]
  {:method :post
   :uri (str *mikrobloggeriet-root* "/fjernkohortene/docs")
   :body (pr-str {:docs (mapcat load-cohort-docs cohorts)})})

(comment

  (def mikrobloggeriet-edn (read-string (slurp "mikrobloggeriet.edn")))

  ;; POST https://mikrobloggeriet.no/fjernkohortene/docs
  (binding [*mikrobloggeriet-root* "http://localhost:7777"]
    (http-client/request (mikrobloggeriet-edn->request mikrobloggeriet-edn)))

  ;; POST http://localhost:7777/fjernkohortene/docs
  (http-client/request (mikrobloggeriet-edn->request mikrobloggeriet-edn))

  )
