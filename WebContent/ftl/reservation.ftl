<#ftl encoding="UTF-8">
<!DOCTYPE html>
<html>
  <head>
    <title>Giesserei - Reservation Velo-Parkplatz</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <!--<link href="../bootstrap/css/bootstrap.min.css" rel="stylesheet">-->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
  </head>
  <body style="margin:20px">
    <div class="page-header">
      <div class="row">
        <div class="col-md-12">
          <div class="row"> 
            <div class="col-md-5" style="width:380px">
              <img src="VAADIN/themes/giesserei/img/giesserei_logo.png"/>
              <button type="button" style="margin-top:20px" class="btn btn-info" onclick="location.href='liste'">Parkplatzliste</button>
            </div>
            <div class="col-md-7">
              <h1>Reservation Velo-Parkplatz</h1>
              <p>Hier könnt ihr einen Velo-Parkplatz reservieren. Nach der Reservation bekommt ihr automatisch ein E-Mail mit den Zahlungsinformationen. 
                 Bei Fragen wendet euch bitte an:</p>
              <address>
                <a href="mailto:kasse@giesserei-gesewo.ch">kasse@giesserei-gesewo.ch</a>
              </address>
            </div>
          </div>
        </div>
      </div>
    </div>
	
	  <div class="row">
	    <div class="col-md-8">
	      <#if !model.init && !model.success && !model.reservationSaveError> 
	        <div class="alert alert-danger">
	          ${model.validationError}
	        </div>
	      </#if>
	      <#if model.success>
	        <#if model.mailSend>
  	        <div class="alert alert-success">
  	          Die Reservation wurde erfolgreich gespeichert. Ein E-Mail mit den Zahlungsinformationen ist unterwegs zu dir.
  	        </div>
  	      <#else>
  	        <div class="alert alert-warning">
              Die Reservation wurde erfolgreich gespeichert. Das Bestätigungsmail konnte leider nicht an dich versendet werden. 
              Bitte melde dich bei <a href="mailto:kasse@giesserei-gesewo.ch">kasse@giesserei-gesewo.ch</a>.
            </div>
	        </#if>
	      </#if>
	      <#if model.reservationSaveError>
          <div class="alert alert-danger">
            Die Reservation konnte nicht durchgeführt werden. Bitte versuche es noch einmal.
          </div>
        </#if>
	      
	      <div class="panel panel-default">
	        <div class="panel-heading">
	          Reservation
	        </div>
  	      <div class="panel-body">
  	        <form role="form" method="post" action="reservation" enctype="application/x-www-form-urlencoded;charset=UTF-8">
  	          <div class="row">
  	            <div class="col-md-6">
                  <div class="radio">
                    <label>
                      <input type="radio" name="typeGroup" id="PEDALPARC_HOCH" value="PEDALPARC_HOCH" <#if model.typ == "PEDALPARC_HOCH">checked</#if>>
                      PedalParc (Fr. 60.- pro Jahr)
                    </label>
                  </div>
                  <div class="radio">
                    <label>
                      <input type="radio" name="typeGroup" id="SPEZIAL" value="SPEZIAL" <#if model.typ == "SPEZIAL">checked</#if>>
                      Spezial (Fr. 84.- pro Jahr)
                    </label>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group <#if model.errorField! == "nummer">has-error</#if>">
                    <label for="nummer">Parkplatz-Nummer</label>
                    <input type="number" class="form-control" id="nummer" name="nummer" placeholder="Parkplatz-Nummer" maxlength="3" 
                           value="${model.nummer!}">
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group <#if model.errorField! == "beginn">has-error</#if>">
                    <label for="beginn">Mietbeginn (01.MM.JJJJ)</label>
                    <input type="text" class="form-control" id="beginn" name="beginn" placeholder="01.MM.JJJJ" maxlength="10" 
                           value="${model.beginn!}">
                    <span class="help-block">Bitte beachte: Mietbeginn ist immer der 1. eines Monats</span>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label>Ende der Miete</label>
                    <input class="form-control" id="disabledInput" type="text" name="ende" placeholder="automatisch gesetzt" disabled
                           value="${model.ende!}">
                    <span class="help-block">Die Mietdauer beträgt immer ein Jahr</span>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group <#if model.errorField! == "name">has-error</#if>">
                    <label for="name">Name</label>
                    <input type="text" class="form-control" id="name" name="name" placeholder="Vorname und Name" maxlength="100"
                           value="${model.name!}">
                  </div>
                  <div class="form-group <#if model.errorField! == "email">has-error</#if>">
                    <label for="email">E-Mail</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Deine Mail-Adresse" maxlength="100"
                           value="${model.email!}">
                  </div>  
                  
                </div>
                <div class="col-md-6">
                  <div class="form-group <#if model.errorField! == "wohnung">has-error</#if>">
                    <label for="wohnung">Wohnungsnummer</label>
                    <input type="number" class="form-control" id="wohnung" name="wohnung" placeholder="Deine Wohnungsnummer" maxlength="4" 
                           value="${model.wohnung!}">
                  </div>
                  <div class="form-group">
                    <label for="name">Mitteilung</label>
                    <textarea class="form-control" rows="1" id="bemerkung" name="bemerkung" 
                              value="${model.bemerkung!}"></textarea>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                <button type="submit" class="btn btn-success" id="btnSubmit"
                            onclick="$('#btnSubmit').html('Bitte warten ...')">Reservation durchführen</button>
                </div>
              </div>
  	        </form>
  	      </div>
	      </div>
	    </div>
    </div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://code.jquery.com/jquery.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <!--<script src="../bootstrap/js/bootstrap.min.js"></script>-->
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>

  </body>
</html>
