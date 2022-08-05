import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MemoDialog extends JDialog implements ActionListener{

	    //月份和年份及日期下拉 列表框
		private JComboBox MonthBox = new JComboBox();
		private JComboBox YearBox = new JComboBox();
		private JComboBox DayBox = new JComboBox();
		
	    //年份月份及日期标签
		private JLabel YearLabel = new JLabel("年份：");
		private JLabel MonthLabel = new JLabel("月份：");
		private JLabel DayLabel = new JLabel("日期：");
		
	    //确定按钮
		private JButton button_ok = new JButton("确认");
		
		//用户输入框
		private JTextArea inputArea = new JTextArea(10,30); 
		private JScrollPane inputPane=new JScrollPane(inputArea);
		//获取今天的日期、年份和月份
		private Date now_date = new Date();
		private int now_year = now_date.getYear() + 1900;
		private int now_month = now_date.getMonth();
		private int now_day = now_date.getDate();
		private String year_int = null;
		private int month_int;
		private int day_int;
		private String[] Things;
		
		// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
	    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	    final String DB_URL = "jdbc:mysql://localhost:3306/db_push";
	    // 数据库的用户名与密码，需要根据自己的设置
	    final String USER = "root";
	    final String PASS = "Jayqyh1230";
	  
		//构造函数
		public MemoDialog(CalendarTrain frame){
			super(frame);
			this.setTitle("添加备忘录");
			this.init();
			this.setLocation(650, 400);
			this.setResizable(false);
			pack();
		}
		//初始化日历
		private void init() {
			Font font = new Font("宋体",Font.BOLD,16);
			YearLabel.setFont(font);
			MonthLabel.setFont(font);
			button_ok.setFont(font);
			//设置文本框
			inputArea.setFont(font);
			inputArea.setLineWrap(true);
			Dimension size=inputArea.getPreferredSize();    //获得文本域的首选大小
	        inputPane.setBounds(110,90,size.width,size.height);
			
			 
			//今天--未来100年
			for(int i = now_year;i <= now_year + 100;i++){
				YearBox.addItem(i+"");
			}
			YearBox.setSelectedIndex(0);
			
			for(int i = 1;i < 13;i++){
				MonthBox.addItem(i+"");
			}
			MonthBox.setSelectedIndex(now_month);
			
			//放置下拉列表框和控制按钮的面板
			JPanel panel_ymd = new JPanel();
			panel_ymd.add(YearLabel);
			panel_ymd.add(YearBox);
			
			panel_ymd.add(MonthLabel);
			panel_ymd.add(MonthBox);
			
			panel_ymd.add(DayLabel);
			panel_ymd.add(DayBox);
			
			panel_ymd.add(button_ok);
			
			//为按钮添加时间监听器
			button_ok.addActionListener(this);
			YearBox.addActionListener(this);
			MonthBox.addActionListener(this);
			
			//用户输入的面板
			JPanel panel_input = new JPanel();
			panel_input.setBorder(new TitledBorder(new EtchedBorder(), "输入窗口"));
			panel_input.add(inputPane);
			
			this.paintDay();
			//设置总布局
			JPanel panel_main = new JPanel();
			panel_main.setLayout(new BorderLayout());
			panel_main.add(panel_input,BorderLayout.SOUTH);
			panel_main.add(panel_ymd,BorderLayout.NORTH);
			getContentPane().add(panel_main);
				
		}
		private void paintDay() {
			year_int = YearBox.getSelectedItem().toString();
			month_int = MonthBox.getSelectedIndex();		
			int year_sel = Integer.parseInt(year_int) - 1900;
			Date firstDay = new Date(year_sel, month_int, 1);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(firstDay);//用给定的日期设置Calendar的当前时间。
			int days = 0;//此月有多少天
			if(month_int == 0||month_int == 2||month_int == 4||month_int == 6
					||month_int == 7||month_int == 9||month_int == 11){
				days = 31;
			}else if(month_int == 3||month_int == 5||month_int == 8||month_int == 10){
				days = 30;
			}else{
				if(cal.isLeapYear(year_sel)){//是否为闰年
					days = 29;
				}else{
					days = 28;
				}
			}
			DayBox.removeAllItems();
			for(int i = 1;i < days+1;i++){
				
				DayBox.addItem(i+"");
			}
			DayBox.setSelectedIndex(now_day-1);
			
		}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		year_int = YearBox.getSelectedItem().toString();
		month_int = MonthBox.getSelectedIndex()+1;
		day_int = DayBox.getSelectedIndex()+1;
		if(e.getSource()==MonthBox){
			this.paintDay();
		}else if(e.getSource()==YearBox){
			this.paintDay();
		}else if(e.getSource()==button_ok){
			InfoTable IT = new InfoTable();
			if(IT.isConnect("www.baidu.com"))
			{
				if(inputArea.getText().equals("")) 
				{
					JOptionPane.showMessageDialog(null, "事件为空");
				}else {
				//如果网络正常
				System.out.println("11111111");
				Connection conn = null;
		        Statement stmt = null;
		        try{
		            // 注册 JDBC 驱动
		            Class.forName(JDBC_DRIVER);
		        
		            // 打开链接
		            System.out.println("连接数据库...");
		            conn = DriverManager.getConnection(DB_URL,USER,PASS);
		        
		            // 执行插入
		            System.out.println(" 实例化Statement对象...");
		            stmt = conn.createStatement();
		            String sql;
		            sql =  "insert into websites(year,month,day,thing) values(?,?,?,?)"; // 生成一条sql语句
		         // 创建一个Statement对象
		            PreparedStatement ps = conn.prepareStatement(sql);
		            StringBuffer SB = new StringBuffer();
		            Things = inputArea.getText().split("/n");//用'/n'来分隔字符串
					for (String str : Things) {
							SB.append(str);
							}
					String thing = SB.toString();	
					System.out.println(thing);
		            // 展开结果集数据库
		         // 为sql语句中第一个问号赋值
		            ps.setString(1, year_int);
		            // 为sql语句中第二个问号赋值
		            ps.setString(2, month_int+"");
		            // 为sql语句中第三个问号赋值
		            ps.setString(3, day_int+"");
		            // 为sql语句中第四个问号赋值
		            ps.setString(4, thing);
		            // 执行sql语句
		            ps.executeUpdate();
		            // 完成后关闭
		            ps.close();
		            stmt.close();
		            conn.close();
		            inputArea.setText("");
					JOptionPane.showMessageDialog(null, "添加成功");
		        }catch(SQLException se){
		            // 处理 JDBC 错误
		            se.printStackTrace();
		        }catch(Exception e1){
		            // 处理 Class.forName 错误
		            e1.printStackTrace();
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
		    }}
				
				
				
				
			else
			{
				//如果网络没有连接
				
			Things = inputArea.getText().split("/n");//用'/n'来分隔字符串
			if(inputArea.getText().equals("")) 
			{
				JOptionPane.showMessageDialog(null, "事件为空");
			}else {
				try {
					BufferedWriter MemoW = new BufferedWriter(new FileWriter("src/MemoThings.txt",true));
					MemoW.write(year_int);
					MemoW.write("|");
					MemoW.write(month_int+"");
					MemoW.write("|");
					MemoW.write(day_int+"");
					MemoW.write("|");
					for (String str : Things) {
						MemoW.write(str);
						//MemoW.newLine();
						MemoW.flush();
						inputArea.setText("");
						JOptionPane.showMessageDialog(null, "添加成功");
						
						}
					MemoW.write("\r\n");
					MemoW.close();
				}catch(Exception Ex){}
			}
			
			
		}
		}
	}}
	


