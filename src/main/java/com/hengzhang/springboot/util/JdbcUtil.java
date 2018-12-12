package com.hengzhang.springboot.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * JDBC 工具类
 * @author zhangh
 * @date 2018年8月31日下午1:53:07
 */
@Component
public class JdbcUtil {
	
	@Autowired
	private DataSource dataSource;
	
	/**
	 * jdbc 获取连接
	 * @author zhangh
	 * @date 2018年8月31日上午9:18:27
	 * @return
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	/**
	 * 关闭连接
	 * @author zhangh
	 * @date 2018年8月31日下午1:52:20
	 * @param conn
	 */
	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行sql
	 * @author zhangh
	 * @date 2018年8月31日下午1:52:08
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int executeUpdateSQL(String sql) throws Exception {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		int result = stmt.executeUpdate(sql);
		closeConnection(conn);
		return result;
	}
	
	/**
	 * JDBC 批量操作sql
	 * @author zhangh
	 * @date 2018年7月24日上午10:41:50
	 * @param conn
	 * @param batchSQL
	 * @return
	 * @throws Exception
	 */
	public void executeBatchSQL(Connection conn,List<String> batchSQL) throws Exception{
		conn.setAutoCommit(false);//必须要加上 否则会等待半天
		Statement stmt = conn.createStatement();
		if(ListUtil.isNotBlank(batchSQL)){
			for (String sql : batchSQL) {				
				stmt.addBatch(sql);
			}
		}
		stmt.executeBatch();
		conn.commit();
		stmt.close();
		closeConnection(conn);
	}
	
	/**
	 * JDBC 批量操作sql
	 * @author zhangh
	 * @date 2018年7月30日下午2:08:27
	 * @param batchSQL
	 * @return
	 * @throws Exception
	 */
	public void executeBatchSQL(List<String> batchSQL) throws Exception{
		Connection conn = getConnection();
		conn.setAutoCommit(false);//必须要加上 否则会等待半天
		Statement stmt = conn.createStatement();
		if(ListUtil.isNotBlank(batchSQL)){
			for (String sql : batchSQL) {				
				stmt.addBatch(sql);
			}
		}
		stmt.executeBatch();
		conn.commit();
		stmt.close();
		closeConnection(conn);
	}

	public String row2SQL(XSSFRow row) {
		return null;
	}
}
