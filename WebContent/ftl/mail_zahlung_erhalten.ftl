<#ftl encoding="UTF-8">
*** Dies ist ein automatisch erstelltes E-Mail ***

Liebe Bewohnerin, lieber Bewohner der Giesserei

Die Miete für deinen Velo-Parkplatz ist auf unserem Vereinskonto eingegangen.

  Name des Mieters: ${reservation.mieterPerson.nameUndWohnung!""}
  Parkplatznummer und -Typ: ${reservation.stellplatz.nummer} / ${typ}
  Mietbeginn: ${reservation.beginnDatum?string("dd.MM.yyyy")}
  Mietende: ${reservation.endDatum?string("dd.MM.yyyy")}
  Mietpreis: ${kosten?string.currency}

------------------------------------------------------------------------------------------------
Parkplatzliste: http://velo.giesserei-gesewo.ch:8080/velo/liste

