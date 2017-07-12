package neojavawallet;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.List;
import javax.swing.*;

public class MainClass {

	public static void main(String[] args) {
		TFFrame f = new MainClass.TFFrame();	
		f.Init();
	}
	static class HttpTool
	{
	    public static String sendGet(String url) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }
	}
	@SuppressWarnings("serial")
	static class TFFrame extends JFrame {
		TFFrame() {

		}
		
		public void Init()
		{
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置关闭动作
			this.setVisible(true);
			this.setSize(800,600);
			//this.setLayout(new FlowLayout());
			this.setLayout(new GridLayout(2,2,5,5));
			//GridLayout grid =new GridLayout(3,3,5,5);
			
			this.InitPanel01();			
			this.InitPanel02();
			this.InitPanel03();
			this.InitPanel04();

		}
		JPanel panel01 = null;//new JPanel();
		JPanel panel02 = null;//new JPanel();
		JPanel panel03 = null;//new JPanel();
		JPanel panel04 = null;//new JPanel();
		void InitPanel01()
		{
			this.panel01 =new JPanel();
			this.add(panel01);
			panel01.setBorder(BorderFactory.createTitledBorder("查询账户"));
			panel01.setLayout(new GridLayout(10,3,5,5));
			
			//3 个一行，摆吧
			
			TextField tf = new TextField();
			panel01.add(tf);
			
			TextField tf2 = new TextField();
			panel01.add(tf2);
			
			Button btn2 = new Button("Button2");
			panel01.add(btn2);
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			for(int i=0;i<9;i++)
			{
				panel01.add(new JPanel());
				panel01.add(new JPanel());
				panel01.add(new JPanel());
			}
		}
		void InitPanel02()
		{
			this.panel02 =new JPanel();
			this.add(panel02);
			panel02.setBorder(BorderFactory.createTitledBorder("panel02"));
			panel02.setLayout(new GridLayout(10,3,5,5));			
		}
		void InitPanel03()
		{
			this.panel03 =new JPanel();
			this.add(panel03);
			panel03.setBorder(BorderFactory.createTitledBorder("panel03"));
			panel03.setLayout(new GridLayout(10,3,5,5));			
		}		
		void InitPanel04()
		{
			this.panel04 =new JPanel();
			this.add(panel04);
			panel04.setBorder(BorderFactory.createTitledBorder("panel04"));
			panel04.setLayout(new GridLayout(10,3,5,5));			
		}	
	}


}
