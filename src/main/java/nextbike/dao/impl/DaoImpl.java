package nextbike.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import nextbike.dao.DaoInterface;
import nextbike.model.Station;

@Service("DAO")
@Scope("singleton")
public class DaoImpl implements DaoInterface {

	@Autowired()
	private ComboPooledDataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void init() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@PreDestroy
	public void cleanUp() {
		try {
			dataSource.close();
		} catch (Exception e) {
		}
	}

	@Override
	public List<Station> getStacje() throws DataAccessException {
		return jdbcTemplate.query("SELECT * FROM TabLokalizacjaStacji",
				new RowMapper<Station>() {

					public Station mapRow(ResultSet rs, int rowNumber) throws SQLException {
						return new Station(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4),
								rs.getInt(5));
					}
				}, new Object[] {});
	}

}
