<#ftl encoding="UTF-8">
<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<#-- height and width of a month -->
<#assign width = 213.34023 height = 167.382>

<#-- spacing between months -->
<#assign spacing = 3>

<#-- compute scale -->
<#assign scale = 0.7 * (scale / 100)>

<svg
   xmlns:svg="http://www.w3.org/2000/svg"
   xmlns="http://www.w3.org/2000/svg"
   xmlns:xlink="http://www.w3.org/1999/xlink"
   version="1.2"
   width="${scale * (width * columns + (spacing * (columns + 1)))}"
   height="${scale * (height * rows + (spacing * (rows + 1)))}">
   
  <#-- definition of gradients --> 
  <defs>
    <linearGradient
       id="linearGradient_bg">
      <stop
         style="stop-color:#ffffff;stop-opacity:1"
         offset="0" />
      <stop
         style="stop-color:${colors.colorBackMonth};stop-opacity:0"
         offset="1" />
    </linearGradient>
    <linearGradient
       id="linearGradient_line">
      <stop
         style="stop-color:#929292;stop-opacity:1"
         offset="0" />
      <stop
         style="stop-color:#ffffff;stop-opacity:0"
         offset="1" />
    </linearGradient>
    <linearGradient
       x1="226.02863"
       y1="472.22565"
       x2="227.00925"
       y2="59.073261"
       id="linearGradient3911"
       xlink:href="#linearGradient_bg"
       gradientUnits="userSpaceOnUse"
       gradientTransform="matrix(0.97053084,0,0,0.70911775,8.1689101,-0.07301384)" />
    <linearGradient
       x1="151.02287"
       y1="66.4533"
       x2="152.02287"
       y2="287.6767"
       id="linearGradient3921"
       xlink:href="#linearGradient_line"
       gradientUnits="userSpaceOnUse"
       gradientTransform="matrix(1,0,0,0.65651784,1.1553981,23.172491)" />
       
    <style type="text/css"><![CDATA[
      text {
        fill-opacity:1;
        stroke:none;
        font-family:Verdana;
        font-style:normal;
        font-weight:normal;
        word-spacing:0px;
        letter-spacing:0px;
        line-height:125%;
        font-size:14px;
      }
      text.dayText {
      }
      text.title {
        fill:${colors.colorTextMonth};
      }
      text.weekdayName {
        fill:${colors.colorTextNameWeekday};
      }
      text.weekendName {
        fill:${colors.colorTextNameWeekend};
      }
      rect.backgroundMonth {
        fill:url(#linearGradient3911);
        fill-opacity:1;
        fill-rule:evenodd;
        stroke:${colors.colorBorder};
        stroke-width:2.08878088;
        stroke-linejoin:round;
        stroke-miterlimit:4;
        stroke-opacity:0.72314046;
        stroke-dasharray:none
      }
      rect.backgroundCalendar {
        fill:${colors.colorBackCalendar};
        fill-opacity:1;
      }
      rect.canvasMonth {
        fill:#ffffff;
        fill-opacity:1;
      }
      path.verticalLine {
        fill:none;
        stroke:url(#linearGradient3921);
        stroke-width:0.8px;
        stroke-linecap:butt;
        stroke-linejoin:miter;
        stroke-opacity:1
      }
    ]]></style>
  </defs>
  
  <#assign col = 0 row = 0>
  
  <#-- iterates the list of months -->
  <g transform="scale(${scale})">
    <rect class="backgroundCalendar" x="0" y="0" height="100%" width="100%"/>
  
	  <#list months as month>
	    <#-- use for translate column, row and spacing -->
	    <g transform="translate(${col * width + (spacing * (col + 1))},${row * height + (spacing * (row + 1))})">
	      <rect class="canvasMonth" x="0" y="0" width="${width}" height="${height}"/>
	      <@_month month=month />
	    </g>
	      
	    <#assign col = col + 1 />
	  
	    <#if (col >= columns)>
	      <#assign col = 0 row = row + 1>
	    </#if>
	      
	  </#list>
  </g>
	  
</svg>

<#-- ----------------------------------------------------------------- -->
<#-- Macro zur Darstellung eines Monats                                -->
<#-- ----------------------------------------------------------------- -->

<#-- param: month BuchungMonat -->
<#macro _month month>

<#-- layer: backround -->
<g
   transform="translate(-118.89146,-39.757581)"
   id="layer_background">
  <rect
     class="backgroundMonth"
     width="211.25145"
     height="165.29323"
     x="119.93585"
     y="40.801971"
     id="rect_border" />
</g>

<#-- layer: occupation -->
<g
   transform="translate(-118.89146,-39.757581)"
   id="layer_occupation">
   
  <#list month.occupiedDays as day>
     <@_sym_occupied day=day />
  </#list> 
  
  <#list month.arrivalDays as day>
     <@_sym_arrival day=day />
  </#list>
  
  <#list month.departureDays as day>
     <@_sym_departure day=day />
  </#list>
</g>

<#-- layer: text -->
<g
   transform="translate(-118.89146,-39.757581)"
   id="layer_text">
   
  <#-- weekday names -->
  <text
     class="weekdayName"
     x="126.64315"
     y="80.668198"
     id="text_mo"
     xml:space="preserve"
     >${dayNames["mo"]}</text>
  <text
     class="weekdayName"
     x="159.83057"
     y="80.880112"
     id="text_di"
     xml:space="preserve"
     >${dayNames["tu"]}</text>
  <text
     class="weekdayName"
     x="188.44279"
     y="80.880112"
     id="text_mi"
     xml:space="preserve"
     >${dayNames["we"]}</text>
  <text
     class="weekdayName"
     x="215.50325"
     y="80.668198"
     id="text_do"
     xml:space="preserve"
     >${dayNames["th"]}</text>
  <text
     class="weekdayName"
     x="246.87378"
     y="80.880112"
     id="text_fr"
     xml:space="preserve"
     >${dayNames["fr"]}</text>
  <text
     class="weekendName"
     x="274.86734"
     y="80.668198"
     id="text_sa"
     xml:space="preserve"
     >${dayNames["sa"]}</text>
  <text
     class="weekendName"
     x="303.6983"
     y="80.668198"
     id="text_so"
     xml:space="preserve"
     >${dayNames["su"]}</text>
  
  <#-- vertical lines between weekdays -->
  <path
     class="verticalLine"
     d="m 152.67827,67.463448 0,112.741142"
     id="line_1" />
  <path
     class="verticalLine"
     d="m 181.79634,67.463448 0,112.741142"
     id="line_2" />
  <path
     class="verticalLine"
     d="m 210.91443,67.463448 0,112.741142"
     id="line_3" />
  <path
     class="verticalLine"
     d="m 240.0325,67.463448 0,112.741142"
     id="line_4" />
  <path
     class="verticalLine"
     d="m 269.15057,67.463448 0,112.741142"
     id="line_5" />
  <path
     class="verticalLine"
     d="m 298.26865,67.463448 0,112.741142"
     id="line_6" />
  
  <#-- title -->
  <text
     class="title"
     x="226"
     y="58.372059"
     id="text_title"
     xml:space="preserve"
     text-anchor="middle"
     >${month.title}</text>
       
  <#-- days -->
  <#list month.days as day>
    <@_day_text day=day />
  </#list>                   
</g>
</#macro >

<#-- ----------------------------------------------------------------- -->
<#-- Macro zur Darstellung gebuchten Tages                             -->
<#-- ----------------------------------------------------------------- -->

<#-- param: date BuchungTag -->
<#macro _sym_occupied day>
<rect
  width="24.93"
  height="14.32"
  x="${day.x}"
  y="${day.y}"
  style="fill:${day.color};fill-opacity:0.75206616;fill-rule:evenodd;stroke:none" />
</#macro>

<#-- ----------------------------------------------------------------- -->
<#-- Macro zur Darstellung eines Anreise-Tages                         -->
<#-- ----------------------------------------------------------------- -->

<#-- param: date BuchungTag -->
<#macro _sym_arrival day>
<path
    d="m ${day.x},${day.y} 24.93,0 0,-14.32 z"
    style="fill:${day.color};fill-opacity:0.75206616;stroke:none" />
</#macro>

<#-- ----------------------------------------------------------------- -->
<#-- Macro zur Darstellung eines Abreise-Tages                         -->
<#-- ----------------------------------------------------------------- -->

<#-- param: date BuchungTag -->
<#macro _sym_departure day>
<path  
    d="m ${day.x},${day.y} 0,-14.25421 24.7573,0 z"
    style="fill:${day.color};fill-opacity:0.75206616;stroke:none" />
</#macro>

<#-- ----------------------------------------------------------------- -->
<#-- Macro zur Darstellung eines Tages im Kalendermonat                -->
<#-- ----------------------------------------------------------------- -->

<#-- param: day Tag im Kalendermonat -->
<#macro _day_text day>
<text
    class="dayText"
    x="${day.x}"
    y="${day.y}"
    text-anchor="end"
    xml:space="preserve"
    style="fill:${day.color};"
    >${day.text}</text>
</#macro>