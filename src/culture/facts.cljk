(ns culture.facts
  "Country-level regional-culture catalog for the Comoros (COM) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). First facts namespace
  in this blueprint-stage repo; the marketentry/statute catalogs land with
  :implemented (ADR-2607141700). City-level counterparts live in the
  cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors the fleet's `statute.facts`
  convention); entries carry no :culture/municipality (that attribute is
  city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"COM"
   [{:culture/id "com.dish.langouste-a-la-vanille"
     :culture/name "Langouste à la vanille"
     :culture/country "COM"
     :culture/kind :dish
     :culture/summary "Small lobsters in vanilla sauce, a seafood dish of Comorian cuisine."
     :culture/url "https://en.wikipedia.org/wiki/Comorian_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "com.dish.mtsolola"
     :culture/name "M'tsolola"
     :culture/country "COM"
     :culture/kind :dish
     :culture/summary "Comorian dish of firm-fleshed fish such as swordfish cooked with plantains and greens such as cassava leaves in coconut milk."
     :culture/url "https://en.wikipedia.org/wiki/Comorian_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "com.dish.mshakiki"
     :culture/name "Mshakiki"
     :culture/country "COM"
     :culture/kind :dish
     :culture/summary "Marinated skewered beef, a meat dish of Comorian cuisine."
     :culture/url "https://en.wikipedia.org/wiki/Comorian_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "com.dish.mkatra-siniya"
     :culture/name "Mkatra siniya"
     :culture/country "COM"
     :culture/kind :dish
     :culture/summary "Comorian spice cake, a dessert of Comorian cuisine."
     :culture/url "https://en.wikipedia.org/wiki/Comorian_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "com.product.ylang-ylang"
     :culture/name "Ylang-ylang"
     :culture/country "COM"
     :culture/kind :product
     :culture/summary "Tree whose essential oil is a signature Comorian product; ylang-ylang essential oil made up 29% of the Comoros' annual export (1998)."
     :culture/url "https://en.wikipedia.org/wiki/Ylang-ylang"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "com.festival.grand-mariage"
     :culture/name "Grand mariage"
     :culture/country "COM"
     :culture/kind :festival
     :culture/summary "Fundamental cultural institution of the Comoros and the last customary rite of passage, celebrated with elaborate multi-day ceremonies, gift exchanges and family gatherings."
     :culture/url "https://fr.wikipedia.org/wiki/Grand_mariage"
     :culture/url-provenance :wikipedia-fr
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "com.heritage.mount-karthala"
     :culture/name "Mount Karthala"
     :culture/name-local "Karthala"
     :culture/country "COM"
     :culture/kind :heritage
     :culture/summary "Active shield volcano on Grande Comore in the Comoros, protected by Karthala National Park (designated 2010) and designated a Ramsar wetland site in 2006."
     :culture/url "https://en.wikipedia.org/wiki/Mount_Karthala"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-com culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "COM"))
                 " COM entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
