package nextbike.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import nextbike.model.Point;
import nextbike.model.RoadStation;
import nextbike.model.Station;
import nextbike.model.Times;
import nextbike.modules.ErrorCode;
import nextbike.validator.RoadFormValidator;

@Controller
public class NextBikeController {

	private static String URL_GETSTATION = "https://sheltered-lowlands-77048.herokuapp.com/getStations";
	private static String URL_ROAD_TIME = "https://sheltered-lowlands-77048.herokuapp.com/getRoadTime";
	private static String MESSAGE_ERROR = "èle wype≥ni≥eú formularz";
	private static String MESSAGE = "Poprawnie poda≥eú punkty trasy";
	
	private static Logger logger = LogManager.getLogger(NextBikeController.class);

	@Autowired
	private RoadFormValidator roadFromValidator;

	@InitBinder("roadForm")
	protected void initRoadFormValidator(WebDataBinder binder) {
		binder.setValidator(roadFromValidator);
	}

	@RequestMapping(value = { "index", "/" })
	public ModelAndView index(){
		ModelAndView modelAndView = new ModelAndView("index");
		JSONObject jsonObject = null;
		try {
			jsonObject = getJSONObjectFromAPI(URL_GETSTATION, "stacje");
		} catch (IOException e) {
			logger.error("NextBikeController: " + e.getMessage() + " Code: " + ErrorCode.getErrorCode());
		}
		modelAndView.addObject("stacje", jsonObject);
		return modelAndView;
	}

	@RequestMapping(value = "wyznaczanieTrasy", method = RequestMethod.GET)
	public ModelAndView wyznaczanieTrasyGET(RoadStation roadStationOrNull, Integer messageOrNull)
			throws ParseException, IOException {
		ModelAndView modelAndView = new ModelAndView("wyznaczanieTrasy");
		JSONObject jsonObject = getJSONObjectFromAPI(URL_GETSTATION, "stacje");
		modelAndView.addObject("stacje", jsonObject);
		List<Station> stations = getStations(jsonObject);
		List<String> stationsNames = new ArrayList<String>();
		stationsNames.add("");
		for (int i = 0; i < stations.size(); i++)
			stationsNames.add(stations.get(i).getNazwaStacji());
		modelAndView.addObject("stationsNames", stationsNames);

		if (roadStationOrNull == null) {
			roadStationOrNull = new RoadStation(null, null);
		}

		if (messageOrNull != null) {
			switch (messageOrNull) {
			case 1:
				modelAndView.addObject("wiadomosc", MESSAGE_ERROR);
				break;
			case 2:
				modelAndView.addObject("wiadomosc", MESSAGE);
				break;
			default:
				break;
			}
		}
		modelAndView.addObject("roadForm", roadStationOrNull);
		return modelAndView;
	}

	@RequestMapping(value = "wyznaczanieTrasy", method = RequestMethod.POST)
	public ModelAndView wyznaczanieTrasyPost(@ModelAttribute("roadForm") @Validated RoadStation roadStation,
			BindingResult result) throws ParseException, IOException {
		if (result.hasErrors())
			return wyznaczanieTrasyGET(roadStation, 1);
		else
			return wyznaczanieTrasy2(roadStation);
	}

