package kr.scg.web.jt.ctl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.scg.web.jt.mdl.ScgUser;

/**
 * Handles requests for the Employee JDBC Service.
 */
@Controller
public class ScgUserCtl {

	private static final Logger logger = LoggerFactory.getLogger(ScgUserCtl.class);

	@Autowired
	@Qualifier("ds_mysql_1")
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@RequestMapping(value = "/scg/test")
	public String test() {
		return "test";
	}

	@RequestMapping(value = "/rest/scg_users", method = RequestMethod.GET)
	public @ResponseBody List<ScgUser> getAllEmployees() {
		logger.info("Start getAllEmployees.");
		List<ScgUser> empList = new ArrayList<ScgUser>();
		// JDBC Code - Start
		String query = "select * from scg_users limit 10";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> empRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> empRow : empRows) {
			ScgUser emp = new ScgUser();
			emp.setNo(Integer.parseInt(String.valueOf(empRow.get("no"))));
			emp.setId(String.valueOf(empRow.get("id")));
			emp.setName(String.valueOf(empRow.get("name")));
			empList.add(emp);
		}
		return empList;
	}
}