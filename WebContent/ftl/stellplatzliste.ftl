<#ftl encoding="UTF-8">
<!DOCTYPE html>
<html>
  <head>
    <title>Giesserei - Velo-Parkplatzliste</title>
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
              <button type="button" style="margin-top:20px" class="btn btn-info" onclick="location.href='reservation'">Reservation</button>
            </div>
            <div class="col-md-7">
              <h1>Velo-Parkplatzliste</h1>
              <p>Hier könnt ihr nachsehen, welche Velo-Parkplätze noch frei sind. Bei Fragen wendet euch bitte an:</p>
              <address>
                <a href="mailto:kasse@giesserei-gesewo.ch">kasse@giesserei-gesewo.ch</a>
              </address>
            </div>
          </div>
        </div>
      </div>
    </div>
	
	  <div class="row">
	    <div class="col-md-6">
	      
	      <!-- Pills -->
        <ul class="nav nav-pills" id="sektorenTab">
          <li class="active"><a href="#sektor1" data-toggle="pill">Sektor 1 (41-69)</a></li>
          <li><a href="#sektor2" data-toggle="pill">Sektor 2 (70-127)</a></li>
          <li><a href="#sektor3" data-toggle="pill">Sektor 3 (Spezial 1-15)</a></li>
          <li><a href="#sektor4" data-toggle="pill">Sektor 4 (Spezial 16-26, 128-183)</a></li>
          <li><a href="#sektor5" data-toggle="pill">Sektor 5 (Spezial 27-32, 184-244)</a></li>
          <li><a href="#sektor6" data-toggle="pill">Sektor 6 (Spezial 33-39, 245-298)</a></li>
        </ul>
        
        <#macro stellplatzTabelle stellplatzListe modelTable>
          <table class="table">
            <thead>
              <tr>
                <th>Platz-Nummer</th>
                <th>Typ</th>
                <th>Gemietet bis</th>
                <th>Wohnung</th>
              </tr>
            </thead>
            <tbody>
              <#list stellplatzListe as s>
              <tr>
                <td>${s.nummer}</td>
                <td>
                  <#if s.typ == "PEDALPARC_HOCH">PedalParc Hoch</#if>
                  <#if s.typ == "PEDALPARC_TIEF">PedalParc Tief</#if>
                  <#if s.typ == "SPEZIAL">Spezial</#if>
                </td>
                <td>
                  <#if s.reserviertBis??>
                    ${s.reserviertBis?string("dd.MM.yyyy")}
                  <#else>
                    <#assign query="nummer=${s.nummer}&typ=${s.typ}"/>
                    <button type="button" class="btn btn-success" onclick="location.href='${modelTable.linkReservation}?${query}'">Reservieren</button>
                  </#if> 
                </td>
                <td>${s.wohnungsNr!""}</td>
              </tr>
              </#list> 
            </tbody>
          </table>
        </#macro>
        
        <!-- Tab panes -->
        <div class="tab-content">
          <div class="tab-pane active" id="sektor1">
            <div class="panel panel-default" style="margin-top:10px">
              <div class="panel-heading">
                <p style="background-color:rgb(254,128,83)">&nbsp;Parkplätze im Sektor 1 (Haus 7)</p>
                <p>&nbsp;Typ <strong>PedalParc</strong> Platz-Nummern 41 bis 69</p>
              </div>
              <@stellplatzTabelle stellplatzListe=model.sektors[0] modelTable=model/>
            </div>
          </div>
          <div class="tab-pane" id="sektor2">
            <div class="panel panel-default" style="margin-top:10px">
              <div class="panel-heading">
                <p style="background-color:rgb(250,236,13)">&nbsp;Parkplätze im Sektor 2 (zwischen Haus 7 und Haus 8):</p>
                <p>&nbsp;Typ <strong>PedalParc</strong> Platz-Nummern 70 bis 127</p>
              </div>
              <@stellplatzTabelle stellplatzListe=model.sektors[1] modelTable=model/>
            </div>
          </div>
          <div class="tab-pane" id="sektor3">
            <div class="panel panel-default" style="margin-top:10px">
              <div class="panel-heading">
                <p style="background-color:rgb(251,192,2)">&nbsp;Parkplätze im Sektor 3 (Velorampe):</p>
                <p>&nbsp;Typ <strong>Spezial</strong> Platz-Nummern 1 bis 15</p>
              </div>
              <@stellplatzTabelle stellplatzListe=model.sektors[2] modelTable=model/>
            </div>
          </div>
          <div class="tab-pane" id="sektor4">
            <div class="panel panel-default" style="margin-top:10px">
              <div class="panel-heading">
                <p style="background-color:rgb(199,252,123)">&nbsp;Parkplätze im Sektor 4 (zwischen Haus 1 und Haus 2):</p>
                <p>&nbsp;Typ <strong>Spezial</strong> Platz-Nummern 16 bis 26</p> 
                <p>&nbsp;Typ <strong>PedalParc</strong> Platz-Nummern 128 bis 183</p>
              </div>
              <@stellplatzTabelle stellplatzListe=model.sektors[3] modelTable=model/>
            </div>          
          </div>
          <div class="tab-pane" id="sektor5">
            <div class="panel panel-default" style="margin-top:10px">
              <div class="panel-heading">
                <p style="background-color:rgb(135,252,214)">&nbsp;Parkplätze im Sektor 5 (zwischen Haus 2 und Haus 3):</p>
                <p>&nbsp;Typ <strong>Spezial</strong> Platz-Nummern 27 bis 32</p> 
                <p>&nbsp;Typ <strong>PedalParc</strong> Platz-Nummern 184 bis 244</p>
              </div>
              <@stellplatzTabelle stellplatzListe=model.sektors[4] modelTable=model/>
            </div>          
          </div>
          <div class="tab-pane" id="sektor6">
            <div class="panel panel-default" style="margin-top:10px">
              <div class="panel-heading">
                <p style="background-color:rgb(125,206,248)">&nbsp;Parkplätze im Sektor 6 (zwischen Haus 3 und Haus 4):</p>
                <p>&nbsp;Typ <strong>Spezial</strong> Platz-Nummern 33 bis 39</p>
                <p>&nbsp;Typ <strong>PedalParc</strong> Platz-Nummern 245 bis 298</p>
              </div>
              <@stellplatzTabelle stellplatzListe=model.sektors[5] modelTable=model/>
            </div> 
          </div>
        </div>
        
        <script>
          <#list 1..6 as index>
            $('#sektor${index} a').click(function (e) {
              e.preventDefault()
              $(this).tab('show')
            })
          </#list>
        </script>
	    
	    </div>
	    <div class="col-md-6">
	      <div style="padding-top:80px">
	        <img src="VAADIN/themes/giesserei/img/sektoren.png"/>
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
