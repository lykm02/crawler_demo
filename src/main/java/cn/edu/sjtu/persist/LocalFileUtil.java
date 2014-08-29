package cn.edu.sjtu.persist;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LocalFileUtil {
	
	public static void write(String content, String fileName,String path) throws IOException{
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		File concreteFile = new File( file.getAbsolutePath() + File.separator + fileName);
		if(!concreteFile.exists()){
			concreteFile.createNewFile();
		}
		FileWriter writer = new FileWriter(concreteFile);
		writer.write(content);
		writer.flush();
		writer.close();
	}
	
	public static void write(List<String> contents, String fileName,String path) throws IOException{
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		File concreteFile = new File( file.getAbsolutePath() + File.separator + fileName);
		if(!concreteFile.exists()){
			concreteFile.createNewFile();
		}
		FileWriter writer = new FileWriter(concreteFile);
		for(String content: contents){
			writer.write(content);
		}
		writer.flush();
		writer.close();
	}
	
	public static void main(String[] args){
		System.out.println(File.separator);
	}
}
