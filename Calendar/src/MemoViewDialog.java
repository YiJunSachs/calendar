import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class MemoViewDialog extends JDialog implements ActionListener{

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
			
			//用户查看框
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
			//构造函数
			public MemoViewDialog(CalendarTrain frame){
				super(frame);
				this.setTitle("查看备忘录");
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
				inputArea.setEditable(false);
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
				
				//用户查看的面板
				JPanel panel_input = new JPanel();
				panel_input.setBorder(new TitledBorder(new EtchedBorder(), "待办事项"));
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
			if(e.getSource()==MonthBox){
				this.paintDay();
			}else if(e.getSource()==YearBox){
				this.paintDay();
			}else if(e.getSource()==button_ok){
				inputArea.setText("");
				String Syear = YearBox.getSelectedItem().toString();
				int Smonth = MonthBox.getSelectedIndex()+1;
				int Sday = DayBox.getSelectedIndex()+1;
				//System.out.println(Syear);
				//System.out.println(Smonth+"");
				//System.out.println(Sday+"");
				InfoTable IT = new InfoTable();
				if(IT.isConnect("www.baidu.com"))
				{
					IT.readThingOnline();
				}else
				{
				    IT.readThing();
				}
				for(int i = 0;i<IT.ThingsCount;i++)
				{
					//System.out.println(IT.MemoThings.elementAt(i).Day);
					//System.out.println(IT.MemoThings.elementAt(i).Month);
					//System.out.println(IT.MemoThings.elementAt(i).Year);
					//System.out.println(Sday+"");
					//System.out.println(Smonth+"");
					//System.out.println(Syear);
					if(IT.MemoThings.elementAt(i).Year.contentEquals(Syear))
						{
						if(IT.MemoThings.elementAt(i).Month.contentEquals(Smonth+""))
						{
							if(IT.MemoThings.elementAt(i).Day.contentEquals(Sday+""))
							{
								inputArea.append(IT.MemoThings.elementAt(i).Thing);
								inputArea.append("\n");
								
							}}}
				}
				
				
			}
		}
		

}
