package com.icss.listener;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import com.icss.bk.dto.BookJson;
import com.icss.util.RedisUtil;

public class AuctionListener implements ServletContextListener{
	private static final BlockingQueue<AsyncContext> queue = new LinkedBlockingQueue<AsyncContext>();  
	  
	  private volatile Thread thread;  
	  
	  public static void add(AsyncContext c) {
	    queue.add(c);  
	  }  
	  
	  @Override  
	  public void contextInitialized(ServletContextEvent servletContextEvent) {  
	    thread = new Thread(new Runnable() {  
	      @Override  
	      public void run() {  
	        while (true) {  	                   
	            AsyncContext acontext = null;  
	            while (queue.peek() != null) {  
	              try {  
	            	acontext = (AsyncContext)queue.poll();
	                ServletResponse response = acontext.getResponse();  
	                PrintWriter out = response.getWriter();	               		        
    		        Thread.sleep(1000);  
    		        String name = Thread.currentThread().getName(); 
    		        String isbn = (String)acontext.getRequest().getAttribute("isbn");
    		        int iRet = bookDecr(isbn);                            //图书抢购 
    		        System.out.println("-------------iRet=" + iRet);
    		  	    out.println(isbn + "*"+ name +"*"+ iRet);
    		  	    out.close();
	              } catch (Exception e) {  
	                  e.getMessage();  
	              } finally {  
	            	  if(acontext != null)
	                       acontext.complete();  
	              }  
	            }  	         
	        }  
	      }  
	    });  
	    thread.start();  
	  }  
	  
	  @Override  
	  public void contextDestroyed(ServletContextEvent servletContextEvent) {  
	    thread.interrupt();  
	  }  
	  
	  /**
	   * 从抢购列表中，把指定的图书数量减少一个
	   * 记日志
	   * 返回N>=0，抢购成功，N表示图书剩余数量
	   * 返回<0，表示抢购失败,没有库存了
	   * @param isbn
	   * @throws Exception
	   */
	  private int bookDecr(String isbn) throws Exception{		  
		  
		  Jedis jedis = RedisUtil.getJedis(); 
	      String book = jedis.hget("AuctionBooks", isbn);
	      String bcString = book.substring(1, book.length()-1);
		  JSONObject jsonObject = JSONObject.fromObject(bcString);
		  BookJson bcJson  = (BookJson) JSONObject.toBean(jsonObject,BookJson.class); 
		  int bkCount = bcJson.getBkCount();
		  if(bkCount>0){
			  bkCount = bkCount-1;
			  bcJson.setBkCount(bkCount);
			  JSONArray jsonArray = JSONArray.fromObject(bcJson); 			
			  String bcNewString = jsonArray.toString();
			  System.out.println("bcNewString=" + bcNewString);
			  jedis.hset("AuctionBooks", isbn, bcNewString);              
		  }else{
			  bkCount = -1;
		  }
	      RedisUtil.returnResource(jedis); 
	      
	      return bkCount;
	  }
}
