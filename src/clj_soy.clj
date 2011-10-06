(ns clj-soy
  (:use clj-soy.file)
  (require [clojure.walk :as walk]
           [clojure.java.io :as jio])
  (:import com.google.template.soy.SoyFileSet
           com.google.template.soy.SoyFileSet$Builder
           com.google.template.soy.data.SoyMapData
           com.google.template.soy.tofu.SoyTofu))

(defn- prepare-data
  [data]
  (SoyMapData. (walk/stringify-keys data)))

(defn from-files [file & [globals]]
  "Takes an arguement coercable to File and returns a compiled Tofu object.

  If file is a directory, compile all .soy files beneath it. globals are used to
  set variables at compile time."
  (let [builder (SoyFileSet$Builder.)]
    (when globals (.setCompileTimeGlobals builder (walk/stringify-keys globals)))
    (doseq [f (filter #(.endsWith (str %) ".soy") (file-seq (jio/as-file file)))]
      (.add builder f))
    (.compileToJavaObj (.build builder))))

(defn- get-defined-soy-templates [ns]
  (map (fn [v] [(deref v) (:file (meta v))])
       (filter #(:soy (meta %)) (map second (ns-publics (symbol ns))))))

(defn from-namespaces
  "Takes a seq of namespaces as strings or symbols and returns a tofu object"
  [namespaces]
  (let [builder (SoyFileSet$Builder.)]
    (doseq [ns namespaces]
      (doseq [[template-txt filename] (get-defined-soy-templates ns)]
        (.add builder template-txt filename)))
    (.compileToJavaObj (.build builder))))

(defn render
  ([tofu template data] (render tofu template data nil))
  ([tofu template data msg-bundle]
     (let [data (if (map? data) (prepare-data data) data)]
       (.render tofu template data msg-bundle))))


