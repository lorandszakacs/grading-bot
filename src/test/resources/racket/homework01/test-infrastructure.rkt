#lang racket
(#%provide (all-defined))
;===============================================================================
;============================test infrastructure================================
;===============================================================================
(require rackunit)
(require rackunit/text-ui)

(define (test suite)
  (run-tests suite 'verbose)  
  )

(define-syntax 342-check-exn
  (syntax-rules ()
    [ (342-check-exn expression exn-msg)
      (check-equal? 
       (with-handlers ([string? (lambda (err-msg) err-msg)]) 
         expression)
       exn-msg)
      ]
    )
  )