#lang racket
(#%provide (all-defined))

(#%require rackunit)
(#%require "hw01-tests.rkt")
(#%require "test-infrastructure.rkt")

(define marking-string "failed tests: ")

(define score (list 30 30 30))

(define (grade)
  (define results 
    (list (test part-one-test-suite)
          (test 18-series-a-test-suite)
          (test 18-series-b-test-suite)))
  
  (print marking-string) (print results)
  )

(grade)
  