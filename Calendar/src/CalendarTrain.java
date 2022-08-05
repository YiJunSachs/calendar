import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
 
 
public class CalendarTrain extends JFrame implements ActionListener{
	//月份和年份下拉 列表框
	private JComboBox MonthBox = new JComboBox();
	private JComboBox YearBox = new JComboBox();
	
	//年份月份标签
	private JLabel YearLabel = new JLabel("年份：");
	private JLabel MonthLabel = new JLabel("月份：");
	
	//确定和今天按钮
	private JButton button_ok = new JButton("查看");
	private JButton button_today = new JButton("今天");
	
	
	//获取今天的日期、年份和月份
	private Date now_date = new Date();
	
	private int now_year = now_date.getYear() + 1900;
	private int now_month = now_date.getMonth();
	//判断是否显示今天的参数
	private boolean todayFlag = false;
	
	//用一组按钮显示日期，一共7行7列。第一行是星期
	private JButton[] button_day = new JButton[42];
	private final String[] week = {"SUN","MON","TUE","WEN","THR","FRI","SAT"};
	private JButton[] button_week = new JButton[7];
	private String year_int = null;
	private int month_int;
	//用两个按钮来添加备忘录和查看备忘录
	private JButton button_add = new JButton("添加备忘录");
	private JButton button_check = new JButton("查看备忘录");
	private JButton button_update = new JButton("同步数据");
	/*构造函数*/
	public CalendarTrain(){
		super();
		this.setTitle("日历");
		this.init();
		this.setLocation(500, 300);
 
		this.judgeToday();
		this.setResizable(true);
		pack();
		
	}
	
	//初始化日历
	private void init() {
		Font font = new Font("宋体",Font.BOLD,16);
		YearLabel.setFont(font);
		MonthLabel.setFont(font);
		button_ok.setFont(font);
		button_today.setFont(font);
		//过去20年--未来100年
		for(int i = now_year - 20;i <= now_year + 100;i++){
			YearBox.addItem(i+"");
		}
		YearBox.setSelectedIndex(20);
		
		for(int i = 1;i < 13;i++){
			MonthBox.addItem(i+"");
		}
		MonthBox.setSelectedIndex(now_month);
		
		//放置下拉列表框和控制按钮的面板
		JPanel panel_ym = new JPanel();
		panel_ym.add(YearLabel);
		panel_ym.add(YearBox);
		panel_ym.add(MonthLabel);
		panel_ym.add(MonthBox);
		panel_ym.add(button_ok);
		panel_ym.add(button_today);
		
		//为两个按钮添加时间监听器
		button_ok.addActionListener(this);
		button_today.addActionListener(this);
		
		//两个备忘录按钮的面板
		JPanel panel_memo = new JPanel();
		panel_memo.add(button_add);
		panel_memo.add(button_check);
		panel_memo.add(button_update);
		
		//为两个按钮添加时间监听器
		button_add.addActionListener(this);
		button_check.addActionListener(this);
		button_update.addActionListener(this);
		
		//显示日期的面板
		JPanel panel_day = new JPanel();
		//7*7
		panel_day.setLayout(new GridLayout(7, 7, 3, 3));
		for(int i = 0; i < 7; i++) {
			button_week[i] = new JButton(" ");
			button_week[i].setText(week[i]);
			button_week[i].setForeground(Color.black);
			panel_day.add(button_week[i]);
		}
		button_week[0].setForeground(Color.red);
		button_week[6].setForeground(Color.red);
		
		for(int i = 0; i < 42;i++){
			button_day[i] = new JButton(" ");
			panel_day.add(button_day[i]);
		}
		
		this.paintDay();//显示当前日期
		
		//设置总布局
		JPanel panel_main = new JPanel();
		panel_main.setLayout(new BorderLayout());
		panel_main.add(panel_memo,BorderLayout.SOUTH);
		panel_main.add(panel_day,BorderLayout.CENTER);
		panel_main.add(panel_ym,BorderLayout.NORTH);
		getContentPane().add(panel_main);
			
	}
	//判断当天是否有待办事项的函数
	private void judgeToday()
	{
		InfoTable IT = new InfoTable();
		if(IT.isConnect("www.baidu.com"))
		{
			IT.readThingOnline();
		}else
		{
		    IT.readThing();
		}
		String Today_year = now_year+"";
		String Today_month = now_month+1+"";
		String Today_day = now_date.getDate()+"";
		MemoRemindDialog remindDialog = new MemoRemindDialog(this);
		for(int i = 0;i<IT.ThingsCount;i++)
		{
			if(IT.MemoThings.elementAt(i).Year.contentEquals(Today_year))
				{
				if(IT.MemoThings.elementAt(i).Month.contentEquals(Today_month))
				{
					if(IT.MemoThings.elementAt(i).Day.contentEquals(Today_day))
					{
                      remindDialog.addTextArea(IT.MemoThings.elementAt(i).Thing);
                      remindDialog.addTextArea("\n");
                      remindDialog.setVisible(true);
						
					}}}else{
						remindDialog.addTextArea("今日无待办事项");
						}
		}
	}
 
