<#ftl encoding="UTF-8">
*** Dies ist ein automatisch erstelltes E-Mail ***

Liebe Bewohnerin, lieber Bewohner der Giesserei

Die Miete für deinen Velo-Parkplatz wurde soeben verlängert.


  Name des Mieters: ${reservation.mieterPerson.nameUndWohnung!""}
  Parkplatznummer und -Typ: ${reservation.stellplatz.nummer} / ${typ}
  Neuer Mietbeginn: ${reservation.beginnDatum?string("dd.MM.yyyy")}
  Mietende: ${reservation.endDatum?string("dd.MM.yyyy")}


Bitte überprüfe noch einmal die obigen Daten. Bei Fehlern wende dich bitte an unsere Kassiererin. 
Ist alles korrekt, überweise den Mietpreis in Höhe von ${kosten?string.currency} innerhalb von 30 Tagen auf unser Vereinskonto:

  Hausverein Giesserei
  8404 Winterthur
  IBAN: CH61 0900 0000 8533 4587 8

Einzahlungsscheine befinden sich vor der Wohnung 2412 (auf dem Schuhschrank, Haus 1, 4. Obergeschoss).


Bitte beachte:
----------------
1. Verlängerungen der Parkplatz-Mieten werden immer ca. 30 Tage vor Ablauf automatisch durchgeführt. 

2. Für jeden Parkplatz wird eine separate Mail versendet.

3. Hast du den Betrag bereits überwiesen, musst du nichts weiter unternehmen.

4. Wird der Mietpreis nicht rechtzeitig bezahlt, wird die Reservation annulliert.


------------------------------------------------------------------------------------------------
Parkplatzliste: http://velo.giesserei-gesewo.ch:8080/velo/liste