	@RequestMapping(value = "wyznaczanieTrasy2")
	public ModelAndView wyznaczanieTrasy2(RoadStation roadStation) throws IOException {
		ModelAndView modelAndView = new ModelAndView("wyznaczanieTrasy2");
		JSONObject jsonObject = getJSONObjectFromAPI(URL_GETSTATION, "stacje");
		List<Station> stations = getStations(jsonObject);
		Point pointA = null;
		Point pointB = null;
		for (int i = 0; i < stations.size(); i++) {
			if (stations.get(i).getNazwaStacji().equals(roadStation.getPointA())) {
				pointA = new Point(stations.get(i).getLat(), stations.get(i).getLng(), stations.get(i).getIdStacji());
			}
			if (stations.get(i).getNazwaStacji().equals(roadStation.getPointB())) {
				pointB = new Point(stations.get(i).getLat(), stations.get(i).getLng(), stations.get(i).getIdStacji());
			}
		}
		modelAndView.addObject("pointA", pointA);
		modelAndView.addObject("pointB", pointB);
		JSONObject czasy = getJSONObjectFromAPI(URL_ROAD_TIME, "czasy");
		List<Times> times = getTimes(czasy);
		int time = 0;
		for (int i = 0; i < times.size(); i++) {
			if (times.get(i).getStationFrom() == pointA.getIdstacji()
					&& times.get(i).getStationTo() == pointB.getIdstacji()) {
				time = times.get(i).getTime();
			}
		}
		modelAndView.addObject("time", time);
		List<Station> wayPoints = getWaypoints(pointA, pointB, stations);
		JSONObject object = convertWaypointsToJson(wayPoints);
		modelAndView.addObject("stacje", object);
		modelAndView.addObject("stations3", wayPoints);
		return modelAndView;
	}

	private static JSONObject getJSONObjectFromAPI(String URL, String nameArray) throws IOException {

		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String string = response.toString();
		byte[] bytes = string.getBytes();
		String string2 = new String(bytes, "UTF-8");
		JSONArray array = new JSONArray(string2);
		JSONObject json = new JSONObject();
		json.put(nameArray, array);
		return json;
	}

	private List<Station> getStations(JSONObject object) {
		List<Station> stations = new ArrayList<Station>();
		JSONArray jsonArray = object.getJSONArray("stacje");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jObj = (JSONObject) jsonArray.get(i);
			stations.add(new Station(jObj.getInt("idstacji"), jObj.getString("nazwaStacji_var"),
					jObj.getDouble("lat_var"), jObj.getDouble("lng_var"), jObj.getInt("iloscStanowisk_var")));
		}
		return stations;
	}

	private List<Times> getTimes(JSONObject object) {
		List<Times> times = new ArrayList<Times>();
		JSONArray jsonArray = object.getJSONArray("czasy");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jObj = (JSONObject) jsonArray.get(i);
			times.add(new Times(jObj.getInt("czas"), jObj.getInt("idstacjiod"), jObj.getInt("idstacjido")));
		}
		return times;
	}

	private List<Station> getWaypoints(Point pointA, Point pointB, List<Station> stations) {
		int flaga1 = 0;
		int flaga2 = 0;
		if (pointA.getLat() < pointB.getLat()) {
			flaga1 = 1;
		}
		if (pointA.getLng() < pointB.getLng()) {
			flaga2 = 1;
		}
		List<Station> stations2 = new ArrayList<Station>();// wschod - zachod
		List<Station> stations3 = new ArrayList<Station>();// polnoc - poludnie
		for (int i = 0; i < stations.size(); i++) {
			Double lng = stations.get(i).getLng();
			if (flaga2 == 1) {
				if (pointA.getLng() < lng && lng < pointB.getLng())
					stations2.add(stations.get(i));
			} else {
				if (pointB.getLng() < lng && lng < pointA.getLng())
					stations2.add(stations.get(i));
			}
		}
		for (int i = 0; i < stations2.size(); i++) {
			Double lat = stations2.get(i).getLat();
			if (flaga1 == 1) {
				if (pointA.getLat() < lat && lat < pointB.getLat())
					stations3.add(stations2.get(i));
			} else {
				if (pointB.getLat() < lat && lat < pointA.getLat())
					stations3.add(stations2.get(i));

			}
		}

		return stations3;
	}

	
	private JSONObject convertWaypointsToJson(List<Station> wayPoints){
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < wayPoints.size(); i++) {
			JSONObject object = new JSONObject();
			object.put("nazwaStacji", wayPoints.get(i).getNazwaStacji());
			object.put("lat_var", wayPoints.get(i).getLat());
			object.put("lng_var", wayPoints.get(i).getLng());
			jsonArray.put(object);

		}
		JSONObject object = new JSONObject();
		object.put("stacje", jsonArray);
		return object;
	}
}
