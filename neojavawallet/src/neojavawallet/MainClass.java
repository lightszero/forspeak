package neojavawallet;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainClass
{

	public static void main(String[] args)
	{
		TFFrame f = new MainClass.TFFrame();
		f.Init();
	}

	static class HttpTool
	{
		public static String api = "http://api.otcgo.cn/testnet";

		public static String sendGet(String url)
		{

			String result = "";
			BufferedReader in = null;
			try
			{
				String urlNameString = url;
				URL realUrl = new URL(urlNameString);
				// 打开和URL之间的连接
				URLConnection connection = realUrl.openConnection();
				// 设置通用的请求属性
				connection.setRequestProperty("accept", "*/*");
				connection.setRequestProperty("connection", "Keep-Alive");
				connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				// 建立实际的连接
				connection.connect();
				// 获取所有响应头字段
				Map<String, List<String>> map = connection.getHeaderFields();
				// 遍历所有的响应头字段
				for (String key : map.keySet())
				{
					System.out.println(key + "--->" + map.get(key));
				}
				// 定义 BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null)
				{
					result += line;
				}
			} catch (Exception e)
			{
				System.out.println("发送GET请求出现异常！" + e);
				e.printStackTrace();
				return null;
			}
			// 使用finally块来关闭输入流
			finally
			{
				try
				{
					if (in != null)
					{
						in.close();
					}
				} catch (Exception e2)
				{
					e2.printStackTrace();
				}
			}
			return result;
		}

		public static String sendPost(String url, String param)
		{
			PrintWriter out = null;
			BufferedReader in = null;
			String result = "";
			try
			{
				URL realUrl = new URL(url);
				// 打开和URL之间的连接
				URLConnection conn = realUrl.openConnection();
				// 设置通用的请求属性
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				// 发送POST请求必须设置如下两行
				conn.setDoOutput(true);
				conn.setDoInput(true);
				// 获取URLConnection对象对应的输出流
				out = new PrintWriter(conn.getOutputStream());
				// 发送请求参数
				out.print(param);
				// flush输出流的缓冲
				out.flush();
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null)
				{
					result += line;
				}
			} catch (Exception e)
			{
				System.out.println("发送 POST 请求出现异常！" + e);
				e.printStackTrace();
				return null;
			}
			// 使用finally块来关闭输出流、输入流
			finally
			{
				try
				{
					if (out != null)
					{
						out.close();
					}
					if (in != null)
					{
						in.close();
					}
				} catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
			return result;
		}
	}

	@SuppressWarnings("serial")
	static class TFFrame extends JFrame
	{
		TFFrame()
		{

		}

		public void Init()
		{
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置关闭动作
			this.setVisible(true);
			this.setSize(800, 600);
			// this.setLayout(new FlowLayout());
			this.setLayout(new GridLayout(2, 2, 5, 5));
			// GridLayout grid =new GridLayout(3,3,5,5);
			this.setTitle("这是一个JAVA实现的轻钱包，使用外部签名机");
			this.InitPanel01();
			this.InitPanel02();
			this.InitPanel03();
			this.InitPanel04();

		}

		JPanel panel01 = null;// new JPanel();
		JPanel panel02 = null;// new JPanel();
		JPanel panel03 = null;// new JPanel();
		JPanel panel04 = null;// new JPanel();

		String dataBeforeSign="";
		String dataSigned="";
		void InitPanel01()
		{
			this.panel01 = new JPanel();
			this.add(panel01);
			panel01.setBorder(BorderFactory.createTitledBorder("查询账户"));
			GridBagLayout layout = new GridBagLayout();
			panel01.setLayout(layout);

			// 3 个一行，摆吧
			{// 第一行，查询高度
				Label label = new Label();
				panel01.add(label);
				label.setText("当前高度");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}

				TextField tf = new TextField();
				panel01.add(tf);
				tf.setText("当前高度");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 3;
					s.weightx=1;
					layout.setConstraints(tf, s);
				}
				
				Button btn = new Button("Button2");
				panel01.add(btn);
				btn.setLabel("查询");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(btn, s);
				}
				
				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String str = HttpTool.sendGet(HttpTool.api + "/height");
						JsonParser parse = new JsonParser();
						JsonObject json = (JsonObject) parse.parse(str);
						if (json.has("height"))
						{
							int height = json.get("height").getAsInt();
							tf.setText("height=" + height);

						} else
						{
							tf.setText("getheight error");
						}
					}
				});
			}
			final JTextArea area =new JTextArea();
			{// 第二行，查地址
				Label label = new Label();
				panel01.add(label);
				label.setText("查地址");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}
				
				TextField tf = new TextField();
				panel01.add(tf);
				tf.setText("ASJiT56jYKZgTN9FtGvjRenbdBKTZ98QVw");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 3;
					s.weightx=1;
					layout.setConstraints(tf, s);
				}
				
				Button btn = new Button("Button2");
				panel01.add(btn);
				btn.setLabel("查询");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(btn, s);
				}
				
				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						String str = HttpTool.sendGet(HttpTool.api + "/address/"+tf.getText());
						JsonParser parse = new JsonParser();
						JsonObject json = (JsonObject) parse.parse(str);
						String outstr="";
						if(json.has("_id")==false)
						{
							outstr+="空钱包\n";
						}
						else
						{
						
							outstr+="address:"+json.get("_id").getAsString()+"\n";
							outstr+="财产\n";
							JsonObject balances= json.get("balances").getAsJsonObject();
							
							Iterator<Entry<String,JsonElement>> it =balances.entrySet().iterator();

							while(it.hasNext())
							{
								Entry<String,JsonElement> ent =it.next();
								String key =ent.getKey();
								
								if(key.equals("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b"))
								{
									outstr+="小蚁股:"+ent.getValue().getAsInt()+"\n";
								}
								else if(key.equals("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7"))
								{
									outstr+="小蚁币:"+ent.getValue().getAsFloat()+"\n";
								}
								
							}

						}
						area.setText(outstr);
						
						
					}
				});
			}
			{// 第三行，查地址2
				Label label = new Label();
				panel01.add(label);
				label.setText("查地址");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}
				
				TextField tf = new TextField();
				panel01.add(tf);
				tf.setText("AJEhCABbWoKGTQS2rxhUG8Ksda9vsV9Esn");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 3;
					s.weightx=1;
					layout.setConstraints(tf, s);
				}
				
				Button btn = new Button("Button2");
				panel01.add(btn);
				btn.setLabel("查询");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(btn, s);
				}
				
				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						String str = HttpTool.sendGet(HttpTool.api + "/address/"+tf.getText());
						JsonParser parse = new JsonParser();
						JsonObject json = (JsonObject) parse.parse(str);
						String outstr="";
						if(json.has("_id")==false)
						{
							outstr+="空钱包\n";
						}
						else
						{
						
							outstr+="address:"+json.get("_id").getAsString()+"\n";
							outstr+="财产\n";
							JsonObject balances= json.get("balances").getAsJsonObject();
							if(balances.has("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b"))
							{
								outstr+="小蚁股:"+balances.get("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").getAsInt()+"\n";
							}
							if(balances.has("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7"))
							{
								outstr+="小蚁币:"+balances.get("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7").getAsFloat()+"\n";
							}
						}
						area.setText(outstr);
						
						
					}
				});
			}
			{// 第四行，查地址3
				Label label = new Label();
				panel01.add(label);
				label.setText("查地址");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}
				
				TextField tf = new TextField();
				panel01.add(tf);
				tf.setText("AGTBUrvtrvDrKccv2c5n5Z9iHxz827WmpM");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 3;
					s.weightx=1;
					layout.setConstraints(tf, s);
				}
				
				Button btn = new Button("Button2");
				panel01.add(btn);
				btn.setLabel("查询");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(btn, s);
				}
				
				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						String str = HttpTool.sendGet(HttpTool.api + "/address/"+tf.getText());
						JsonParser parse = new JsonParser();
						JsonObject json = (JsonObject) parse.parse(str);
						String outstr="";
						if(json.has("_id")==false)
						{
							outstr+="空钱包\n";
						}
						else
						{
						
							outstr+="address:"+json.get("_id").getAsString()+"\n";
							outstr+="财产\n";
							JsonObject balances= json.get("balances").getAsJsonObject();
							if(balances.has("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b"))
							{
								outstr+="小蚁股:"+balances.get("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").getAsInt()+"\n";
							}
							if(balances.has("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7"))
							{
								outstr+="小蚁币:"+balances.get("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7").getAsFloat()+"\n";
							}
						}
						area.setText(outstr);
						
						
					}
				});
			}
			{

				panel01.add(area);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.gridheight =5;
					s.weightx=0;
					s.weighty=1;
					layout.setConstraints(area, s);
				}
			}
		}

		void InitPanel02()
		{
			JPanel panel =new JPanel();
			this.panel02 = panel;
			this.add(panel);
			panel.setBorder(BorderFactory.createTitledBorder("构造交易"));
			GridBagLayout layout = new GridBagLayout();
			
			TextField tfsrc = new TextField();
			TextField tfdest = new TextField();
			TextField tfassetid = new TextField();
			TextField tfnumber = new TextField();
			JTextArea area =new JTextArea();
			panel.setLayout(layout);
			{// 第一行
				Label label = new Label();
				panel.add(label);
				label.setText("源地址");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}

				
				panel.add(tfsrc);
				tfsrc.setText("ASJiT56jYKZgTN9FtGvjRenbdBKTZ98QVw");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 5;
					s.weightx=1;
					layout.setConstraints(tfsrc, s);
				}

				JPanel empty = new JPanel();
				panel.add(empty);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(empty, s);
				}
			}
			{// 第二行
				Label label = new Label();
				panel.add(label);
				label.setText("目标地址");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}

				
				panel.add(tfdest);
				tfdest.setText("AJEhCABbWoKGTQS2rxhUG8Ksda9vsV9Esn");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 5;
					s.weightx=1;
					layout.setConstraints(tfdest, s);
				}

				JPanel empty = new JPanel();
				panel.add(empty);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(empty, s);
				}
			}
			{// 第3行
				Label label = new Label();
				panel.add(label);
				label.setText("资产id");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}

				
				panel.add(tfassetid);
				tfassetid.setText("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 5;
					s.weightx=1;
					layout.setConstraints(tfassetid, s);
				}

				JPanel empty = new JPanel();
				panel.add(empty);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(empty, s);
				}
			}
			{// 第4行
				Label label = new Label();
				panel.add(label);
				label.setText("资产数量");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}

				
				panel.add(tfnumber);
				tfnumber.setText("0.1");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 5;
					s.weightx=1;
					layout.setConstraints(tfnumber, s);
				}


				Button btn = new Button("Button2");
				panel.add(btn);
				btn.setLabel("生成");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(btn, s);
				}
				
				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String param ="source="+tfsrc.getText();
						param +="&assetId="+tfassetid.getText();
						param +="&dests=" +tfdest.getText();
						param +="&amounts=" + tfnumber.getText();
						String str = HttpTool.sendPost(HttpTool.api + "/transfer",param);
						JsonParser parse = new JsonParser();
						JsonObject json = (JsonObject) parse.parse(str);
						
						String outstr="error.";
						if(json.has("transaction"))
						{
							outstr=json.get("transaction").getAsString();
							dataBeforeSign=outstr;	
						}
						area.setText(outstr);
						
						
					}
				});
			}
			{//第5行
				area.setLineWrap(true);
				panel.add(area);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.gridheight =5;
					s.weightx=0;
					s.weighty=1;
					layout.setConstraints(area, s);
				}
			}
		}

		void InitPanel03()
		{
			JPanel panel =new JPanel();
			this.panel03 = panel;
			this.add(panel);
			panel.setBorder(BorderFactory.createTitledBorder("签名交易"));
			
			GridBagLayout layout = new GridBagLayout();

			panel.setLayout(layout);
			JTextArea area =new JTextArea();
			{// 第一行
				JTextArea label = new JTextArea();
				panel.add(label);
				label.setText("因为涉及一些加密签名的算法，JAVA刚学不到72小时，这个步骤我实在是完不成了，所以我直接演示通过外部签名机来进行签名，比如以后的硬件钱包，就是一种外部签名机");
				label.setLineWrap(true);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 5;
					s.weightx=1;
					layout.setConstraints(label, s);
				}

				JPanel empty = new JPanel();
				panel.add(empty);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(empty, s);
				}
			}
			{
				JPanel empty = new JPanel();
				panel.add(empty);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(empty, s);
				}
			}
			TextField tfsrc =new TextField();
			{// 第2行
				Label label = new Label();
				panel.add(label);
				label.setText("谁来签名");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}

				
				panel.add(tfsrc);
				tfsrc.setText("ASJiT56jYKZgTN9FtGvjRenbdBKTZ98QVw");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 5;
					s.weightx=1;
					layout.setConstraints(tfsrc, s);
				}

				JButton btn = new JButton();
				btn.setText("签名");
				panel.add(btn);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(btn, s);
					
					
					//http://localhost:8080/_api/ver
				}
				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String param ="source="+tfsrc.getText();
						param +="&data="+ dataBeforeSign;
						String str= HttpTool.sendPost("http://localhost:8080/_api/sign",param);
						if(str==null)
						{
							area.setText("硬件钱包未插入，请插入");
							return;
						}
						dataSigned=str;
						area.setText(str);
						
						
					}
				});
			}
			{//第3行
				area.setLineWrap(true);
				panel.add(area);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.gridheight =5;
					s.weightx=0;
					s.weighty=1;
					layout.setConstraints(area, s);
				}

			}
		}

		void InitPanel04()
		{
			JPanel panel =new JPanel();
			this.panel04 = panel;
			this.add(panel);
			panel.setBorder(BorderFactory.createTitledBorder("发送交易"));
			
			GridBagLayout layout = new GridBagLayout();

			panel.setLayout(layout);
			JTextArea area =new JTextArea();
			{// 第1行
				Label label = new Label();
				panel.add(label);
				label.setText("发送");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					s.weightx=1;
					layout.setConstraints(label, s);
				}


				JButton btn = new JButton();
				btn.setText("发送交易");
				panel.add(btn);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.weightx=0;
					layout.setConstraints(btn, s);
					
					
					//http://localhost:8080/_api/ver
				}
				btn.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String param =dataSigned;
						String str= HttpTool.sendPost(HttpTool.api +"/broadcast",param);
						if(str==null)
						{
							area.setText("发送交易失败");
							return;
						}
						area.setText(str);
											
					}
				});
			}
			{//第2行
				area.setLineWrap(true);
				panel.add(area);
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 0;
					s.gridheight =5;
					s.weightx=0;
					s.weighty=1;
					layout.setConstraints(area, s);
				}

			}
		}
	}

}
