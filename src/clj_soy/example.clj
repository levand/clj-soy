(ns clj-soy.example
  (:require [clj-soy.template :as soy]))

(defn example []
  (let [tpl (soy/build "templates/example.soy")]
    (soy/render tpl
                "clj_soy.example.helloWorld"
                {:name "Luke"
                 :greeting "Bonjour"})))

(defn example2 []
  (let [tpl (soy/build "templates")]
    (soy/render tpl
                "clj_soy.example.helloWorld"
                {:name "Luke"
                 :greeting "Bonjour"})))

