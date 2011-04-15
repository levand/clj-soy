(ns clj-soy.test.test-utils)

(defn ns-clean []
  (map (fn [v] ns-unmap *ns* v) (keys (ns-interns *ns*))))