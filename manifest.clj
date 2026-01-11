(ns manifest)

(defn manifest! []
  ;; 1. git add, git commit, git push
  ;; 2. lag manifest
  ;; 3. send manifest til https://mikrobloggeriet.no

  ;; Manifestet er content ID-er på alt innholdet.

  ;; Vi *kan* regne ut hasher av hver bit innhold, men det trenger vi egentlit ikke?
  ;; Jeg tror vi kan klare oss med Commit SHA, og deretter "dytte opp" den Commit SHA-en til Mikrobloggeriet.
  ;; Og bare lese filene via HTTPS til Github med Commit SHA
  )
