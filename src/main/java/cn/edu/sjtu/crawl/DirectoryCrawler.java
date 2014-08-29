package cn.edu.sjtu.crawl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.sjtu.crawl.util.CrawlUtil;
import cn.edu.sjtu.management.Manager;
import cn.edu.sjtu.persist.LocalFileUtil;

public class DirectoryCrawler extends AbstractPageCrawler {

	@Override
	protected List<Model> parse(Element element, String url) {
		Elements elements = element.select("div#content ul.noble > li > a");
		List<Model> models = new ArrayList<Model>(elements.size());
		for (Element ele : elements) {
			Model mod = new Model();
			mod.referUrl = url;
			mod.status = 0;
			mod.url = ele.baseUri() + ele.attr("href");
			mod.rule = Manager.StateMachine.VolumnPage.rule;
			mod.title = ele.text();
			models.add(mod);
		}
		return models;
	}

	@Override
	public boolean execute(Model model) throws Exception {
		String content = CrawlUtil.crawl(model.url, model.referUrl,3000);
		LocalFileUtil.write(content, Manager.StateMachine.Directory.rule,
				Manager.StateMachine.Directory.rule);
		Element element = Jsoup.parse(content,
				model.url.substring(0, model.url.indexOf('/', 8)));
		List<Model> models = parse(element, model.url);
		taskDao.insertBatch(models);
		return true;
	}

	public static void main(String[] args) throws Exception {
		String input = "http://prl.aps.org/browse";
		DirectoryCrawler crawler = new DirectoryCrawler();
		List<Model> models = crawler.crawl(input, null);
		System.out.println(models);
	}
}
