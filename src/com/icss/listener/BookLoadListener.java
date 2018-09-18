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
	 * ϵͳ����ʱ�������е�ͼ����Ϣ��װ�ص�redis���ݿ���
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
				bkj.setBkCount(30);                                            //����������ÿ����30��
				JSONArray jsonArray = JSONArray.fromObject(bkj); 			
				String bkString = jsonArray.toString();                        //ÿ��������һ��json��		
				map.put(bk.getIsbn(), bkString);
			}			
			jedis.hmset("AuctionBooks",map);                                    //������ͼ�飬����hash map��	
			
			RedisUtil.returnResource(jedis);
		} catch (Exception e) {
			 e.printStackTrace();
		}		
	}

}
