# cloud-itonami-iso3166-com

**COM**: Union of the Comoros.

- ARMP (Autorité de Régulation des Marchés Publics) e-procurement -- Comores Marchés Publics
- ANPI (guichet unique) company registration + RCCM (Tribunal de Commerce) + AGID tax registration; ARMP exclusion/prescription gate (Code des Marchés Publics 2022 Art.75 7°)

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as `cloud-itonami-iso3166-btn`/`-ben` (minus any country-specific
bridge):

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites the Autorité de
  Régulation des Marchés Publics (ARMP, Code des Marchés Publics 2022,
  Loi n°22-011/AU du 23 juin 2022 modifiant la loi n°11-027/AU du 29
  décembre 2011), the Direction Nationale de Contrôle des Marchés
  Publics (DNCMP), the Comores Marchés Publics e-procurement system
  (décret n°23-080/PR du 15 août 2023), ANPI (Agence Nationale pour la
  Promotion des Investissements, the guichet unique for company
  creation), the RCCM (issued by the Tribunal de Commerce under
  directly-applicable OHADA Uniform Acts -- Comoros IS a ratified OHADA
  member, see namespace docstring for the correction to this vertical's
  own initial scaffolding assumption) and AGID (Administration Générale
  des Impôts et des Domaines, Loi n°12-004/AU, tax + land/domain
  registration). `governor.cljc`'s flagship check independently
  recomputes whether an engagement's own declared ARMP exclusion
  decision has already prescribed under the Code's own five-year
  window (Art.75 7°).
- `src/statute/facts.cljc` -- general-law catalog: the OHADA Acte
  Uniforme révisé relatif au droit des sociétés commerciales et du GIE
  (2014, directly applicable, no domestic transposition act), the Code
  du Travail 2012 (Loi n°12-12/AU), and the Code des Marchés Publics
  2022 itself (Loi n°22-011/AU).

Every citation is WebFetch/curl-verified against an official source
(armpcomores.km, finances.gouv.km, dgi.gouv.km, eregulations.investcomoros.net,
anpicomores.com, ohada.org, natlex.ilo.org); see each namespace's
docstring for the full research trail and any honestly-narrowed scope.

## Culture catalog

This repo carries a **country-level regional-culture catalog**
(ADR-2607171400 addendum 2, `cloud-itonami-municipality-culture-catalog`
Wave 1, in `com-junkawasaki/root`) — national dishes, protected products,
beverages, crafts, festivals and heritage sites for the Comoros:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring the fleet's `statute.facts` convention).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
