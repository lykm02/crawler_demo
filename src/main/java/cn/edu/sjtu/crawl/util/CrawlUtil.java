package cn.edu.sjtu.crawl.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CrawlUtil {
	private static Logger logger = Logger.getLogger("crawlerUtil");
	
	public static String crawl(String url, String refer, long timeout) throws ClientProtocolException, IOException, URISyntaxException{
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpUriRequest request = new HttpGet(url);
		request.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		request.addHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		request.addHeader("Accept-Encoding","gzip, deflate");
		request.addHeader("Connection","keep-alive");
		if(refer!=null && !"".equals(refer)){
			request.addHeader("refer", refer);
		}
		
		HttpResponse response = null;
		int i = 0;
		do{
			response = client.execute(request);
			i++;
			if(i>1){
				if(response.getStatusLine().getStatusCode() > 400){
					throw new RuntimeException("We got ["+response.getStatusLine()+"] when request " + url);
				}
				logger.warning("retry "+ i+" times when request " +url+" response code: "+response.getStatusLine());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}while(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK && i<3);
				
		if(i == 3){
			System.out.println(response.getStatusLine());
			System.out.println(response.getEntity().toString());
			return null;
		}
		
		HttpEntity entity = response.getEntity();
		String charsetName = extract(entity.getContentType());
		return EntityUtils.toString(entity,charsetName);
	}
	
	private static String extract(Header contentType) {
		if(contentType == null || contentType.getValue() == null){
			return null;
		}
		String[] args = contentType.getValue().split(";");
		for(String arg : args){
			if(arg.startsWith("charset=")){
				String charsetName = arg.substring(arg.indexOf('=')+1);
				if(!"".equals(charsetName)){
					return charsetName;
				}
			}
		}
		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

}
