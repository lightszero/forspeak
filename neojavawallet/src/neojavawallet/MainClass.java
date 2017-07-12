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
import java.util.List;
import javax.swing.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
				// �򿪺�URL֮�������
				URLConnection connection = realUrl.openConnection();
				// ����ͨ�õ���������
				connection.setRequestProperty("accept", "*/*");
				connection.setRequestProperty("connection", "Keep-Alive");
				connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				// ����ʵ�ʵ�����
				connection.connect();
				// ��ȡ������Ӧͷ�ֶ�
				Map<String, List<String>> map = connection.getHeaderFields();
				// �������е���Ӧͷ�ֶ�
				for (String key : map.keySet())
				{
					System.out.println(key + "--->" + map.get(key));
				}
				// ���� BufferedReader����������ȡURL����Ӧ
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null)
				{
					result += line;
				}
			} catch (Exception e)
			{
				System.out.println("����GET��������쳣��" + e);
				e.printStackTrace();
			}
			// ʹ��finally�����ر�������
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
				// �򿪺�URL֮�������
				URLConnection conn = realUrl.openConnection();
				// ����ͨ�õ���������
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				// ����POST�������������������
				conn.setDoOutput(true);
				conn.setDoInput(true);
				// ��ȡURLConnection�����Ӧ�������
				out = new PrintWriter(conn.getOutputStream());
				// �����������
				out.print(param);
				// flush������Ļ���
				out.flush();
				// ����BufferedReader����������ȡURL����Ӧ
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null)
				{
					result += line;
				}
			} catch (Exception e)
			{
				System.out.println("���� POST ��������쳣��" + e);
				e.printStackTrace();
			}
			// ʹ��finally�����ر��������������
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
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ���ùرն���
			this.setVisible(true);
			this.setSize(800, 600);
			// this.setLayout(new FlowLayout());
			this.setLayout(new GridLayout(2, 2, 5, 5));
			// GridLayout grid =new GridLayout(3,3,5,5);

			this.InitPanel01();
			this.InitPanel02();
			this.InitPanel03();
			this.InitPanel04();

		}

		JPanel panel01 = null;// new JPanel();
		JPanel panel02 = null;// new JPanel();
		JPanel panel03 = null;// new JPanel();
		JPanel panel04 = null;// new JPanel();

		void InitPanel01()
		{
			this.panel01 = new JPanel();
			this.add(panel01);
			panel01.setBorder(BorderFactory.createTitledBorder("��ѯ�˻�"));
			GridBagLayout layout = new GridBagLayout();
			panel01.setLayout(layout);

			// 3 ��һ�У��ڰ�
			{// ��һ�У���ѯ�߶�
				Label label = new Label();
				panel01.add(label);
				label.setText("��ǰ�߶�");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 1;
					layout.setConstraints(label, s);
				}

				TextField tf = new TextField();
				panel01.add(tf);
				tf.setText("��ǰ�߶�");
				{
					GridBagConstraints s = new GridBagConstraints();
					s.fill = GridBagConstraints.BOTH;
					s.gridwidth = 3;
					s.weightx=1;
					layout.setConstraints(tf, s);
				}
				
				Button btn = new Button("Button2");
				panel01.add(btn);
				btn.setLabel("��ѯ");
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
			{// �ڶ��У����ַ
				Label label = new Label();
				panel01.add(label);
				label.setText("���ַ");
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
				btn.setLabel("��ѯ");
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
							outstr+="��Ǯ��\n";
						}
						else
						{
						
							outstr+="address:"+json.get("_id").getAsString()+"\n";
							outstr+="�Ʋ�\n";
							JsonObject balances= json.get("balances").getAsJsonObject();
							if(balances.has("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b"))
							{
								outstr+="С�Ϲ�:"+balances.get("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").getAsInt()+"\n";
							}
							if(balances.has("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7"))
							{
								outstr+="С�ϱ�:"+balances.get("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7").getAsFloat()+"\n";
							}
						}
						area.setText(outstr);
						
						
					}
				});
			}
			{// �����У����ַ2
				Label label = new Label();
				panel01.add(label);
				label.setText("���ַ");
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
				btn.setLabel("��ѯ");
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
							outstr+="��Ǯ��\n";
						}
						else
						{
						
							outstr+="address:"+json.get("_id").getAsString()+"\n";
							outstr+="�Ʋ�\n";
							JsonObject balances= json.get("balances").getAsJsonObject();
							if(balances.has("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b"))
							{
								outstr+="С�Ϲ�:"+balances.get("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").getAsInt()+"\n";
							}
							if(balances.has("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7"))
							{
								outstr+="С�ϱ�:"+balances.get("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7").getAsFloat()+"\n";
							}
						}
						area.setText(outstr);
						
						
					}
				});
			}
			{// �����У����ַ3
				Label label = new Label();
				panel01.add(label);
				label.setText("���ַ");
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
				btn.setLabel("��ѯ");
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
							outstr+="��Ǯ��\n";
						}
						else
						{
						
							outstr+="address:"+json.get("_id").getAsString()+"\n";
							outstr+="�Ʋ�\n";
							JsonObject balances= json.get("balances").getAsJsonObject();
							if(balances.has("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b"))
							{
								outstr+="С�Ϲ�:"+balances.get("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").getAsInt()+"\n";
							}
							if(balances.has("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7"))
							{
								outstr+="С�ϱ�:"+balances.get("602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7").getAsFloat()+"\n";
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
			this.panel02 = new JPanel();
			this.add(panel02);
			panel02.setBorder(BorderFactory.createTitledBorder("panel02"));
			panel02.setLayout(new GridLayout(10, 3, 5, 5));
		}

		void InitPanel03()
		{
			this.panel03 = new JPanel();
			this.add(panel03);
			panel03.setBorder(BorderFactory.createTitledBorder("panel03"));
			panel03.setLayout(new GridLayout(10, 3, 5, 5));
		}

		void InitPanel04()
		{
			this.panel04 = new JPanel();
			this.add(panel04);
			panel04.setBorder(BorderFactory.createTitledBorder("panel04"));
			panel04.setLayout(new GridLayout(10, 3, 5, 5));
		}
	}

}
