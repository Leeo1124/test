package com.leeo.transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransServices2 {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('7','aaa','bbb')");

	}

	public void save2() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('7','aaa','bbb')");

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save3() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('7','aaa','bbb')");

		throw new RuntimeException("save出错");
	}

	public void save4() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('7','aaa','bbb')");

		throw new RuntimeException("save出错");
	}

	@Transactional(propagation = Propagation.NESTED)
	public void save5() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('7','aaa','bbb')");

		throw new RuntimeException("save出错");
	}

	@Transactional(propagation = Propagation.NESTED)
	public void save6() throws Exception {
		this.jdbcTemplate
		.execute("insert into test123(id, NAME, remart) values('7','aaa','bbb')");
	}
}
