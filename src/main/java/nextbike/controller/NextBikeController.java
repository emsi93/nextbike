package nextbike.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import nextbike.dao.DaoInterface;
import nextbike.model.Point;
import nextbike.model.RoadStation;
import nextbike.model.Station;
import nextbike.model.Times;
import nextbike.validator.RoadFormValidator;

@Controller
public class NextBikeController {

	private static String URL_GETSTATION = "https://sheltered-lowlands-77048.herokuapp.com/getStations";
	private static String URL_ROAD_TIME = "https://sheltered-lowlands-77048.herokuapp.com/getRoadTime";
	private static String MESSAGE_ERROR = "èle wype≥ni≥eú formularz";
	private static String MESSAGE = "Poprawnie podano punkty trasy";

	@Autowired
	private DaoInterface dao;

	@Autowired
	private RoadFormValidator roadFromValidator;

	@InitBinder("roadForm")
	protected void initRoadFormValidator(WebDataBinder binder) {
		binder.setValidator(roadFromValidator);
	}

	@RequestMapping(value = { "index", "/" })
	public ModelAndView index() throws DataAccessException, ParseException, IOException {
		ModelAndView modelAndView = new ModelAndView("index");
		JSONObject jsonObject = getJSONObjectFromAPI(URL_GETSTATION,"stacje");
		modelAndView.addObject("stacje", jsonObject);
		return modelAndView;
	}

	@RequestMapping(value = "wyznaczanieTrasy", method = RequestMethod.GET)
	public ModelAndView wyznaczanieTrasyGET(RoadStation roadStationOrNull, Integer messageOrNull)
			throws DataAccessException, ParseException, IOException {
		ModelAndView modelAndView = new ModelAndView("wyznaczanieTrasy");
		JSONObject jsonObject = getJSONObjectFromAPI(URL_GETSTATION,"stacje");
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
			BindingResult result) throws DataAccessException, ParseException, IOException {
		if (result.hasErrors())
			return wyznaczanieTrasyGET(roadStation, 1);
		else
			return wyznaczanieTrasy2(roadStation);
	}

	@RequestMapping(value = "wyznaczanieTrasy2")
	public ModelAndView wyznaczanieTrasy2(RoadStation roadStation) throws IOException {
		ModelAndView modelAndView = new ModelAndView("wyznaczanieTrasy2");
		JSONObject jsonObject = getJSONObjectFromAPI(URL_GETSTATION,"stacje");
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
		for(int i=0; i<times.size();i++)
		{
			if(times.get(i).getStationFrom() == pointA.getIdstacji() && times.get(i).getStationTo() == pointB.getIdstacji()){
				time = times.get(i).getTime();
			}
		}
		modelAndView.addObject("time", time);
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
	
	private List<Times> getTimes(JSONObject object){
		List<Times> times = new ArrayList<Times>();
		JSONArray jsonArray = object.getJSONArray("czasy");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jObj = (JSONObject) jsonArray.get(i);
			times.add(new Times(jObj.getInt("czas"),jObj.getInt("idstacjiod"),jObj.getInt("idstacjido")));
		}
		return times;
	}

}
