#lang racket
(#%require rackunit)
(#%require "hw01-tests.rkt")
(#%require "test-infrastructure.rkt")(define results (list
(test part-one-test-suite)
(test 18-series-a-test-suite)
(test 18-series-b-test-suite)))
(print "results: ")
(print results)