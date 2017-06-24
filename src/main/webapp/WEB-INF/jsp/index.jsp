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
<title>Nextbike - statystyki</title>
<link href="/nextbike/static/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="/nextbike/static/css/row.css" rel="stylesheet">
<script src="/nextbike/static/bootstrap/css/bootstrap.min.css"></script>
</head>
<body>
	<%@include file="navbar.jsp" %>
	<div class="row">
		<center>
			<div id="map"></div>
		</center>
	</div>
</body>
<script>
	var s = ${stacje};
	var markers = [];
	var titles = [];
	var locations = [];
	var contentString = [];
	var infoWindows = [];
	for(i=0;i<s.stacje.length;i=i+1)
		{
			locations.push({
				lat : s.stacje[i].lat_var,
				lng : s.stacje[i].lng_var
			});
			titles.push(s.stacje[i].nazwaStacji_var);
			contentString.push("<div align='left'>ID stacji: " + s.stacje[i].idstacji + "<br>"
					+ "Lokalizacja: " + s.stacje[i].nazwaStacji_var+"<br>Liczba stanowisk: " + s.stacje[i].iloscStanowisk_var + "</div>");
		}
	function initMap() {

		var map = new google.maps.Map(document.getElementById('map'), {
			zoom : 15,
			center : {
				lat : 51.7592485,
				lng : 19.4559833
			}
		})

		
		for(z=0;z<s.stacje.length;z=z+1)
			{
				var infoWindow = new google.maps.InfoWindow({
					content : contentString[z]
				});
				var marker = new google.maps.Marker({
					position : locations[z],
					map : map,
					title : titles[z],
					infoWindowIndex : z
				});
				google.maps.event.addListener(marker, 'click', function(event) {
					map.panTo(event.latLng);
					infoWindows[this.infoWindowIndex].open(map, this);
				});

				infoWindows.push(infoWindow);
				markers.push(marker);
			}
		
		
		var markerCluster = new MarkerClusterer(
				map,
				markers,
				{
					imagePath : 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'
				});
		 markers = locations.map(function(location, i) {
				return new google.maps.Marker({
					position : location,
					title : titles[i]
				});
			});
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
	height: 650px;
	width: 100%;
}
</style>
</html>