(ns manifest
  (:require [babashka.fs :as fs]
            [babashka.process :as p]
            [clojure.string :as str]))

(defn rev-parse [dir ref]
  (-> (p/shell {:out :string :dir dir} "git rev-parse" ref)
      :out str/trim))

(defn github-raw-href [rev path]
  (str "https://raw.githubusercontent.com/mikrobloggeriet/fjernkohortene/" rev "/" path))

(defn load-cohort-docs [rev cohort]
  (->> (fs/list-dir (:cohort/root cohort))
       (keep (fn [doc-dir]
               (when (and (fs/exists? (fs/file doc-dir "index.md"))
                          (fs/exists? (fs/file doc-dir "meta.edn")))
                 {:doc/cohort [:cohort/id (:cohort/id cohort)]
                  :slug (fs/file-name doc-dir)
                  :md {:href (github-raw-href rev (str (fs/file doc-dir "index.md")))}
                  :meta {:href (github-raw-href rev (str (fs/file doc-dir "meta.edn")))}})))))

(defn create-manifest []
  (set! *print-namespace-maps* false)
  (let [rev (rev-parse "." "HEAD")
        {:as manifest :keys [cohorts]} (read-string (slurp "mikrobloggeriet.edn"))
        docs (mapcat #(load-cohort-docs rev %) cohorts)]
    (assoc manifest
           :docs docs
           :rev rev)))

(defn prepare-request [the-manifest]
  {:method :post
   :uri "https://mikrobloggeriet.no/fjernkohortene"
   :body (pr-str the-manifest)})

(comment
  (require '[babashka.http-client :as http-client])
  (def manifest (create-manifest))
  (-> manifest
      prepare-request
      http-client/request)

  )
