package com.bean.jdbccontext;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IRowMapper<T> {
	T mapRow(ResultSet rs) throws SQLException;
}
