
package com.bean.jdbccontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;


/**
 * <p>JdbcTemplate 기능을 직접 구현한 클래스
 * 
 * <p>사용자가 SQL쿼리문을 입력하면 그 쿼리문을 실행하여 결과값을 리턴해주는 메소드들을
 * 멤버 메소드로 가지고 있다.
 * 
 * <p><b>그냥 연습용으로 만든거니 실무에서는 사용하지 말 것!!!</b>
 * 
 * 
 * 
 * @author 정진원
 *
 */
public class JdbcContext {
	private Connection c = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private DataSource dataSource;
	

	
	
	/**
	 * DataSource를 얻어와서 클래스 내부 멤버 변수에 저장한다.
	 * 
	 * @param dataSource	데이터베이스 Connection 객체를 얻기위한 객체
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	
	
	
	/**
	 * 인자 없는 SQL문을 입력받아 실행하여 업데이트된 Row수를 반환한다.
	 * 
	 * @param sql	매개변수가 없는 SQL 실행문
	 * @return		업데이트한 Row수
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException{
		return executeUpdate(getStatementStrategy(sql));
	}
	
	
	
	
	
	/**
	 * 인자 있는 SQL문을 입력받아 실행하여 업데이트된 Row수를 반환한다.
	 * 
	 * @param sql	매개변수를 대체하는 1개이상의 '?'를 가지고 있는 SQL 실행문
	 * @param args	sql 을 인자로 하여 생성된 PreparedStatement 객체에 입력할 인자값
	 * @return	업데이트한 Row수
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object... args) throws SQLException {
		return executeUpdate(getStatementStrategy(sql, args));
	}
	

	
	
	
	/**
	 * <p>인자 있는 SQL 쿼리문을 입력받아 실행하여 그 결과값을 T타입 객체에 담아 반환한다.
	 * 
	 * <p>만약 ResultSet으로 저장된 데이터가 없다면 SQLException이 발생한다.
	 * 
	 * @param sql		매개변수를 대체하는 1개이상의 '?'를 가지고 있는 SQL 쿼리문
	 * @param args		sql 을 인자로 하여 생성된 PreparedStatement 객체에 입력할 인자값
	 * @param rowMapper	쿼리문의 결과값을 저장할 로직이 담겨있는 메소드가 구현된 IRowMapper 인터페이스의 객체
	 * @return	쿼리문의 결과값을 저장한 T타입 객체
	 * @throws SQLException	
	 */
	public <T> T queryForObject(String sql, Object[] args, IRowMapper<T> rowMapper) throws SQLException {
		return queryForObject(getStatementStrategy(sql, args), rowMapper);
	}
	
	
	
	
	
	/**
	 * <p>인자 <b>없는</b> SQL 쿼리문을 입력받아 실행하여 그 결과값을 T타입 객체에 담아 반환한다.
	 * 
	 * <p>만약 ResultSet으로 저장된 데이터가 없다면 SQLException이 발생한다.
	 * 
	 * @param sql	매개변수가 없는 SQL 실행문		
	 * @param rowMapper	쿼리문의 결과값을 저장할 로직이 담겨있는 메소드가 구현된 IRowMapper 인터페이스의 객체
	 * @return 	쿼리문의 결과값을 저장한 T타입 객체
	 * @throws SQLException
	 */
	public <T> T queryForObject(String sql, IRowMapper<T> rowMapper) throws SQLException {
		return queryForObject(getStatementStrategy(sql), rowMapper);
	}
	
	
	
	
	
	/**
	 * <p>인자 있는 SQL 쿼리문을 입력받아 실행하여 그 결과값들을 List 형태의 객체에 담아 반환한다.
	 * SQL 쿼리문에 의하여 ResultSet으로 저장된 데이터들을 rowMapper의 mapRow()메소드를 실행하여 저장하여 각각의 객체를
	 * List 형태의 객체에 담아 반환한다.
	 * 
	 * <p><b>1개 이상의 T타입 객체가 반환된다.</b>
	 * 
	 * <p>만약 ResultSet으로 저장된 데이터가 없다면 SQLException이 발생한다.
	 * 
	 * @param sql		매개변수를 대체하는 1개이상의 '?'를 가지고 있는 SQL 쿼리문
	 * @param args		sql 을 인자로 하여 생성된 PreparedStatement 객체에 입력할 인자값
	 * @param rowMapper	쿼리문의 결과값을 저장할 로직이 담겨있는 메소드가 구현된 IRowMapper 인터페이스의 객체
	 * @return	쿼리문의 결과값을 저장한 T타입 객체를  저장한 List 객체
	 * @throws SQLException
	 */
	public <T> List<T> queryForObjects(String sql, Object[] args, IRowMapper<T> rowMapper) throws SQLException {
		return queryForObjects(getStatementStrategy(sql, args), rowMapper);
	}
	
	
	
	
	
