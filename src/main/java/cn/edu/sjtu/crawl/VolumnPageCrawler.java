package cn.edu.sjtu.crawl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.sjtu.crawl.util.CrawlUtil;
import cn.edu.sjtu.management.Manager;
import cn.edu.sjtu.persist.LocalFileUtil;


public class VolumnPageCrawler extends AbstractPageCrawler{
	
	@Override
	public List<Model> parse(Element element, String url){
		Elements elements = element.select("div#content ul.noble > li > a");
		List<Model> models = new ArrayList<Model>(elements.size());
		for(Element ele : elements){
			Model mod = new Model();
			mod.referUrl = url;
			mod.status = 0;
			mod.url = ele.baseUri() + ele.attr("href");
			mod.title = ele.text();
			mod.rule = Manager.StateMachine.IssuePage.rule;
			models.add(mod);
		}
		return models;
	}
	
	@Override
	public boolean execute(Model model) throws Exception {
		String content = CrawlUtil.crawl(model.url, model.referUrl,3000);
		LocalFileUtil.write(content, model.title,
				Manager.StateMachine.VolumnPage.rule);
		Element element = Jsoup.parse(content,
				model.url.substring(0, model.url.indexOf('/', 8)));
		List<Model> models = parse(element, model.url);
		taskDao.insertBatch(models);
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String refer = "http://prl.aps.org/browse";
		String url = "http://prl.aps.org/vtoc/PRL/v109";
		VolumnPageCrawler crawler = new VolumnPageCrawler();
		List<Model> models = crawler.crawl(url,refer);
		System.out.println(models);
	}
}
