#lang racket

(#%require rackunit)
(require rackunit)
(require rackunit/text-ui)

(define (test suite)
  (run-tests suite 'verbose)  
  )

(define (forty-two) 42)

(define all
  (test-suite
   "test-suite"
   
   (test-case
    "test case 1. that should run normally"
    (check-equal? (forty-two) 42)
    )
   
   (test-case
    "test case 2. that fails"
    (check-equal? (forty-two) 11)
    )
   )
  )

(test all)