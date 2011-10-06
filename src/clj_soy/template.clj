(ns clj-soy.template
  (:require [clojure.walk :as walk]
            [clojure.string :as str])
  (:use [clj-soy.utils :only (dbg) :as u]))

(defprotocol SoyString
  "A string representing a Soy template"
  (toStr [s]))

(defn- soystring
  "Helper method for building a SoyString instance"
  [& components]
  (reify
   SoyString (toStr [this] (apply str components))
   Object (toString [this] (apply str components))))


(defn- escape-string
  "Escapes a string so it will always be literal in output. It shouldn't be possible for users to manually or accidentally write raw soy"
  [s]
  (str/escape (str s) {\space "{sp}"
                       \newline "{\n}"
                       \return "{\r}"
                       \tab "{\t}"
                       \{ "{lb}"
                       \} "{rb}"}))

(defn escape
  "Takes a seq and escapes each member (unless it is a SoyString), concatnating the results"
  [content]
  (reduce str (map (fn [cont] (if (satisfies? SoyString cont)
                                cont
                                (escape-string cont)))
                   content)))

(defn- implicit-print?
  "Checks if a form should be translated to an implicit print directive"
  [form]
  (and (symbol? form) (.startsWith (name form) "$")))

(defn leading-space?
  "Conditionally adds a leading space"
  [attr-string]
  (if (empty? attr-string) attr-string (str " " attr-string)))

(defn map-to-attributes
  "Converts a map to a string of key='value' statements"
  [attrs]
  (str/join \space (map (fn [[k v]]
                           (str k "=" "\"" v "\""))
                         (walk/stringify-keys attrs))))

(defn render-namespace
  "Renders a Google Templates 'namespace' element."
  [ns]
  (soystring "\n{namespace " ns "}"))

(defn render-template
  "Renders a Google Template 'template' element."
  [name options & content]
  (let [opts (if (map? options) options {})
        cont (if (map? options) content (cons options content))]
    (soystring "\n" "{template ." name (leading-space? (map-to-attributes  opts)) "}"
               "\n" (escape cont)
               "\n" "{/template}")))

(defmacro template->
  "A Google Template 'template' element"
  [template-name & forms]
  `(render-template
    ~(name template-name)
    ~@(map (fn [f] (if (implicit-print? f)
                     `(render-print ~(name f) ~#{})
                     f))
           forms)))

(defn render-literal
  "Renders a Google Template 'literal' element."
  [content])

(defn render-print
  "Renders a Google Template 'print' element."
  [expression directives])

(defn render-msg
  "Renders a Google Template 'msg' element."
  [description meaning content])

(defn render-if
  "Renders a Google Template 'if' element."
  [expression])

(defn render-elseif
  "Renders a Google Template 'elseif' element."
  [expression])

(defn render-else
  "Renders a Google Template 'else' element."
  [])

(defn render-switch
  "Renders a Google Template 'switch' element."
  [expression])

(defn render-case
  "Renders a Google Template 'case' element."
  [expression-list])

(defn render-default
  "Renders a Google Template 'default' element."
  [])

(defn render-foreach
  "Renders a Google Template 'foreach' element."
  [var-name data-expr])

(defn render-ifempty
  "Renders a Google Template 'ifempty' element."
  [])

(defn render-for
  "Renders a Google Template 'for' element."
  [var-name expr1 expr2 expr3])

(defn render-call
  "Renders a Google Template 'call' element."
  [template-name data])

(defn render-param
  "Renders a Google Template 'param' element."
  [key expr])

(defn render-css
  "Renders a Google Template 'css' element."
  [command-text])

(defmacro deftemplate [template-name & forms]
  `(def ~(with-meta template-name {:soy true})
        (str
         (render-namespace ~(name (ns-name *ns*)))
         (render-template ~(str template-name) ~@forms))))

