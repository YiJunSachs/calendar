import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import java.io.InputStream;    
import java.io.InputStreamReader;   
import java.sql.*;

public class InfoTable {

	public Vector<Thing> MemoThings = new Vector<Thing>();
	public int ThingsCount = 0;

	public void deleteAllOnline()
	{
		// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
	    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	    final String DB_URL = "jdbc:mysql://localhost:3306/db_push";

	    // 数据库的用户名与密码，需要根据自己的设置
	    final String USER = "root";
	    final String PASS = "Jayqyh1230";
	 
	    Connection conn = null;
	    Statement stmt = null;
	        try{
	            // 注册 JDBC 驱动
	            Class.forName(JDBC_DRIVER);
	        
	            // 打开链接
	            System.out.println("连接数据库...");
	            conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        
	            // 执行查询
	            System.out.println(" 实例化Statement对象...");
	            stmt = conn.createStatement();
	            String sql;
	            sql = "TRUNCATE TABLE websites";
	            stmt.executeUpdate(sql);
	            // 完成后关闭
	            stmt.close();
	            conn.close();
	        }catch(SQLException se){
	            // 处理 JDBC 错误
	            se.printStackTrace();
	        }catch(Exception e){
	            // 处理 Class.forName 错误
	            e.printStackTrace();
	        }finally{
	            // 关闭资源
	            try{
	                if(stmt!=null) stmt.close();
	            }catch(SQLException se2){
	            }// 什么都不做
	            try{
	                if(conn!=null) conn.close();
	            }catch(SQLException se){
	                se.printStackTrace();
	            }
	        }
	        System.out.println("Goodbye!");
	    }
	
	
	public void readThingOnline() {
		if(!this.isConnect("www.baidu.com"))
		{
			JOptionPane.showMessageDialog(null, "网络未连接");
		}
		else {
		// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
	    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	    final String DB_URL = "jdbc:mysql://localhost:3306/db_push";
	 
	    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
	    //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	    //static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
	 
	 
	    // 数据库的用户名与密码，需要根据自己的设置
	    final String USER = "root";
	    final String PASS = "Jayqyh1230";
	 
	    Connection conn = null;
	    Statement stmt = null;
	        try{
	            // 注册 JDBC 驱动
	            Class.forName(JDBC_DRIVER);
	        
	            // 打开链接
	            System.out.println("连接数据库...");
	            conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        
	            // 执行查询
	            System.out.println(" 实例化Statement对象...");
	            stmt = conn.createStatement();
	            String sql;
	            sql = "SELECT id, year, month,day,thing FROM websites";
	            ResultSet rs = stmt.executeQuery(sql);
	        
	            // 展开结果集数据库
	            while(rs.next()){
	                // 通过字段检索
	            	Thing thing = new Thing();
	            	int id  = rs.getInt("id");
	                String year = rs.getString("year");
	                String month = rs.getString("month");
	                String day = rs.getString("day");
	                String thingToBedo = rs.getString("thing");
	                thing.Year = year;
	                thing.Month = month;
	                thing.Day = day;
	                thing.Thing = thingToBedo;
	                MemoThings.add(thing);
	                ThingsCount++;
	            }
	            // 完成后关闭
	            rs.close();
	            stmt.close();
	            conn.close();
	        }catch(SQLException se){
	            // 处理 JDBC 错误
	            se.printStackTrace();
	        }catch(Exception e){
	            // 处理 Class.forName 错误
	            e.printStackTrace();
	        }finally{
	            // 关闭资源
	            try{
	                if(stmt!=null) stmt.close();
	            }catch(SQLException se2){
	            }// 什么都不做
	            try{
	                if(conn!=null) conn.close();
	            }catch(SQLException se){
	                se.printStackTrace();
	            }
	        }
	        System.out.println("Goodbye!");
	    }
		}
	
	public void readThing() {
		try {
			 BufferedReader in = new BufferedReader(new FileReader("src/MemoThings.txt"));
			 String buf;
			 while(true)
			 {
				 String[] sst; 
				 buf = in.readLine();
				 if(buf == null)
				 break;
				 else {
					Thing thing = new Thing();
					sst = buf.split("\\|");
					thing.Year = sst[0];
					//System.out.println(thing.Year);
					thing.Month = sst[1];
				//	System.out.println(thing.Month);
					thing.Day = sst[2];
					//System.out.println(thing.Day);
					thing.Thing = sst[3];
					MemoThings.add(thing);
					ThingsCount++;
				 }
			 }
			 in.close();
			 
		 }catch(IOException e){}
		
	}
	//判断网络是否连接
	public boolean isConnect(String ip){
	    boolean connect = false;    
	    Runtime runtime = Runtime.getRuntime();    
	    Process process;    
	    try {    
	        process = runtime.exec("ping " + ip);    
	        InputStream is = process.getInputStream();     
	        InputStreamReader isr = new InputStreamReader(is);     
	        BufferedReader br = new BufferedReader(isr);     
	        String line = null;     
	        StringBuffer sb = new StringBuffer();     
	        while ((line = br.readLine()) != null) {     
	            sb.append(line);     
	        }        
	        is.close();     
	        isr.close();     
	        br.close();     
	 
	        if (null != sb && !sb.toString().equals("")) {     
	            String logString = "";     
	            if (sb.toString().indexOf("TTL") > 0) {     
	                // 网络畅通      
	                connect = true;    
	            } else {     
	                // 网络不畅通      
	                connect = false;    
	            }     
	        }     
	    } catch (IOException e) {    
	        e.printStackTrace();    
	    }     
	    return connect;    
	} 
}





   
