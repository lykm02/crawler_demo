package cn.edu.sjtu.crawl;

import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.sjtu.crawl.util.CrawlUtil;
import cn.edu.sjtu.management.Manager;
import cn.edu.sjtu.persist.LocalFileUtil;
import cn.edu.sjtu.persist.PageDetailDao;
import cn.edu.sjtu.persist.model.PageDetail;

public class DetailPageCrawler extends AbstractPageCrawler {
	private static Logger logger = Logger.getLogger("detailcrawler");
	private final PageDetailDao detailDao = new PageDetailDao();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String url = "http://prl.aps.org/abstract/PRL/v109/i26/e268102";
		String referUrl = "http://prl.aps.org/toc/PRL/v109/i26";
		DetailPageCrawler crawler = new DetailPageCrawler();
		//List<Model> models = crawler.crawl(url,referUrl);
//		crawler.execute(model);

	}

	@Override
	protected List<Model> parse(Element doc, String url) {
		
		return null;
	}

	@Override
	public boolean execute(Model model) throws Exception{
		logger.info("start handling model " + model.url);
		String content = CrawlUtil.crawl(model.url, model.referUrl,3000);
		
		LocalFileUtil.write(content, model.title,
				Manager.StateMachine.DetailPage.rule);
		Element element = Jsoup.parse(content,
				model.url.substring(0, model.url.indexOf('/', 8)));
		PageDetail detail = extract(element, model.url);
		detailDao.insertDetail(detail);
		return true;
	}

	private PageDetail extract(Element element, String url) {
		PageDetail detail = new PageDetail();
		detail.title = element.select("div#content h1").first().text();
		Elements authorInfo = element.select("div#aps-authors a[href*=/author/]");
		if(!authorInfo.isEmpty()){
			String authorText = "";
			for(Element sub : authorInfo){
				authorText += sub.text() + " and ";
			}
			detail.author = authorText;
		}
		Elements orgInfo = element.select("div#aps-authors span.italic");
		String orgText = "";
		for(Element sub: orgInfo){
			orgText += sub.text() + "  and  "; 
		}
//		detail.org = orgText;
		detail.abstractText = element.select("div.aps-abstractbox").text();
		Elements elements = element.select("#aps-article-info > div.table-row");
		if(!elements.isEmpty()){
			for(Element subEle : elements){
				String text = subEle.text();
				if(text.startsWith("URL:")){
					detail.url = text.substring(text.indexOf(':')+1).trim();
				}
				if(text.startsWith("DOI:")){
					detail.doi = text.substring(text.indexOf(':')+1).trim();
				}
				if(text.startsWith("PACS:")){
					detail.pacs = text.substring(text.indexOf(':')+1).trim();
				}
			}
		}
		Elements citings = element.select("a:contains(Citing Articles)");
		if(!citings.isEmpty()){
			String citingText = citings.text().substring(citings.text().indexOf("Citing Articles")+15);
			detail.citingCount = citingText;
		}
		Elements timeInfo = element.select("p:containsOwn(Received)");
		if(!timeInfo.isEmpty()){
			detail.timeInfo = timeInfo.first().ownText();
		}
		Elements navbar = element.select("div.breadcrumb");
		if(!navbar.isEmpty()){
			detail.navigation = navbar.text();
		}
		return detail;
	}

}
