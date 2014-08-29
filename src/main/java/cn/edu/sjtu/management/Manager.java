package cn.edu.sjtu.management;

import java.sql.SQLException;
import java.util.logging.Logger;

import cn.edu.sjtu.crawl.AbstractPageCrawler;
import cn.edu.sjtu.crawl.DetailPageCrawler;
import cn.edu.sjtu.crawl.DirectoryCrawler;
import cn.edu.sjtu.crawl.IssuePageCrawler;
import cn.edu.sjtu.crawl.Model;
import cn.edu.sjtu.crawl.VolumnPageCrawler;
import cn.edu.sjtu.persist.TaskDao;

public class Manager {
	private static Logger logger = Logger.getLogger("manager");
	public static void schedule() throws SQLException, InterruptedException{
		TaskDao dao = new TaskDao();
		while(true){
			Model model = dao.getUnFinishedModel();
			if(model == null){
				break;
			}
			StateMachine state = StateMachine.findByRule(model.rule);
			logger.info("start crawling " + model.url + " using " + model.rule);
			try {
				boolean flag = state.crawler.execute(model);
				if(flag){
					model.status = 1;
					dao.updateModel(model);
				}
			} catch(RuntimeException re){
				throw re;
			}catch (Exception e) {
				model.status = 1000;
				dao.updateModel(model);
				logger.warning("Exception happened when crawl " + model.url +" [rule] " + model.rule+ " while "+ e.getMessage());
			}
			
			Thread.sleep(5000);
		}
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException, InterruptedException {
		Manager.schedule();

	}
	public static enum StateMachine{
		Directory("directory",new DirectoryCrawler()),VolumnPage("volumnpage", new VolumnPageCrawler()),
		IssuePage("issuePage",new IssuePageCrawler()),DetailPage("detailPage", new DetailPageCrawler());
		
		public final String rule;
		public final AbstractPageCrawler crawler;
		
		private StateMachine(String rule, AbstractPageCrawler crawler){
			this.rule = rule;
			this.crawler = crawler;
		}
		
		public static StateMachine  findByRule(String rule){
			for(StateMachine statem : StateMachine.values()){
				if(statem.rule.equals(rule)){
					return statem;
				}
			}
			throw new IllegalArgumentException("cannot found matching rule for "+ rule);
		}
	}
}
