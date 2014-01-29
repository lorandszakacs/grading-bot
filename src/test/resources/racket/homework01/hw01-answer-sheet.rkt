#lang racket
(#%provide (all-defined))

#|
If there are any specific instructions for a problem, please read them carefully. Otherwise,
follow these general rules:
   - replace the 'UNIMPLEMENTED symbol with your solution
   - you are NOT allowed to change the names of any definition
   - you are NOT allowed to change the number of arguments of the pre-defined functions,
     but you can change the names of the arguments if you deem it necessary.
   - make sure that you submit an asnwer sheet that compiles! If you cannot write
     a correct solution at least make it compile, if you cannot make it compile then
     comment it out. In the latter case, make sure that the default definitions
     for the problem are still present. Otherwise you may incurr penalties to your
     score for this homework.
   - you may use any number of helper functions you deem necessary.
|#
;======================================01=======================================
;((3 + 3) * 9)
;equal to 54
;wrong on purpose
(define (p1-1)
  (* (+ 3 3) 3)
)

;((6 * 9) / ((4 + 2) + (4 * 3)))
;equal to 3
;wrong on purpose
(define (p1-2)
  (/ (* 6 9) (+ (+ 4 2) (* 4 5)))
)

;(2* ((20 - (91 / 7)) * (45 - 42)))
;equal to 42
(define (p1-3)
  (* 2 (* (- 20 (/ 91 7)) (- 45 42)))
)
;======================================02=======================================
;write your answer as a string; you do not need to write any special escape
;characters to distinguish new lines.
(define p2
  "Start writting the operators, from left to right, followed by their operands in the
   order they are evaluated.
  "
)
;======================================03=======================================
;;Write the definitions of x,y,z here:
(define x 2)
(define y 3)
(define z 4)
;======================================04=======================================
;you will need to have solved problem 3. The values x,y,z are not parameters
;of this function!
(define (p4)
  (if (= x y z)
      0
      (- (+ x y z) (min x y z))) 
)

;======================================05=======================================
(define (p5)
  (if (= x y z) 
      0
      (- (+ x y z) (max x y z)))
)

;======================================06=======================================
(define (p6)
  (= x y)  
)

;======================================07=======================================
;same instructions as problem 02.
(define p7
  "(define forty-two 42) binds the value 42 to the variable forty-two
   (define (forty-two) 42) creates a function called forty-two that
   returns the value 42.
 "
)

;======================================08=======================================
(define p8
  "Quote will return the literal symbol, number or list of whatever is after it, until it
   encounters a valid separator.
   For lists it is a closed parenthesis )
   > '(1 whatever is here)
    '(whatever is here)
   For numbers and literal symbols it is usually a space:
   > (list '3 (+ 4 6))
      '(3 10)
  "
)

;======================================09=======================================
(define p9
  "The difference is that list will evaluate the expressions it encounters. Unlike
   list which would return its literal value.
   > (list '3 (+ 4 6))
      '(3 10)
   > '('3 (+ 4 6))
     '('3 (+ 4 6))
  "
)

;======================================10=======================================
(define p10
  "Strings are mutable and are accompanied by several library functions that help
   build strings. Symbols are immutable and are 'interned', meaning that if two
   symbols contain the same characters they will behave like pointers that points
   to the same value. Consider the example:
   
   > (eq? (string #\a #\b) (string #\a #\b))
     #f
   > (eq? 'ab 'ab)
     #t

   Note: strings created using directly double quotes are also immutable.
   "
)

;======================================11=======================================
;(4 2 6 9)
;(4 2 6 9)
(define (p11-1)
  (list 4 2 6 9)  
)

;(spaceship
;  (name(serenity))
;  (class(firefly)))
(define (p11-2)
  '(spaceship
    (name (serenity))
    (class (firefly)))
)

;(2 * ((20 - (91 / 7)) * (45 - 42)))
(define (p11-3)
  (list '2 '* (list (list '20 '- (list '91 '/ '7)) '* (list '45 '- '42))) 
)

;======================================12=======================================
(define example '(a b c))

;(d a b c)
(define (p12-1 lst)
  (cons 'd lst)
)

;(a b d a b)
(define (p12-2 lst)
  (list (car lst)
        (cadr lst)
        'd
        (car lst)
        (cadr lst))
)

;(b c d a)
(define (p12-3 lst)
  (list
   (cadr lst)
   (caddr lst)
   'd
   (car lst))
)


;======================================13=======================================
(define p13
  "eq? is analogous to reference equality in Java, while equal? is analogous to the
   equals method of class Object.
   > (eq? (string #\a #\b) (string #\a #\b))
     #f
   > (equal? (string #\a #\b) (string #\a #\b))
     #t
  "
)

;======================================14=======================================
(define (create-error-msg sym val)
  (string-append "This is a custom message. Symbol '"
                 (symbol->string sym) " was paired with value " (number->string val) " instead of 42")
)
;======================================15=======================================
;No answer necessary
;======================================16=======================================
;;write two examples that fail *only* at runtime here:

(define (p16-example-1)
  ;calling a function with the wrong number of parameters
  (car)
)

(define (p16-example-2)
  ;type mismatches
  (+ 1 "a")
)

;======================================17=======================================
(define (list-of-even-numbers? lst)
  (and
     (list? lst) 
     (or 
       (null? lst)
       (and (and (number? (car lst)) (even? (car lst)))
            (list-of-even-numbers? (cdr lst)))
     )
  )
)
;======================================18=======================================
;;for n > 0
;Sn = 1/1 + 1/4 + 1/9 + 1/16 + ...
(define (series-a n)
  (if (= n 1)
      1
      (+ (/ 1 (expt n 2))
         (series-a (- n 1)))
  )
)

;====
;;for n >= 0
;Sn = 1 - 1/2 + 1/6 - 1/24 + ...
(define (series-b n)
  (if (= n 0)
      1
      (+ (/ (expt -1 n) (fact (+ n 1)))
         (series-b (- n 1)))
   )
)

;I've written factorial in this way to illustrate the concept of tail recursion.
;Make note of the differences between the structure in this implementation and the
; structure of `series-a`
(define (fact n)
  (define (helper n acc)
    (if (equal? n 0)
        acc
        (helper (- n 1) (* acc n))
        )
    )
  (helper n 1)
)