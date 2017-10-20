(ns gloo-resources.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [gloo-resources.core-test]))

(doo-tests 'gloo-resources.core-test)
