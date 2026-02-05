(ns user
  (:import (java.lang System)))

(when-not (System/getProperty "babashka.version")
  ;; clojure+ printers are JVM only
  ((requiring-resolve 'clojure+.print/install!)))
