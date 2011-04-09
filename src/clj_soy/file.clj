(ns clj-soy.file
  (:import java.io.File))

 
(defn- soy-children
  "Returns a seq of all a dir's child .soy files and dirs"
  [dir]
  (filter #(.endsWith (.getName %) ".soy") (file-seq dir)))

(defn add-file
  "Adds file(s) to a SoyFileSet$Builder, returns the builder"
  [builder file]
  (println "Adding file:" file)
  (cond
   (string? file) (add-file builder (File. file))
   (seq? file) (doseq [f file] (add-file builder f))
   (instance? File file) (if (.isDirectory file)
                           (add-file builder
                                     (soy-children file))
                           (.add builder file))
   :else (throw (IllegalArgumentException.)))
  builder)