	//显示天数的函数
	private void paintDay() {
		if(todayFlag){
			year_int = now_year +"";
			month_int = now_month;
		}else{
			year_int = YearBox.getSelectedItem().toString();
			month_int = MonthBox.getSelectedIndex();		
		}
		int year_sel = Integer.parseInt(year_int) - 1900;
		Date firstDay = new Date(year_sel, month_int, 1);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(firstDay);//用给定的日期设置Calendar的当前时间。
		int days = 0;//此月有多少天
		int day_week = 0;//第一天为周几
		
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
		
		day_week = firstDay.getDay();//返回为周几
		int count = 1;
		
		for(int i = day_week;i<day_week+days;count++,i++){
			if(i%7 == 0||(i+1)%7 == 0){
				if((i == day_week+now_date.getDate()-1)&& month_int==now_month && (year_sel == now_year-1900)){
					button_day[i].setForeground(Color.BLUE);
					button_day[i].setText(count+"");
				}else{
					button_day[i].setForeground(Color.RED);
					button_day[i].setText(count+"");
				}
			}else{
				if((i == day_week+now_date.getDate()-1)&& month_int==now_month && (year_sel == now_year-1900)){
					button_day[i].setForeground(Color.BLUE);
					button_day[i].setText(count+"");
				}else{
					button_day[i].setForeground(Color.BLACK);
					button_day[i].setText(count+"");
				}
			}
			
		}
		if(day_week == 0){
			for(int i = days;i<42;i++){
				button_day[i].setText("");
			}
		}else{
			for(int i = 0;i<day_week;i++){
				button_day[i].setText("");
			}
			for(int i=day_week+days;i<42;i++){
				button_day[i].setText("");
			}
		}
		
		
	}
 
	
 
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button_ok){
			todayFlag=false;
			this.paintDay();
		}else if(e.getSource()==button_today){
			todayFlag=true;
			YearBox.setSelectedIndex(20);
			MonthBox.setSelectedIndex(now_month);
			this.paintDay();
		}else if(e.getSource()==button_add) {
			MemoDialog Dialog1 = new MemoDialog(this);
			Dialog1.setVisible(true);
		}else if(e.getSource()==button_check) {
			MemoViewDialog Dialog2 = new MemoViewDialog(this);
			Dialog2.setVisible(true);
		}else if(e.getSource()==button_update) {
			//同步本地至数据库
			InfoTable IT_local = new InfoTable();
			if(!IT_local.isConnect("www.baidu.com"))
			{
				JOptionPane.showMessageDialog(null, "网络未连接");
			}
			else
			{
				// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
			    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
			    final String DB_URL = "jdbc:mysql://localhost:3306/db_push";

			    // 数据库的用户名与密码，需要根据自己的设置
			    final String USER = "root";
			    final String PASS = "Jayqyh1230";
				IT_local.readThing();
				IT_local.deleteAllOnline();
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
		            for(int i = 0;i<IT_local.ThingsCount;i++)
		            { 
		            PreparedStatement ps = conn.prepareStatement(sql);
		            StringBuffer SB = new StringBuffer();
		            // 展开结果集数据库
		            // 为sql语句中第一个问号赋值
		            ps.setString(1, IT_local.MemoThings.elementAt(i).Year);
		            // 为sql语句中第二个问号赋值
		            ps.setString(2,IT_local.MemoThings.elementAt(i).Month);
		            // 为sql语句中第三个问号赋值
		            ps.setString(3, IT_local.MemoThings.elementAt(i).Day);
		            // 为sql语句中第四个问号赋值
		            ps.setString(4, IT_local.MemoThings.elementAt(i).Thing);
		            // 执行sql语句
		            ps.executeUpdate();
		            // 完成后关闭
		            ps.close();
		            }
		            stmt.close();
		            conn.close();
					JOptionPane.showMessageDialog(null, "同步完成");
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
				
			}
			
			
			
		}
		
	}


	
	public static void main(String[] args) {
		CalendarTrain ct = new CalendarTrain();
		ct.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) {
			     //此处加入你要的操作代码
				InfoTable IT = new InfoTable();
				if(IT.isConnect("www.baidu.com"))
				{
				 
					IT.readThingOnline();
					try {
					BufferedWriter MemoW = new BufferedWriter(new FileWriter("src/MemoThings.txt",false));
					for(int i = 0;i<IT.ThingsCount;i++)
					{
						MemoW.write(IT.MemoThings.elementAt(i).Year);
						MemoW.write("|");
						MemoW.write(IT.MemoThings.elementAt(i).Month);
						MemoW.write("|");
						MemoW.write(IT.MemoThings.elementAt(i).Day);
						MemoW.write("|");
						MemoW.write(IT.MemoThings.elementAt(i).Thing);
						MemoW.write("\r\n");
						MemoW.flush();
					}
					MemoW.close();
				}catch(Exception Ex){}
				
				JOptionPane.showMessageDialog(null, "已将数据库同步至本地");
				System.exit(0);
				}
			   }
		});
		ct.setVisible(true);
 
	}
}

