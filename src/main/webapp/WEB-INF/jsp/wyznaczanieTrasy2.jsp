<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" isELIgnored="false"
	contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Nextbike - wyznaczanie trasy cd.</title>
<link href="/nextbike/static/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="/nextbike/static/css/row.css" rel="stylesheet">
<script src="/nextbike/static/bootstrap/css/bootstrap.min.css"></script>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container">
		<div class="row">
			<div class="col-md-8"><center>
			<div id="map"></div>
		</center></div>
			<div class="col-md-4">
				Średni czas przejazdu: ${time } min</br> <i>(Ctrl+Click
				lub Cmd+Click wybieranie wielu punktów)</i> </br>
			<div class="form-group">
			 Punkty pośrednie:
				<select size="10" multiple id="waypoints">
					<c:forEach var="item" items="${stations3}">
						<option value="${item.nazwaStacji}">${item.nazwaStacji}</option>
					</c:forEach>
				</select></br><input type="submit" class="btn btn-primary" id="submit">
				
			</div>
			<div id="content">
					
					
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<center>
			<div id="map"></div>
		</center>
	</div>
	
</body>
<script>

	var waypts = [];
	var stacjeWybrane = [];
	var s = ${stacje};
	var locationA;
	var locationB;
	var content =  document.getElementById("content");
	locationA = {
		lat : ${pointA.lat},
		lng : ${pointA.lng}
		};
	locationB = {
		lat : ${pointB.lat},
		lng : ${pointB.lng}
		}; 
	function initMap() {
		var directionsDisplay = new google.maps.DirectionsRenderer;
		var directionsService = new google.maps.DirectionsService;
		var map = new google.maps.Map(document.getElementById('map'), {
			zoom : 15,
			center : {
				lat : 51.7592485,
				lng : 19.4559833
			}
		});
		directionsDisplay.setMap(map);
		calculateAndDisplayRoute(directionsService, directionsDisplay);
		document.getElementById('submit').addEventListener('click', function() {
	          myFunction(directionsService, directionsDisplay);
	          content.innerHTML="Czas między punktami:<br>";
	          myFunction2();
	          
	          
	        });
	}
	 function calculateAndDisplayRoute(directionsService, directionsDisplay) {
		directionsService.route({
			origin : locationA,
			destination : locationB,
			travelMode : google.maps.TravelMode.BICYCLING
		}, function(response, status) {
			if (status == google.maps.DirectionsStatus.OK) {
				directionsDisplay.setDirections(response);
			}
		});
	} 
	function myFunction(directionsService, directionsDisplay) {
		waypts = [];
		stacjeWybrane = [];
        var loc;
        var checkboxArray = document.getElementById('waypoints');
        for (var i = 0; i < checkboxArray.length; i++) {
          if (checkboxArray.options[i].selected) 
          {
           		for(z=0; z<s.stacje.length;z++)		
        	  	{
           			if(checkboxArray.options[i].value == s.stacje[z].nazwaStacji)
           				{
           					
           					loc = {
           						lat : s.stacje[z].lat_var,
           						lng : s.stacje[z].lng_var
           						};
           					stacjeWybrane.push({
           						lat : s.stacje[z].lat_var,
           						lng : s.stacje[z].lng_var,
           						idstacji:s.stacje[z].idStacji,
           						nazwaStacji: s.stacje[z].nazwaStacji
           						});
	           				waypts.push({
	           	              location: loc,
	           	              stopover: true
	           	            });
        	  			}
        	  	}	
        	  
          }
        }
        directionsService.route({
			origin : locationA,
			destination : locationB,
			waypoints: waypts,
			travelMode : google.maps.TravelMode.BICYCLING
		}, function(response, status) {
			if (status == google.maps.DirectionsStatus.OK) {
				directionsDisplay.setDirections(response);
			}
		});
	};
	function myFunction2(){
		var lat = ${pointA.lat};
		var lng = ${pointA.lng};
		var wynik = [];
		var czasy = [];
		var times = ${times};
		var wynik2 = [];
		for(k=0;k<stacjeWybrane.length;k=k+1)
			{
				var odlegloscX = Math.abs(lat - stacjeWybrane[k].lat);
				var odlegloscY = Math.abs(lng - stacjeWybrane[k].lng);
				var odleglosc = Math.sqrt( odlegloscX*odlegloscX +odlegloscY*odlegloscY );
				wynik.push({
					dystans : odleglosc,
					idStacji : stacjeWybrane[k].idstacji,
					nazwaStacji: stacjeWybrane[k].nazwaStacji
				});
			}
		wynik.sort(function(a, b){
		    return a.dystans - b.dystans;
		});
		czasy = [];
		wynik2 = [];
		var id = ${pointA.idstacji};
		wynik2.push({
			idstacji: id,
		});
		for(k=0;k<wynik.length;k=k+1)
		{	
			wynik2.push({
				idstacji:wynik[k].idStacji
			});	
		}
		id = ${pointB.idstacji};
		wynik2.push({
			idstacji: id,
		});
		for(k=0; k<wynik2.length-1;k=k+1)
			{
				for(z=0;z<times.czasy.length;z=z+1)
				{
					if(times.czasy[z].idstacjiod == wynik2[k].idstacji )
						{
							if(times.czasy[z].idstacjido == wynik2[k+1].idstacji)
								{
									czasy.push(times.czasy[z].czas);
									break;
								}else
									continue;
						}else
							continue;
				}
			}
		var tresc = "";
		for(i=0;i<czasy.length;i++)
			{
				tresc = tresc + " " + czasy[i] + "<br>";
				
			}
		content.innerHTML=tresc;
	}

	
</script>
<script
	src="https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/markerclusterer.js">
	
</script>
<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBQAR809YkzO5lbIQ_dht4OlSFEaznt2T4&callback=initMap">
	
</script>
<style>
#map {
	height: 430px;
	width: 100%;
}
</style>
</html>