package com.icss.listener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

import com.icss.bk.biz.BookBiz;
import com.icss.bk.dto.BookJson;
import com.icss.bk.entity.TBook;
import com.icss.util.RedisUtil;



public class BookLoadListener implements  ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}	
	
	/**
	 * 系统启动时，把所有的图书信息都装载到redis数据库中
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		BookBiz biz = new BookBiz();
		try {
			List<TBook> books = biz.getAllBooks();			
			Jedis jedis = RedisUtil.getJedis();	
			Map<String, String> map = new HashMap<String, String>();
			for(TBook bk : books){				
				BookJson bkj = new BookJson();
				bkj.setBname(bk.getBname());
				bkj.setIsbn(bk.getIsbn());
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");				
				bkj.setPdate(sd.format(bk.getPdate()));
				bkj.setPicurl(bk.getPicurl());
				bkj.setPress(bk.getPress());
				bkj.setPrice(bk.getPrice());
				bkj.setBkCount(30);                                            //抢购数量，每种书30个
				JSONArray jsonArray = JSONArray.fromObject(bkj); 			
				String bkString = jsonArray.toString();                        //每本书生成一个json串		
				map.put(bk.getIsbn(), bkString);
			}			
			jedis.hmset("AuctionBooks",map);                                    //把所有图书，存于hash map中	
			
			RedisUtil.returnResource(jedis);
		} catch (Exception e) {
			 e.printStackTrace();
		}		
	}

}
