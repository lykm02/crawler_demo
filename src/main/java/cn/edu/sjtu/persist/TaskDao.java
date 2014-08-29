package cn.edu.sjtu.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.edu.sjtu.crawl.Model;

public class TaskDao {
	
	public List<Model> getUnFinishedModels() throws SQLException{
		Connection conn = ConnectionPools.getConn();
		Statement statement = conn.createStatement();
		ResultSet resultset = statement.executeQuery("select * from task where task_status!=0");
		List<Model> models = new ArrayList<Model>();
		while(resultset.next()){
			Model model = new Model();
			model.id = resultset.getInt("id");
			model.status = resultset.getInt("task_status");
			model.rule = resultset.getString("rule");
			model.url = resultset.getString("url");
			model.referUrl = resultset.getString("refer_url");
			model.title = resultset.getString("title");
			model.path = resultset.getString("path");
			models.add(model);
		}
		resultset.close();
		statement.close();
		conn.close();
		return models;
	}
	
	public Model getUnFinishedModel() throws SQLException{
		Connection conn = ConnectionPools.getConn();
		Statement statement = conn.createStatement();
		ResultSet resultset = statement.executeQuery("select top 1 * from task where task_status=0");
		Model model = new Model();
		while(resultset.next()){
			model.id = resultset.getInt("id");
			model.status = resultset.getInt("task_status");
			model.rule = resultset.getString("rule");
			model.url = resultset.getString("url");
			model.referUrl = resultset.getString("refer_url");
			model.title = resultset.getString("title");
			model.path = resultset.getString("path");
		}
		resultset.close();
		statement.close();
		conn.close();
		if(model.id!=null){
			return model;
		}
		return null;
	}
	
	public Model getModelByKey(int id) throws SQLException{
		Connection conn = ConnectionPools.getConn();
		PreparedStatement statement = conn.prepareStatement("select * from task where id = ?");
		statement.setInt(1, id);
		ResultSet resultset = statement.executeQuery();
		Model model = new Model();
		while(resultset.next()){
			model.id = resultset.getInt("id");
			model.status = resultset.getInt("task_status");
			model.rule = resultset.getString("rule");
			model.url = resultset.getString("url");
			model.referUrl = resultset.getString("refer_url");
			model.title = resultset.getString("title");
			model.path = resultset.getString("path");
		}
		resultset.close();
		statement.close();
		conn.close();
		if(model.id!=null){
			return model;
		}
		return null;
	}
	
	public void updateModel(Model model) throws SQLException{
		Connection conn = ConnectionPools.getConn();
		PreparedStatement statement = conn.prepareStatement("update task set task_status=" + model.status +",path=? where id="+model.id);
		statement.setString(1, model.path);
		statement.executeUpdate();
		statement.close();
		conn.close();
	}
	
	public void insertModel(Model model) throws SQLException{
		Connection conn = ConnectionPools.getConn();
		PreparedStatement statement = conn.prepareStatement("insert into task (rule,url,refer_url,task_status,title,path) values (?,?,?,?,?,?)");
		statement.setString(1, model.rule);
		statement.setString(2, model.url);
		statement.setString(3, model.referUrl);
		statement.setInt(4,model.status);
		statement.setString(5, model.title);
		statement.setString(6, model.path);
		statement.execute();
		statement.close();
		conn.close();
	}
	
	public static String generateInsertSql(Model model){
		return "insert into task (rule,url,refer_url,task_status,title,path) values ("+model.rule+","+model.url+","+model.referUrl+","+model.status+","+model.title+","+model.path+")";
	}
	
	public void insertBatch(List<Model> models) throws SQLException{
		for(Model model:models){
			insertModel(model);
		}
//		Connection conn = ConnectionPools.getConn();
//		Statement statement = conn.createStatement();
//		for(Model model : models){
//			statement.addBatch(generateInsertSql(model));
//		}
//		statement.executeBatch();
//		statement.close();
//		conn.close();
	}
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		Model model = new Model();
		model.url = "http://prl.aps.org/browse";
		model.rule = "directory";
		TaskDao dao = new TaskDao();
		dao.insertModel(model);
	}

}
