#lang racket
(#%provide (all-defined))

(#%require rackunit)
(#%require "util-lib.rkt")
(#%require "test-infrastructure.rkt")

;if you rename the answer sheet to conform with the submission requirements
;you will have to change this line so that the tests can find the 
(#%require "hw01-answer-sheet.rkt")

;to run the tests, evaluate:
;>(test-all)

(define (test-all)
  ;or any of these seperately
  (test part-one-test-suite)
  (test 18-series-a-test-suite)
  (test 18-series-b-test-suite)
  )

(define part-one-test-suite
  (test-suite
   "First part of the homework"
   
   (test-case
    "p1, ((3 + 3) * 9)"
    (check-equal? (p1-1) 54 )
    )
   
   (test-case
    "p1, ((6 * 9) / ((4 + 2) + (4 * 3)))"
    (check-equal? (p1-2) 3 )
    )
   
   (test-case
    "p1, (2* ((20 - (91 / 7)) * (45 - 42)))"
    (check-equal? (p1-3) 42 )
    )
   
   (test-case
    "p2, written answer needs to be a string"
    (check-true (string? p2))
    )
   
   ;;no test case for problem 3.
   
   (test-case
    "p4, sum of the two largest x,y,z"
    (check-equal? (p4) 7)
    )
   
   (test-case
    "p5, sum of the two smallest x,y,z"
    (check-equal? (p5) 5)
    )
   
   (test-case
    "p6, x, y are not equal"
    (check-equal? (p6) #f)
    )
   
   (test-case
    "p7, written answer needs to be a string"
    (check-true (string? p7))
    )
   
   (test-case
    "p8, written answer needs to be a string"
    (check-true (string? p8))
    )
   
   (test-case
    "p9, written answer needs to be a string"
    (check-true (string? p9))
    )
   
   (test-case
    "p10, written answer needs to be a string"
    (check-true (string? p10))
    )
   
   (test-case
    "p10, written answer needs to be a string"
    (check-true (string? p10))
    )
   
   ;no tests for 11 because writting the tests would give away the answers
   
   (test-case
    "12-1, looking for '(d a b c)"
    (check-equal? (p12-1 example) '(d a b c))
    )
   
   (test-case
    "12-2, looking for '(a b d a b)"
    (check-equal? (p12-2 example) '(a b d a b))
    )
   
   (test-case
    "12-2, looking for '(b c d a)"
    (check-equal? (p12-3 example) '(b c d a))
    )
   
   (test-case
    "p13, written answer needs to be a string."
    (check-true (string? p10))
    )
   
   (test-case
    "p14. create-error-msg function"
    
    (check-equal? (create-error-msg 'forty-two 10)
                  "This is a custom message. Symbol 'forty-two was paired with value 10 instead of 42")
    
    (check-equal? (create-error-msg 'answer-to-everything 13)
                  "This is a custom message. Symbol 'answer-to-everything was paired with value 13 instead of 42")
    )
   
   )
  )
;=============================================================================
(define 17-list-of-even-numbers-test-suite
  (test-suite
   "list-of-even-numbers?"
   
   (test-case
    "test case 1. normal input"
    (check-true (list-of-even-numbers? '(4 20 16 2)))
    )
   
   (test-case
    "test case 2. list containing an odd number"
    (check-false (list-of-even-numbers? '(4 1 16 2)))
    )
   
   (test-case
    "test case 3. list containing not a number, string"
    (check-false (list-of-even-numbers? '(4 2 16 "two")))
    )
   
   (test-case
    "test case 4. list containing not a number, symbol"
    (check-false (list-of-even-numbers? '(4 2 16 'two)))
    )
   
   (test-case
    "test case 5. list containing not a number, list"
    (check-false (list-of-even-numbers? '(4 1 16 '(2 4))))
    )
   
   (test-case
    "test case 6. input is not a list"
    (check-false (list-of-even-numbers? 2))
    )
   )
  )

;=============================================================================
(define 18-series-a-test-suite
  (test-suite
   "Sn = 1/1 + 1/4 + 1/9 + 1/16 + ..."
   
   ;;you do not have to account for the case n=0
   (test-case
    "test 1"
    (check-equal? (series-a 1) 1)
    )
   
   (test-case
    "test 2"
    (check-equal? (series-a 2) (/ 5 4))
    )
   
   (test-case
    "test 3"
    (check-equal? (series-a 3) (/ 49 36))
    )
   
   (test-case
    "test 4" 
    (check-equal? (series-a 4) (/ 205 144)) 
    )
   )
  )

;=============================================================================
(define 18-series-b-test-suite
  (test-suite
   "Sn = 1 - 1/2 + 1/6 - 1/24 + ..."
   
   (test-case
    "test 0"
    (check-equal? (series-b 0) 1) 
    )
   
   (test-case 
    "test 1"
    (check-equal? (series-b 1) (/ 1 2) ) 
    )
   
   (test-case
    "test 2"
    (check-equal? (series-b 2) (/ 2 3))
    )
   
   (test-case
    "test 3"
    (check-equal?(series-b 3)(/ 5 8))
    )
   
   (test-case 
    "test 4"
    (check-equal? (series-b 4) (/ 19 30) "test 4")
    )
   
   (test-case
    "test 5"
    (check-equal? (series-b 5) (/ 91 144))
    )
   )
  )