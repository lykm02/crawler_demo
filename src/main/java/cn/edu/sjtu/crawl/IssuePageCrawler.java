package cn.edu.sjtu.crawl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.sjtu.crawl.util.CrawlUtil;
import cn.edu.sjtu.management.Manager;
import cn.edu.sjtu.persist.LocalFileUtil;

public class IssuePageCrawler extends AbstractPageCrawler {

	@Override
	protected List<Model> parse(Element doc, String url) {
		Elements elements = doc.select("div#content div.aps-toc-articleinfo > strong > a");
		List<Model> models = new ArrayList<Model>(elements.size());
		for(Element ele : elements){
			Model mod = new Model();
			mod.referUrl = url;
			mod.status = 0;
			mod.url = ele.baseUri() + ele.attr("href");
			mod.rule = Manager.StateMachine.DetailPage.rule;
			mod.title = ele.text();
			models.add(mod);
		}
		return models;
	}

	@Override
	public boolean execute(Model model) throws Exception {
		String content = CrawlUtil.crawl(model.url, model.referUrl,3000);
		LocalFileUtil.write(content, model.title,
				Manager.StateMachine.IssuePage.rule);
		Element element = Jsoup.parse(content,
				model.url.substring(0, model.url.indexOf('/', 8)));
		List<Model> models = parse(element, model.url);
		//do a filter if there are some duplicated data.
		List<Model> uniqueModels = unique(models);
		taskDao.insertBatch(uniqueModels);
		return true;
	}
	
	private List<Model> unique(List<Model> models) {
		List<Model> otherModels = new ArrayList<Model>(models.size());
		for(Model model : models){
			boolean flag = false;
			for(Model model2 : otherModels){
				if(model2.compareTo(model) == 0){
					flag = true;
					continue;
				}
			}
			if(!flag){
				otherModels.add(model);
			}
		}
		return otherModels;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String url = "http://prl.aps.org/toc/PRL/v110/i19";
		String refer = "http://prl.aps.org/toc/PRL/v110";
		IssuePageCrawler crawler = new IssuePageCrawler();
		Model model = new Model();
		model.url = url;
		model.referUrl = refer;
//		List<Model> models = crawler.crawl(url,refer);
//		System.out.println(models);
		crawler.execute(model);
		
	}
}
