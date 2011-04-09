(ns clj-soy.template.macro)

(defn- deftemplate-helper
  "Does all the work of building a Google Template string"
  [forms]
  "test")

(defmacro deftemplate [name & forms]
  `(def ~(with-meta name {:soy true}) ~(deftemplate-helper forms)))