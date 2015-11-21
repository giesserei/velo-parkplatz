<#ftl encoding="UTF-8">
*** Dies ist ein automatisch erstelltes E-Mail ***

Liebe Mitglieder der Velo-Gruppe

Es wurde soeben die Reservation für einen Velo-Parkplatz im Untergeschoss bestätigt. 
Der Parkplatz kann damit von euch als reserviert markiert werden.

  Name des Mieters: ${reservation.mieterPerson.nameUndWohnung!""}
  Parkplatznummer und -Typ: ${reservation.stellplatz.nummer} / ${typ}
  Mietbeginn: ${reservation.beginnDatum?string("dd.MM.yyyy")}
  
  
------------------------------------------------------------------------------------------------
Parkplatzliste: http://velo.giesserei-gesewo.ch:8080/velo/liste

