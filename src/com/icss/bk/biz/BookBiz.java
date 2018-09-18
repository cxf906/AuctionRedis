package com.icss.bk.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import com.icss.bk.dao.BookDao;
import com.icss.bk.dto.BookJson;
import com.icss.bk.entity.TBook;
import com.icss.util.Log;
import com.icss.util.RedisUtil;

public class BookBiz {
		
	/**
	 * 主页显示，所有图书信息
	 * @return
	 * @throws Exception
	 */
	public List<TBook> getAllBooks() throws Exception{
		List<TBook> books = null;
		
		BookDao dao = new BookDao();
		try {
			books = dao.getAllBooks();	
		} catch (Exception e) {
			Log.logger.error(e.getMessage());
		}finally{
			dao.closeConnection();
		}
		
		return books;		
	}	
	
	/**
	 * 从redis中提取所有参加秒杀图书信息
	 * @return
	 * @throws Exception
	 */
	public List<BookJson> getAllBookAction() throws Exception{
		
		List<BookJson> bkList = new ArrayList<BookJson>();
		
		Jedis jedis = RedisUtil.getJedis();			
		Map<String,String> allBooks =  jedis.hgetAll("AuctionBooks");
		Set<String> keys =  allBooks.keySet();
		for(String isbn : keys){
			 String bkString = allBooks.get(isbn);
			 String bkNew = bkString.substring(1, bkString.length()-1);
			 JSONObject jsonObject = JSONObject.fromObject(bkNew);
			 BookJson bkJson  = (BookJson) JSONObject.toBean(jsonObject,BookJson.class); 
			 bkList.add(bkJson);
		}			
		RedisUtil.returnResource(jedis);		
		
		return bkList;
		
	}
	
	

}
