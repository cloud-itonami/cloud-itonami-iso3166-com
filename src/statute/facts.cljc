(ns statute.facts
  "General-law compliance catalog for the Union of the Comoros (COM) --
  extends this repo's existing `marketentry.facts` (public-procurement
  market-entry only, narrow scope) with a second, orthogonal catalog of
  statutes a company operating in this jurisdiction must generally
  track for compliance. Mirrors cloud-itonami-iso3166-aze/-bih/-jpn/
  -deu/-bgr/-blr/-bol/-blz/-atg/-brb/-brn/-btn's `statute.facts`
  (ADR-2607141700, cloud-itonami-compliance-fact-federation).

  Every entry below cites an OFFICIAL source, WebFetch/curl-verified
  2026-07-22, and every citation reflects PRIMARY TEXT this iteration
  actually fetched (`curl` + `pdftotext -layout`) and read in full, not
  a secondary summary:

  - **The OHADA Acte Uniforme révisé relatif au droit des sociétés
    commerciales et du groupement d'intérêt économique (2014)** -- this
    iteration's `marketentry.facts` namespace docstring documents, in
    full, why Comoros IS a ratified OHADA member state (OHADA's own
    'Les Etats membres de l'OHADA' page, fetched directly, lists
    'Comores'; ratified 20 October 1995, entry into force 3 December
    1996) -- a correction to this vertical's own initial scaffolding
    brief, not an assumption carried over from a sibling. This
    iteration downloaded the Uniform Act's own primary PDF text
    directly (served from `eregulations.investcomoros.net/media/...`,
    Comoros's own government investment-promotion platform, itself
    citing this exact Act -- by its own embedded PDF metadata title,
    originally sourced from droit-afrique.com) and read it via
    `pdftotext -layout`. HIGH confidence, primary text: Art.311 ('Sauf
    dispositions nationales contraires, le capital social doit être
    d'un million (1.000.000) de francs CFA au moins' -- SARL minimum
    capital) and Art.387 ('Le capital social minimum est fixé à dix
    millions (10.000.000) de francs CFA' -- SA minimum capital), both
    confirmed verbatim. This is the statute this repo's
    `marketentry.facts` `business-registration-spec-basis` cites for
    company incorporation, with the RCCM certificate itself issued by
    the Tribunal de Commerce (per Comoros's own ANPI e-regulations
    database, procedure 37 'Créer une société commerciale', read
    directly via its underlying REST API).
  - **The Code du Travail 2012** -- Loi n°12-12/AU, promulguée par le
    décret n°12-167/PR du 28 juin 2012, abrogeant, modifiant et
    complétant certaines dispositions de la loi N°84-108/PR portant
    Code du Travail (the original 1984 law). Downloaded in full as a
    PDF directly from the ILO's NATLEX legislative database
    (`natlex.ilo.org`, an intergovernmental primary-text archive) and
    read via `pdftotext -layout`. HIGH confidence, primary text: own
    title page, 'CODE DU TRAVAIL 2012', own citation line quoted above;
    own Article 1er: 'La présente loi est applicable aux relations
    entre employeurs et travailleurs exerçant leur activité
    professionnelle aux Comores'; own Article 1bis on workers'
    participation rights. This iteration did NOT independently fetch
    the original 1984 law's (Loi N°84-108/PR) own separate primary text
    (an honest, explicit gap -- the 2012 law's own citation line is
    this iteration's only source for the 1984 law's existence and
    number).
  - **The Code des Marchés Publics 2022** -- Loi n°22-011/AU du 23 juin
    2022, modifiant et complétant la loi n°11-027/AU du 29 décembre
    2011, portant passation des marchés publics et délégation des
    services publics, promulguée par le décret n°2022-085/PR du 13
    octobre 2022. Downloaded in full as a PDF directly from the
    Autorité de Régulation des Marchés Publics' (ARMP) own official
    site (`armpcomores.km`) and read via `pdftotext -layout` (86 pages).
    HIGH confidence, primary text: Art.19 ('Il est créé une Autorité de
    régulation des marchés publics dotée de la personnalité juridique
    et de l'autonomie administrative et financière... rattachée à la
    Présidence de l'Union des Comores'); Art.75 7° (the ARMP-exclusion
    ineligibility clause, 'sauf prescription de cinq ans'). This is the
    statute this repo's `marketentry.facts` `:legal-basis` AND
    `:armp-exclusion-legal-basis` cite -- the entire procurement-
    compliance vertical's `:owner-authority` chain (ARMP/DNCMP/
    Ministère des Finances) is grounded in this Act.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"COM"
   [{:statute/id "com.ohada-auscgie-2014"
     :statute/title "Acte Uniforme révisé relatif au droit des sociétés commerciales et du groupement d'intérêt économique (OHADA, 2014)"
     :statute/jurisdiction "COM"
     :statute/kind :law
     :statute/law-number "Art.311 (SARL minimum capital, 1 000 000 FCFA sauf dispositions nationales contraires); Art.387 (SA minimum capital, 10 000 000 FCFA); directly applicable OHADA Uniform Act -- Comoros ratified the OHADA treaty 20 October 1995, entry into force 3 December 1996 (OHADA's own member-states page, https://www.ohada.org/les-etats-membres-de-lohada/); RCCM certificate issued by the Tribunal de Commerce"
     :statute/url "https://eregulations.investcomoros.net/media/Ohada-Acte-Uniforme-2014-Societes-commerciales-GIE.pdf"
     :statute/url-provenance :anpi-government-platform
     :statute/enacted-date "2014-01-30"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "com.code-du-travail-2012"
     :statute/title "Code du Travail 2012"
     :statute/jurisdiction "COM"
     :statute/kind :law
     :statute/law-number "Loi n°12-12/AU, promulguée par le décret n°12-167/PR du 28 juin 2012, abrogeant, modifiant et complétant certaines dispositions de la loi N°84-108/PR portant Code du Travail; own Art.1er: applicable aux relations entre employeurs et travailleurs exerçant leur activité professionnelle aux Comores"
     :statute/url "https://natlex.ilo.org/dyn/natlex2/natlex2/files/download/91298/COM-91298.pdf"
     :statute/url-provenance :ilo-natlex
     :statute/enacted-date "2012-06-28"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:labor :employment}}
    {:statute/id "com.code-des-marches-publics-2022"
     :statute/title "Code des Marchés Publics 2022"
     :statute/jurisdiction "COM"
     :statute/kind :law
     :statute/law-number "Loi n°22-011/AU du 23 juin 2022, modifiant et complétant la loi n°11-027/AU du 29 décembre 2011, portant passation des marchés publics et délégation des services publics, promulguée par le décret n°2022-085/PR du 13 octobre 2022; own Art.19 (création de l'ARMP), Art.75 7° (exclusion, sauf prescription de cinq ans); administered by ARMP / DNCMP, Ministère des Finances, du Budget et du Secteur Bancaire"
     :statute/url "https://armpcomores.km/wp-content/uploads/2025/04/Comores-Code-2022-marches-publics.pdf"
     :statute/url-provenance :armp-official-site
     :statute/enacted-date "2022-06-23"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:public-finance :procurement}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-com statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "COM")) " COM statutes seeded with an "
                 "official citation. Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :procurement)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
