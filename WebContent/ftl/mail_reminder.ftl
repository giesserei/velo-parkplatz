<#ftl encoding="UTF-8">
*** Dies ist ein automatisch erstelltes E-Mail ***

Liebe Bewohnerin, lieber Bewohner der Giesserei

Die Miete für deinen Velo-Parkplatz ist noch nicht auf unserem Vereinskonto eingegangen:


  Name des Mieters: ${reservation.mieterPerson.nameUndWohnung!""}
  Parkplatznummer und -Typ: ${reservation.stellplatz.nummer} / ${typ}
  Mietbeginn: ${reservation.beginnDatum?string("dd.MM.yyyy")}
  Mietende: ${reservation.endDatum?string("dd.MM.yyyy")}


Bitte überweise den Mietpreis in Höhe von ${kosten?string.currency} innerhalb von 10 Tagen auf unser Vereinskonto:

  Hausverein Giesserei
  8404 Winterthur
  IBAN: CH61 0900 0000 8533 4587 8

Einzahlungsscheine befinden sich vor der Wohnung 2412 (auf dem Schuhschrank, Haus 1, 4. Obergeschoss).


Bitte beachte:
----------------
1. Hast du den Betrag bereits überwiesen, musst du nichts weiter unternehmen.

2. Nach Ablauf der oben genannten Zahlungsfrist wird die Reservation annulliert. 


------------------------------------------------------------------------------------------------
Parkplatzliste: http://velo.giesserei-gesewo.ch:8080/velo/liste

