(ns foo.bar)


;; hiccup way
(defn app-layout [title entries]
  (html [:html
         [:head [:title title]]
         [:body (for [entry entries]
                  [:div#entry (gen-content entry)])]]))


(defn app-layout [title entires]
  (render baz {:title title :entries entries}))

;;creates template foo.bar.baz
(t/htmltemplate baz
  [:html
   [:head [:title ??]]
   [:body (t/foreach
           [:div#entry (qux)]
           [:div#entry (qux {:aaa "aaa" :bbb "bbb"})])]])

(t/deftemplate baz
  "<html>"
  "<head><title>" ?? "</title></head>"
  "<body>"
  (t/foreach
   "<div id='entry>" (qux) "</div>")
  "</body></html>")



{:author {:firstname "Luke" :lastname "V."}
 :entries [{:title "foo"
            :content "bar"}]}


(t/deftemplate baz
  "<html>"
  "<head><title>" $author.firstname "</title></head>"  
  "<body>"
  (t/foreach [entry entries]
             "<div id='entry>" (qux) "</div>")
  "</body></html>")

(def ^{:soy true} baz "{namespace foo.bar.baz} {template ...}")


(t/foreach [entry entries]
           "<div>" (qux) "</div>"
           (t/ifempty
            "<div>No posts!</div>"))

(t/switch $numMarbles
          (t/case 0) "You have no marbles."
          (t/case 1 2 3) "You have a normal number of marbles."
          (t/default) "You have lots of marbles")

(t/switch $numMarbles
          (t/case [0] "You have no marbles.") 
          (t/case [1 2 3] "You have a normal number of marbles.") 
          (t/default "You have lots of marbles"))

(t/if ???
      $pi " is a good approximation of pi."
      (t/elseif ???) $pi " is a bad approximation of pi.")

(t/if ???
      $pi " is a good approximation of pi."
      (t/elseif ??? $pi " is a bad approximation of pi."))



;; 1
(t/if "round($pi, 2) == 3.14"
      $pi " is a good approximation of pi."
      (t/elseif ??? $pi " is a bad approximation of pi."))

;; 2
(t/if ($ round($pi, 2)  == 3.14 )
      $pi " is a good approximation of pi."
      (t/elseif ??? $pi " is a bad approximation of pi."))

;; 3
(t/if ($ (== (round $pi 2) 3.14))
      $pi " is a good approximation of pi."
      (t/elseif ??? $pi " is a bad approximation of pi."))

;; 4
(t/if '((round $pi 2) == 3.14)
      $pi " is a good approximation of pi."
      (t/elseif ??? $pi " is a bad approximation of pi."))

;; 5
(t/if '(== (round $pi 2) 3.14)
      $pi " is a good approximation of pi."
      (t/elseif ??? $pi " is a bad approximation of pi."))


(t/deftemplate baz
  "<html>"
  "<head><title>" $author.firstname "</title></head>"  
  "<body>"
  (t/foreach [entry entries]
             "<div id='entry>" (qux ) "</div>")
  "</body></html>")


;; 1
(qux)
(qux $author)
(qux $author {:foo 1 :bar 2})
(qux :all {:foo 1 :bar 2})

;; 2
(t/% qux)
(t/% qux $author)
(t/% qux $author {:foo 1 :bar 2})
(t/% qux :all {:foo 1 :bar 2})

;; 3
(t/template qux)
(t/template qux $author)
(t/template foo.bar.qux)




(ns bees
  (:use foo.bar))

(deftemplate )
(baz)