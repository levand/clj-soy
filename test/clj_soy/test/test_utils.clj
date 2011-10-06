(ns clj-soy.test.test-utils)

(defn ns-clean []
  (map (fn [v] ns-unmap *ns* v) (keys (ns-interns *ns*))))

(def ^{:soy true} test-soy (slurp "./test/unit_test.soy"))

