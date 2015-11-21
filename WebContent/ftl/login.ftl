<#ftl encoding="UTF-8">
<!DOCTYPE html>
<html>
  <head>
    <title>Giesserei - Velo Parkplatzverwaltung</title>
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
            </div>
          
            <div class="col-md-7">
              <h1>Velo Parkplatzverwaltung</h1>
              <p>Dieser Bereich ist geschützt.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
	
	  <div class="row">
	    <div class="col-md-10">
	      <div class="row">
          <div class="col-md-6">
	          <#if model.validationError??> 
              <div class="alert alert-danger">
                ${model.validationError}
              </div>
            </#if>      
	          <div class="panel panel-default">
              <div class="panel-heading">
                Anmeldung
              </div>
              <div class="panel-body">
                <form role="form" method="post" action="login" enctype="application/x-www-form-urlencoded;charset=UTF-8">
                  <div class="form-group">
                    <label for="userName">Benutzername</label>
                    <input type="text" class="form-control" id="userName" name="userName" maxlength="30" autofocus="autofocus"
                           value="${model.userName!}">
                  </div>
                  <div class="form-group">
                    <label for="userPassword">Passwort</label>
                    <input type="password" class="form-control" id="userPassword" name="userPassword" maxlength="30"
                           value="">
                  </div>
                  <button type="submit" class="btn btn-primary" id="btnLogin" onclick="$('#btnLogin').html('Bitte warten ...')">Anmelden</button>
                </form>
              </div>
            </div>
	        </div>  
        </div>
      </div>  
    </div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://code.jquery.com/jquery.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <!--<script src="../bootstrap/js/bootstrap.min.js"></script>-->
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>
    
    <script>
      (function () { $('#userName').focus(); })();
    </script>

  </body>
</html>
