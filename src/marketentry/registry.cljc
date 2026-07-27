(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `armp-exclusion-active?` is THIS vertical's own new ground-truth
  check, grounding COM's flagship governor check
  (`marketentry.governor/armp-exclusion-active-violations`): the Code
  des Marchés Publics 2022 (own primary text, see `marketentry.facts`)
  Art.75 7° bars a bidder subject to an unprescribed ARMP exclusion
  decision from public procurement, but that exclusion LAPSES after a
  five-year prescription window. This is a DIFFERENT check SHAPE from
  every prior sibling: not a turnover-scaled formula (Bulgaria), not a
  flat statutory threshold (Albania), not a boolean registry-membership
  read of the SUPPLIER with no time dimension (Azerbaijan/Armenia/
  Bolivia), not a 3-tier contract-value classification (Antigua and
  Barbuda), not a bid-evaluation price-adjustment recompute (Benin), not
  a struck-off company-registry legal-capacity boolean of the SUPPLIER
  (Belize), not a sector-eligibility set-membership test (Bhutan), and
  not a pure date-precedence/ordering check (Brunei) -- it is closest in
  general MECHANIC to Barbados's validity-window expiry-date recompute,
  but it is the MIRROR IMAGE of that shape: Barbados recomputes whether
  a POSITIVE credential (a registration) is still valid before its
  expiry date; this recomputes whether a NEGATIVE sanction (an
  exclusion) has already LAPSED after its own prescription window. The
  object decaying with time is a bar to participation, not a permission
  to participate -- a bidder with an unprescribed exclusion is BARRED
  (the check fires true = restricted), while a bidder whose exclusion
  has prescribed, or who was never excluded at all, has no bar at all
  (the check is a no-op, false). The five-year window itself is NEVER
  hardcoded here -- it is always passed in from `marketentry.facts`, the
  single source of truth, the same discipline `fdi-negative-list`-style
  siblings use for their own government-published parameters.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real ARMP, Tribunal de Commerce, AGID or Comores Marchés
  Publics system, and no wall-clock reads (`armp-exclusion-active?`
  takes the as-of year as an explicit argument so the recompute stays
  deterministic and portable across JVM/cljs/WASM). It builds the RECORD
  an operator would keep, not the act of submitting a real tender
  response itself (that is `marketentry.operation`'s `:filing/submit`,
  always human-gated -- see README Actuation)."
  (:require [clojure.string :as str]
            #?(:clj  [clojure.edn :as edn]
               :cljs [cljs.reader :as edn])))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(def ^:private money-scale
  "Sub-minor-unit scale used when comparing two money amounts: 1/10000 of
  a unit. Coarser than double representation error by many orders of
  magnitude, finer than any real currency's minor unit (2 decimals for
  most, 3 for KWD/BHD/OMR, 0 for JPY/KRW)."
  10000)

(defn- money=
  "Exact-at-money-precision equality for two amounts.

  `==` on raw doubles is NOT the right comparison for money. With
  whole-unit fees the two agree, but as soon as an amount carries
  cents the sum `base + rate x months` is routinely not the double
  nearest the true total, and a CORRECT claim compares false: measured
  on this exact shape, 40,989 of 327,060 cent-denominated combinations
  (12.5%) were rejected while being right, against 0 of 327,060 in
  whole units.

  Rounding both sides to `money-scale` before comparing removes the
  representation error while preserving every distinction money can
  actually carry."
  [x y]
  (and (number? x) (number? y)
       (= (Math/round (* money-scale (double x)))
          (Math/round (* money-scale (double y))))))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  ;; nil when any field is not a number: an un-recomputable engagement is
  ;; un-verifiable, which is neither `correct` nor a ClassCastException
  ;; thrown out of the caller.
  (when (and (number? base-fee) (number? monthly-rate) (number? monitoring-months))
    (+ (double base-fee)
       (* (double monthly-rate) (double monitoring-months)))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (money= claimed-fee (compute-engagement-fee engagement)))

(defn- year-of
  "Extract the integer year from an ISO-8601-prefixed date string
  ('YYYY' or 'YYYY-MM-DD'), via `edn/read-string` on the leading 4
  characters -- deterministic, no date-parsing library needed, portable
  across JVM/cljs (edn numeric syntax is identical on both)."
  [date-str]
  (edn/read-string (subs date-str 0 4)))

(defn armp-exclusion-active?
  "Is `engagement`'s own declared ARMP exclusion decision STILL legally
  binding as of `as-of-date` (i.e. NOT yet prescribed under Code des
  Marchés Publics 2022 Art.75 7°, 'sauf prescription de cinq ans')?

  `prescription-years` (the five-year window) is sourced from
  `marketentry.facts` -- NEVER hardcoded here, the same discipline
  every sibling's own government-published parameter uses.

  A no-op (false) when `:armp-exclusion-decision-date` is nil (the
  engagement was never the subject of an ARMP exclusion decision) --
  missing declared exclusion is never treated as excluded HERE (that is
  the `evidence-incomplete` check's job, upstream, where an assessment
  must already exist). Works at YEAR granularity: the Code's own text
  gives no finer-than-year grain for the prescription window ('cinq
  ans' only), an honest, explicitly-flagged simplification (see
  `marketentry.facts` namespace docstring)."
  [{:keys [armp-exclusion-decision-date]} as-of-date prescription-years]
  (boolean
   (when (and armp-exclusion-decision-date as-of-date prescription-years)
     (< (- (year-of as-of-date) (year-of armp-exclusion-decision-date))
        prescription-years))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a real tender-response
  package. Pure function -- does not touch any real ARMP, Tribunal de
  Commerce, AGID or Comores Marchés Publics system."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a real tender
  response through Comores Marchés Publics (always human-gated
  upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
