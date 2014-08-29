package cn.edu.sjtu.crawl;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import cn.edu.sjtu.crawl.util.CrawlUtil;
import cn.edu.sjtu.persist.TaskDao;

public abstract class AbstractPageCrawler {
	protected TaskDao taskDao = new TaskDao();
	
	public List<Model> crawl(String url,String referUrl) throws Exception{
		String content = CrawlUtil.crawl(url, referUrl,3000);
		System.out.println(content);
		Element element = Jsoup.parse(content, url.substring(0, url.indexOf('/', 8)));
		return parse(element, url);
	}
	
	protected abstract List<Model> parse(Element doc, String url);
	
	public abstract boolean execute(Model model) throws Exception;
}