	/**
	 * <p>인자 없는 SQL 쿼리문을 입력받아 실행하여 그 결과값들을 List 형태의 객체에 담아 반환한다.
	 * SQL 쿼리문에 의하여 ResultSet으로 저장된 데이터들을 rowMapper의 mapRow()메소드를 실행하여 DTO에 저장하여 각각의 객체를
	 * List 형태의 객체에 담아 반환한다.
	 * 
	 * <p><b>1개 이상의 T타입 객체가 반환된다.</b>
	 * 
	 * <p>만약 ResultSet으로 저장된 데이터가 없다면 SQLException이 발생한다. 
	 * 
	 * @param sql		매개변수를 대체하는 1개이상의 '?'를 가지고 있는 SQL 쿼리문
	 * @param rowMapper	쿼리문의 결과값을 저장할 로직이 담겨있는 메소드가 구현된 IRowMapper 인터페이스의 객체
	 * @return	쿼리문의 결과값을 저장한 T타입 객체를  저장한 List 객체
	 * @throws SQLException
	 */
	public <T> List<T> queryForObjects(String sql, IRowMapper<T> rowMapper) throws SQLException {
		return queryForObjects(getStatementStrategy(sql), rowMapper);
	}
	
	
	
	
	
	/**
	 * 데이터베이스 테이블명을 입력받아 해당 테이블의 Row수를 반환한다.
	 * 
	 * @param tableName	Row수를 구할 데이터베이스의 테이블 이름
	 * @return	데이터베이스 테이블의 Row수
	 * @throws SQLException
	 */
	public int countRows(String tableName) throws SQLException {
		return executeJdbcContext(getStatementStrategy("select count(*) from " + tableName), 
			() -> { doExecuteQuery(); return rs.getInt(1); });
	}
	
	
	
	
	
	/**
	 * <p>SQL문을 실행하는 메소드를 구현한 IJdbcContext 객체를 stmt와 함께
	 * 클래스 내부의 private 메소드인 executeJdbcContext() 메소드로 넘긴다.
	 * 
	 * <p>executeJdbcContext() 메소드에서는 넘겨받은 매개변수를 사용하여
	 * 로직을 수행한다.
	 * 
	 * <p>executeJdbcContext() 메소드는 모든 로직을 수행한 후 사용한 
	 * Connection 객체, PreparedStatement객체 그리고 ResultSet객체를 close()한다.
	 * 
	 * @param stmt	PreparedStatement를 구하는 메소드가 구현된 인터페이스 객체
	 * @return	업데이트한 Row수
	 * @throws SQLException
	 */
	private int executeUpdate(StatementStrategy stmt) throws SQLException {
		return executeJdbcContext(stmt, () -> ps.executeUpdate());
	}
	
	
	
	
	
	/**
	 * <p>이 메소드에서는 넘겨받은 rm 객체 를 사용하여 
	 * SQL문을 실행하여 데이터를 저장하는 메소드를 구현한다. 
	 * 그리고 메소드를 구현한 IJdbcContext 객체를 stmt와 함께
	 * 클래스 내부의 private 메소드인 executeJdbcContext() 메소드로 넘겨서
	 * 반환된 리턴값을 반환한다.
	 * 
	 * <p>executeJdbcContext() 메소드에서는 넘겨받은 매개변수를 사용하여
	 * 로직을 수행한다.
	 * 
	 * <p>executeJdbcContext() 메소드는 모든 로직을 수행한 후 사용한 
	 * Connection 객체, PreparedStatement객체 그리고 ResultSet객체를 close()한다.
	 * 
	 * @param stmt	PreparedStatement를 구하는 메소드가 구현된 인터페이스 객체
	 * @param rm	쿼리문을 실행하여 얻은 데이터를 저장하는 메소드가 구현된 객체
	 * @return	데이터를 저장한 T 객체
	 * @throws SQLException
	 */
	private <T> T queryForObject(StatementStrategy stmt, IRowMapper<T> rm) throws SQLException {
		return executeJdbcContext(stmt, () -> { doExecuteQuery(); return rm.mapRow(rs); });
	}
	
	
	
	
	
