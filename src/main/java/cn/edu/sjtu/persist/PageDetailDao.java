package cn.edu.sjtu.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.edu.sjtu.persist.model.PageDetail;

public class PageDetailDao {
	public void insertDetail(PageDetail detail) throws SQLException{
		Connection conn = ConnectionPools.getConn();
		PreparedStatement statement = conn.prepareStatement("insert into page_detail (url,title,author,contacts,org,navbar,citing_count,doi,pacs,abstract_text,time_info) values (?,?,?,?,?,?,?,?,?,?,?)");
		statement.setString(1, detail.url);
		statement.setString(2, detail.title);
		statement.setString(3, detail.author);
		statement.setString(4, detail.contacts);
		statement.setString(5, detail.org);
		statement.setString(6, detail.navigation);
		statement.setString(7, detail.citingCount);
		statement.setString(8, detail.doi);
		statement.setString(9, detail.pacs);
		statement.setString(10, detail.abstractText);
		statement.setString(11, detail.timeInfo);
		statement.execute();
		statement.close();
		conn.close();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
