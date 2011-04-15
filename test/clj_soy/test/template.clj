(ns clj-soy.test.template
  (:use clojure.test
        [clj-soy.test.test-utils :only (ns-clean)])
  (:use clj-soy.template :reload))

(deftest t-render-template
  (is (= (str (render-template "foo" "{}"))
         "\n{template .foo}\n{lb}{rb}\n{/template}"))
  (is (= (str (render-template "bar" (render-template "foo" "{}")))
         "\n{template .bar}\n\n{template .foo}\n{lb}{rb}\n{/template}\n{/template}"))
  (testing "passing options"
    (let [rendered (str (render-template "bar" {:private true :autoescape false} "{}"))]
      (is (.contains rendered "private=\"true\""))
      (is (.contains rendered "autoescape=\"false\"")))))

(deftest t-template->
  (is (= (str (template-> foo {:foo 1 :bar 2} "<html>" $baz.qux "</html>"))
         "\n{template .foo foo=\"1\" bar=\"2\"}\n<html></html>\n{/template}"))
  (is (= (str (template-> foo "<html>" $baz.qux "</html>"))
         "\n{template .foo}\n<html></html>\n{/template}")))