	/**
	 * <p>이 메소드에서는 넘겨받은 rm 객체 를 사용하여 
	 * SQL문을 실행하여 데이터를 DTO객체에 저장하는 메소드를 구현한다. 
	 * 그리고 메소드를 구현한 IJdbcContext 객체를 stmt와 함께
	 * 클래스 내부의 private 메소드인 executeJdbcContext() 메소드로 넘겨서
	 * 반환된 리턴값을 반환한다.
	 * 
	 * 
	 * <p>executeJdbcContext() 메소드에서는 넘겨받은 매개변수를 사용하여
	 * 로직을 수행한다.
	 * 
	 * <p>executeJdbcContext() 메소드는 모든 로직을 수행한 후 사용한 
	 * Connection 객체, PreparedStatement객체 그리고 ResultSet객체를 close()한다. 
	 * 
	 * @param stmt	PreparedStatement를 구하는 메소드가 구현된 인터페이스 객체
	 * @param rm	쿼리문을 실행하여 얻은 데이터를 저장하는 메소드가 구현된 객체
	 * @return	데이터를 저장한 T 객체를 담은 List 객체
	 * @throws SQLException
	 */
	private <T> List<T> queryForObjects(StatementStrategy stmt, IRowMapper<T> rm) throws SQLException {
		return executeJdbcContext(stmt, 
			() -> {
				doExecuteQuery();
				
				List<T> list = Collections.synchronizedList(new ArrayList<T>());
				do {
					T tmp = null;
					if((tmp = rm.mapRow(rs)) == null) throw new SQLException();
					list.add(tmp);
				} while(rs.next());
				
				return list;
			});
	}

	
	
	
	
	/**
	 * <p>이 메소드에서는 넘겨받은 매개변수를 사용하여 데이터베이스 관련 로직을 수행하면서 예외처리를 하며
	 * 로직 수행이 끝난 후에는 사용한 Connection, PreparedStatement, ResultSet 객체를 close()하는 역할을 한다.
	 * 
	 * <p><b>각각의 SQL실행 메소드에서 사용될 공통적인 부분만을 추려서 메소드 형태로 만들었다.</b>
	 * 
	 * @param stmt	PreparedStatement를 구하는 메소드가 구현된 인터페이스 객체
	 * @param ijc	PreparedStatement를 이용하여 데이터베이스 관련 로직을 수행하는 메소드가 구현된 IJdbcContext 객체
	 * @return	데이터를 저장한 T타입 객체
	 * @throws SQLException
	 */
	private <T> T executeJdbcContext(StatementStrategy stmt, IJdbcContext<T> ijc) throws SQLException {
		T o = null;
		
		try {
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			o = ijc.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) { try { rs.close(); } catch (SQLException e) { e.printStackTrace(); } }
			if (ps != null) { try { ps.close(); } catch (SQLException e) { e.printStackTrace(); } }
			if (c != null) { try { c.close(); } catch (SQLException e) { e.printStackTrace(); } }
		}
		
		return o; 
	}
	
	
	
	
	
	/**
	 * PreparedStatement객체를 생성할 때 매개변수에 해당하는 sql에서 '?'에 해당하는
	 * 부분을 args값들로 순차적으로 매치시켜준다.
	 * 
	 * @param c		PreparedStatement 객체를 생성하기 위한 Connection 객체
	 * @param sql	PreparedStatement 객체를 생성할 때 사용할 매개변수(SQL문)
	 * @param args	sql에서 '?'에 해당하는 매개변수 배열
	 * @return	생성한 PreparedStatement 객체
	 * @throws SQLException
	 */
	private PreparedStatement setObjects(Connection c, String sql, Object[] args) throws SQLException {
		ps = c.prepareStatement(sql);
		if(args == null || args.length == 0) return ps;
		int i = 1;
		for(Object o : args) {
			ps.setObject(i++, o);
		}
		return ps;
	}
	
	
	
	
	
	/**
	 * <p>중복되는 쿼리 실행메소드 executeQuery() 호출과
	 * 쿼리 실행문의 반환값이 하나도 없을 때 던지는 throw new SQLException(); 을 메소드로 구현하였다.
	 * 
	 * @throws SQLException
	 */
	private void doExecuteQuery() throws SQLException {
		rs = ps.executeQuery();
		if(!rs.next()) throw new SQLException();
	}
	
	
	
	
	
	/**
	 * sql을 매개변수로 하여 생성된 PreparedStaement객체에
	 * sql의 1개이상의 '?'를 args로 매치시켜 반환하는 메소드를 구현한
	 * StatementStrategy 객체를 반환한다.
	 * 
	 * @param sql	SQL 실행문
	 * @param args	sql을 인자로 하여 생성된 PreparedStatement 객체에 입력할 인자
	 * @return	생성된 StatementStrategy 객체
	 * @throws SQLException
	 */
	private StatementStrategy getStatementStrategy(String sql, Object[] args) throws SQLException {
		return c -> setObjects(c, sql, args);
	}
	
	
	
	
	
	/**
	 * sql을 인자로하여 PreparedStatement 객체를 반환하는 메소드를 구현한
	 * StatementStrategy 객체를 반환한다.
	 * 
	 * @param sql	인자없는 SQL문
	 * @return	생성된 StatementStrategy 객체
	 * @throws SQLException
	 */
	private StatementStrategy getStatementStrategy(String sql) throws SQLException {
		return c -> c.prepareStatement(sql);
	}
	
}
