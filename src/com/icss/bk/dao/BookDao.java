package com.icss.bk.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.icss.bk.entity.TBook;

public class BookDao extends BaseDao{
	
	/**
	 * 主页显示，所有图书信息
	 * @return
	 * @throws Exception
	 */
	public List<TBook> getAllBooks() throws Exception{
		List<TBook> books = null;
		
		String sql = "select isbn,bname,press,price,pdate,picurl from tbook order by isbn" ;
		this.openConnection();
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if(rs != null){
			books = new ArrayList<TBook>();
			while(rs.next()){
				TBook bk = new TBook();
				bk.setBname(rs.getString("bname"));
				bk.setIsbn(rs.getString("isbn"));
				bk.setPdate(rs.getDate("pdate"));
				bk.setPress(rs.getString("press"));
				bk.setPrice(rs.getDouble("price"));
				bk.setPicurl(rs.getString("picurl"));
				books.add(bk);
			}
			
		}
		
		return books;
	}	


}
