package com.xl.tool.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class XMLUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(XMLUtil.class);
	
	/**
	 *
	 * @param in
	 * @param head
	 * @param T
	 * @return
	 * @throws DocumentException
	 */
	public static <T>List<T> fromXMLToObjectList(InputStream in,
			String head, Class<T> T) throws Exception {
		List<T> result = new ArrayList<T>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(in);
		
		List<Element> list = document.selectNodes(head);
		Iterator<Element> iter = list.iterator();
		while(iter.hasNext()){
			Element element = iter.next();
			T t = (T)fromElementToObject(element, T);//灏唀lement杞崲鎴愬璞?
			result.add(t);
		}
		return result;
	}
	public static <T>List<T> fromXMLToObjectList(String path,
												 String head, Class<T> T) throws Exception {
		List<T> result = new ArrayList<T>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(path);

		List<Element> list = document.selectNodes(head);
		Iterator<Element> iter = list.iterator();
		while(iter.hasNext()){
			Element element = iter.next();
			T t = (T)fromElementToObject(element, T);//灏唀lement杞崲鎴愬璞?
			result.add(t);
		}
		return result;
	}
	
	/**
	 * 读取为Map对象，将id作为Key
	 * @param in 文件流
	 * @param head XML Head
	 * @param T Class
	 * @param key Map.key
	 * @return
	 * @throws DocumentException
	 */
	public static <T>Map<Integer, T> fromXMLToObjectMap(InputStream in,
														String head,String key, Class<T> T) throws Exception {
		Map<Integer, T> result = new HashMap<Integer, T>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(in);

		List<Element> list = document.selectNodes(head);
		Iterator<Element> iter = list.iterator();
		while(iter.hasNext()){
			Element element = iter.next();
			T t = (T)fromElementToObject(element, T);//转换为Java对象
			int id = Integer.parseInt(element.attributeValue(key));
			result.put(id, t);
		}
		return result;
	}
	public static <T>Map<Integer, T> fromXMLToObjectMap(String path,String head,String key, Class<T> T) throws Exception{
		return fromXMLToObjectMap(new FileInputStream(path),head,key,T);
	}
	public static <T>Map<Integer, T> fromXMLToObjectMap(String path,String head, Class<T> T) throws Exception{
		return fromXMLToObjectMap(new FileInputStream(path), head,  T);
	}
	/**
	 * 读取为Map对象，将id作为Key
	 * @param in 文件流
	 * @param head XML Head
	 * @param T Class
	 * @return
	 * @throws DocumentException
	 */
	public static <T>Map<Integer, T> fromXMLToObjectMap(InputStream in,
														String head, Class<T> T) throws Exception {
		Map<Integer, T> result = new HashMap<Integer, T>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(in);

		List<Element> list = document.selectNodes(head);
		Iterator<Element> iter = list.iterator();
		while(iter.hasNext()){
			Element element = iter.next();
			T t = (T)fromElementToObject(element, T);//转换为Java对象
			int id = Integer.parseInt(element.attributeValue("id"));
			result.put(id, t);
		}
		return result;
	}
	
	/**
	 * 转为Map对象，字段值也存为Map
	 * @param in
	 * @param head
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, Map> fromXMLToMap(InputStream in,
			String head) throws Exception {
		Map<String, Map> result = new HashMap<String, Map>();
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(in);
		List<Element> list = document.selectNodes(head);
		Iterator<Element> iter = list.iterator();
		while(iter.hasNext()){
			Element element = iter.next();
			Map map = fromElementToJsonToMap(element);//灏唀lement杞崲鎴怣ap
			String id = element.attributeValue("id");			
			result.put(id, map);
		}
		return result;
	}
	
	/**
	 * 转换为对象
	 * @param element
	 * @return
	 */
	public static <T>T fromElementToObject(Element element, Class<T> t) {
		String json = fromElementToJson(element);//灏唀lement杞崲鎴恓son鏍煎紡
		T result = (T)JSONObject.parseObject(json, t);//灏唈son杞崲鎴愬璞?
		return result;
	}

	/**
	 * 转换为JSON
	 * @param element
	 * @return
	 */
	public static String fromElementToJson(Element element) {
		Iterator<DefaultAttribute> iter = element.attributeIterator();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		while(iter.hasNext()){
			DefaultAttribute attribute = iter.next();
			String name = attribute.getName();
			String value = attribute.getStringValue();
			sb.append("\"").append(name).append("\"").append(":")
			  .append("\"").append(value).append("\"").append(",");
		}
		sb.append("}");
		return sb.toString();
	}
	/**
	 * 转换为JSONMap
	 * @param element
	 * @return
	 */
	public static Map fromElementToJsonToMap(Element element) {
		List<DefaultElement> list= element.content();
		Map reMap=new HashMap();
		Map map=null;
		for(int i=0;i<list.size();i++){
			if(list.get(i) instanceof DefaultElement){
				map=new HashMap();
				Iterator<DefaultAttribute> it = list.get(i).attributeIterator();
				while(it.hasNext()){
					DefaultAttribute attribute = it.next();
					String name1 = attribute.getName();
					String value = attribute.getStringValue();
					map.put(name1, value);
				}
				reMap.put(map.get("id"), map);
			}
		}
		return reMap;
	}
	/**
	 * 转换为对象
	 * @param in
	 * @param head
	 * @param T
	 * @return
	 * @throws DocumentException
	 */
	public static <T>T fromXMLToObject(InputStream in, String head,
			Class<T> T) throws Exception {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(in);
		
		List<Element> list = document.selectNodes(head);
		return fromElementToObject(list.get(0), T);
	}
	public static int readAndSetInt(Element e,String name,int defaultValue){
		String value=e.attributeValue(name);
		if(value==null||value.trim().equals("")){
			return 0;
		}else{
			return Integer.parseInt(value);
		}
	}
	public static long readAndSetLong(Element e,String name,long defaultValue){
		String value=e.attributeValue(name);
		if(value==null||value.trim().equals("")){
			return 0;
		}else{
			return Long.parseLong(value);
		}
	}
	public static int[] readIntArray(Element e,String name,String splitRegx){
		String value=e.attributeValue(name);
		if(value==null||value.trim().equals("")){
			return null;
		}
		String[] paramsArr=value.split(splitRegx);
		int[] params=new int[paramsArr.length];
		for(int i=0;i<paramsArr.length;i++){
			params[i]=Integer.parseInt(paramsArr[i]);
		}
		return params;
	}
}