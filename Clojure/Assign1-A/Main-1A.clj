(def p1 '(and x (or x (and y (not z)))))
(def p2 '(and (and z false) (or x true)))
(def p3 '(or true a))

(defn andexp [& rest]
  (conj rest 'and))

(defn orexp [& rest]
  (conj rest 'or))

(defn notexp [e1] (list 'not e1))

(declare simplify-not)
(declare simplify-and)
(declare simplify-or)

(defn simplify [expr]
  (cond
    (not(seq? expr)) expr
    (= (first expr) 'not) (simplify-not expr)
    (= (first expr) 'and) (simplify-and expr)
    (= (first expr) 'or) (simplify-or expr)
    :default expr
    )
  )

(defn simplify-not [expr]
  (let [arg1 (second expr)]
    (cond
      (= arg1 false) true

      (= arg1 true) false

      (and (seq? arg1) (= (first arg1) 'not))
      (let [ simple (simplify arg1) ]
        (cond
          (= simple false) true
          (= simple true) false
          (and (seq? simple) (= (first simple) 'not)) (second simple)
          :default simple
          ))
      (and (seq? arg1) (= (first arg1) 'and))
      (simplify
        (concat (list 'or)
                (map #(list 'not %) (rest arg1)) ))
      (and (seq? arg1) (= (first arg1) 'or))
      (simplify
        (concat (list 'and)
                (map #(list 'not %) (rest arg1)) ))
      :default (list 'not arg1)
      )
    )
  )

(defn simplify-and [expr]
  (let [simple (map simplify (rest expr))]
    (let [arg1 (first simple)
          arg2 (if (empty? (rest (rest simple)))
                 (second simple)
                 (simplify (concat (list 'and) (rest simple))))]
      (cond
        (or (= arg1 false) (= arg2 false)) false
        (and (= arg1 true) (= arg2 nil)) true
        (= arg1 true) arg2
        (= arg2 true) arg1
        :default (concat (list 'and) (list arg1) (list arg2))
        )
      )
    )
  )

(defn simplify-or [expr]
  (let [simple (map simplify (rest expr))]
    (let [arg1 (first simple)
          arg2 (if (empty? (rest (rest simple)))
                 (second simple)
                 (simplify (concat (list 'or ) (rest simple))))]

      (cond
        (and (= arg1 false) (= arg2 false)) false
        (and (= arg1 true) (= arg2 nil)) true
        (and (= arg1 false) (= arg2 nil)) false
        (= arg1 true) true
        (= arg2 true) true
        (= arg1 false) arg2
        (= arg2 false) arg1
        :default (concat (list 'or) (list arg1) (list arg2))
        )
      )
    )
  )

(declare deep-substitute)
(defn evalexp [exp bindings] (simplify (deep-substitute exp bindings)))

(defn deep-substitute [l m]
  (map #(cond
          (seq? %) (deep-substitute % m)
          :default (get m % %))
       l))

