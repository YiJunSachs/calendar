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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MemoRemindDialog extends JDialog implements ActionListener{
	
	
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
	public MemoRemindDialog(CalendarTrain frame){
		super(frame);
		this.setTitle("今日待办事项");
		this.init();
		this.setLocation(650, 400);
		this.setResizable(false);
		pack();
	}
	//向TextArea中添加待办事项
	void addTextArea(String a)
	{
		inputArea.append(a);
	}
	//初始化日历
	private void init() {
		Font font = new Font("宋体",Font.BOLD,16);
		button_ok.setFont(font);
		//设置文本框
		inputArea.setFont(font);
		inputArea.setLineWrap(true);
		inputArea.setEditable(false);
		Dimension size=inputArea.getPreferredSize();    //获得文本域的首选大小
        inputPane.setBounds(110,90,size.width,size.height);
        
		//放置确认按钮的面板
		JPanel panel_ok = new JPanel();

		panel_ok.add(button_ok);
		
		//为按钮添加时间监听器
		button_ok.addActionListener(this);
		
		//用户查看的面板
		JPanel panel_input = new JPanel();
		panel_input.setBorder(new TitledBorder(new EtchedBorder(), "待办事项"));
		panel_input.add(inputPane);
		
		//设置总布局
		JPanel panel_main = new JPanel();
		panel_main.setLayout(new BorderLayout());
		panel_main.add(panel_input,BorderLayout.NORTH);
		panel_main.add(panel_ok,BorderLayout.SOUTH);
		getContentPane().add(panel_main);
			
	}

@Override
public void actionPerformed(ActionEvent e) {
	// TODO 自动生成的方法存根
	if(e.getSource()==button_ok){
		this.dispose();
	}
}


}

