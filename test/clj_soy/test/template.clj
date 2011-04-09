(ns clj-soy.test.template
  (:use clojure.test)
  (:use clj-soy.template :reload))

; (map #(ns-unmap *ns* %) (keys (ns-interns *ns*)))

(deftest test-from-files
  (let [tofu (from-files "./test")]
    (is (= "Hello, Luke" (render tofu "clj_soy.unit_test.hello" {:name "Luke"})))))

(def ^{:soy true} test-soy (slurp "./test/unit_test.soy"))

(deftest test-from-namespaces
  (let [tofu (from-namespaces ["clj-soy.test.template"])]
    (is (= "Hello, Luke" (render tofu "clj_soy.unit_test.hello" {:name "Luke"})))))
