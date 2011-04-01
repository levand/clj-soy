(ns clj-soy.template
  (:import java.io.File
           com.google.template.soy.SoyFileSet
           com.google.template.soy.SoyFileSet$Builder
           com.google.template.soy.data.SoyMapData
           com.google.template.soy.tofu.SoyTofu))

(defn- soy-children
  "Returns a seq of all a dir's child .soy files and dirs"
  [builder dir]
  (filter (fn [file]
            (or (.isDirectory file)
                (.endsWith (.getName file) ".soy")))
          (.listFiles dir)))

(defn- add-file
  "Adds file(s) to a SoyFileSet$Builder, returns the builder"
  [builder file]
  (cond
   (string? file) (add-file builder (File. file))
   (seq? file) (doseq [f file] (add-file builder f))
   (instance? File file) (if (.isDirectory file)
                           (add-file builder
                                     (soy-children builder file))
                      (.add builder file))
   :else (throw (IllegalArgumentException.)))
  builder)

(defn build
  "Compiles Google Closure templates from the provided .soy file(s). Takes one or more strings, java File objects, or sequences of strings/Files. If a file refers to a directory, it will recursively load all child files with a .soy extension."
  ([file-or-seq] (let [builder (SoyFileSet$Builder.)]
                   (add-file builder file-or-seq)
                   (.compileToJavaObj (.build builder))))
  ([file-or-seq & more] (build (cons file-or-seq more))))

(declare stringify-map)
(defn- stringify-entry [[k v]]
  [(if (keyword? k) (name k) k)
   (if (map? v) (stringify-map v) v)])

(defn- stringify-map [m]
  (into {} (map stringify-entry m)))


(defn prepare-data
  "Compiles a Clojure map to a form that can be efficiently used by Google Closure templates. Converts keyword map keys to strings"
  [data]
  (SoyMapData. (stringify-map data)))

(defn render
  "Takes a precompiled Google Closure template (as created by the 'build' function), a template name, a data object, and an optional SoyMsgBundle object (for internationalization). Returns the string result of applying the template to the data. The data may be either a map or a prepared Google Closure data object as returned by 'prepare-data'. 'prepare-data' is called internally on any provided maps, so if data is to be used more than once, preparing the data once beforehand can give substantial performance improvments."
  ([template name data] (render template name data nil))
  ([template name data msg-bundle]
     (let [data (if (map? data) (prepare-data data) data)]
       (.render template name data msg-bundle))))
