package cn.edu.sjtu.crawl;

public class Model implements Comparable<Model>{
	public String url;
	public String referUrl;
	public String title;
	public Integer status;
	public Integer id;
	public String rule;
	public String path;
	
	public Model(){
		status = 0;
	}
	
	@Override
	public String toString(){
		return id + "@@" + url + "@@" + referUrl +"@@" + status+"@@"+title +"@@"+rule+"@@"+path;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int compareTo(Model o) {
		if(url == null && o.url == null){
			return 0;
		}
		if(this.url!=null && url.equals(o.url)){
			return 0;
		}
		
		return url.compareTo(o.url);
	}

}
