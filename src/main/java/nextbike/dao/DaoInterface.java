package nextbike.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import nextbike.model.Station;

public interface DaoInterface {

	public List<Station> getStacje() throws DataAccessException;
}
