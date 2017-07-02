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
<title>Nextbike - wyznaczanie trasy</title>
<link href="/nextbike/static/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="/nextbike/static/css/row.css" rel="stylesheet">
<script src="/nextbike/static/bootstrap/css/bootstrap.min.css"></script>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container">
		<div class="row">
			<div class="col-md-8">
				<div id="map"></div>
			</div>
			<div class="col-md-4">
				<c:if test="${not empty wiadomosc}">
					<div class="alert alert-info">
						<center>${wiadomosc}</center>
					</div>
				</c:if>
				<form:form method="post" modelAttribute="roadForm"
					action="/nextbike/webapp/wyznaczanieTrasy" role="form">
					<div class="form-group">
						Z:
						<form:select id="pointA" path="pointA" items="${stationsNames }"
							class="form-control" />
						<div>
							<form:errors path="pointA" element="div" />
						</div>

					</div>
					<div class="form-group">

						Do:
						<form:select id="pointB" path="pointB" items="${stationsNames }"
							class="form-control" />
						<div>
							<form:errors path="pointB" element="div" />
						</div>

					</div>
					<div class="row col-lg-3">
						<br>
						<form:input class="submit btn btn-primary" path="" type="submit"
							value="Wyznacz trasę"></form:input>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
<script>
	var s = ${stacje};
	var titles = [];
	var locations = [];
	for (i = 0; i < s.stacje.length; i = i + 1) {
		locations.push({
			lat : s.stacje[i].lat_var,
			lng : s.stacje[i].lng_var
		});
		titles.push(s.stacje[i].nazwaStacji_var);
	}
	function initMap() {

		var map = new google.maps.Map(document.getElementById('map'), {
			zoom : 15,
			center : {
				lat : 51.7592485,
				lng : 19.4559833
			}
		})

		var markers = locations.map(function(location, i) {
			return new google.maps.Marker({
				position : location,
				title : titles[i]
			});
		});
		var markerCluster = new MarkerClusterer(
				map,
				markers,
				{
					imagePath : 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'
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
	height: 430px;
	width: 100%;
}
</style>
</html>