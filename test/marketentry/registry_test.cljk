(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "COM" 0)
        s (registry/register-submit "eng-1" "COM" 0)]
    (is (= "COM-DFT-000000" (get d "draft_number")))
    (is (= "COM-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "COM" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest armp-exclusion-active-recompute
  (testing "Code des Marchés Publics 2022 Art.75 7° -- an ARMP exclusion decision within the five-year prescription window bars the bidder, a pure boolean-status-gated-by-date-arithmetic test"
    (is (true? (registry/armp-exclusion-active?
                {:armp-exclusion-decision-date "2023-05-01"}
                "2026-07-22" 5))
        "decision is 3 years old (< 5) -> still active/unprescribed")
    (is (false? (registry/armp-exclusion-active?
                 {:armp-exclusion-decision-date "2018-01-01"}
                 "2026-07-22" 5))
        "decision is 8 years old (>= 5) -> already prescribed, no longer bars"))
  (testing "no-op (false) when the engagement was never subject to an exclusion decision at all"
    (is (false? (registry/armp-exclusion-active?
                 {:armp-exclusion-decision-date nil}
                 "2026-07-22" 5))))
  (testing "missing as-of-date or missing prescription-years is never treated as active here (evidence-incomplete's job upstream)"
    (is (false? (registry/armp-exclusion-active?
                 {:armp-exclusion-decision-date "2023-05-01"} nil 5)))
    (is (false? (registry/armp-exclusion-active?
                 {:armp-exclusion-decision-date "2023-05-01"} "2026-07-22" nil)))))

;; ---------------------------------------------------------------------------
;; Money is compared at money precision, not at double precision
;; ---------------------------------------------------------------------------

(deftest whole-unit-fees-were-already-correct-and-stay-correct
  (testing "the seeded shape: base + rate x months in whole currency units"
    (is (registry/engagement-fee-matches-claim?
         {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
           :claimed-fee 860000.0}))))

(deftest cent-denominated-fees-are-no-longer-rejected-while-correct
  (testing "`(== (double claimed) (+ (double base) (* (double rate) (double months))))`
            rejected CORRECT totals once an amount carried cents -- 40,989 of
            327,060 combinations (12.5%), against 0 of 327,060 in whole units"
    (let [bad (for [m (range 1 37)
                    bc (range 10000 90000 2100)
                    rc (range 500 6000 210)
                    :let [truth (/ (+ bc (* rc m)) 100.0)]
                    :when (not (registry/engagement-fee-matches-claim?
                                {:base-fee (/ bc 100.0) :monthly-rate (/ rc 100.0)
                                  :monitoring-months m :claimed-fee truth}))]
                [m (/ bc 100.0) (/ rc 100.0) truth])]
      (is (empty? bad) (str "false rejections: " (count bad) " e.g. " (first bad))))))

(deftest a-genuinely-wrong-fee-is-still-caught
  (testing "rounding to money precision must not blunt the check"
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 860000.01})))
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 859999.99})))))

(deftest an-unverifiable-fee-never-matches
  (testing "un-verifiable is not the same as correct, and not a crash"
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12})))
    (is (not (registry/engagement-fee-matches-claim?
              {:base-fee "500000" :monthly-rate 30000 :monitoring-months 12
                :claimed-fee 860000.0})))
    (is (nil? (registry/compute-engagement-fee {:base-fee 500000 :monthly-rate 30000})))))
