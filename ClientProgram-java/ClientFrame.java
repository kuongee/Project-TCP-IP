/* Author: Jeesoo Min
Email: kuongee@gmail.com
Completion date
Version 1: 2014-01-11 ~ 2014-01-14
Updated: 2017-12-21*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class ClientFrame extends JFrame{
	static Toolkit toolkit = Toolkit.getDefaultToolkit();
		
	MainFrame mf;
	OGamePanel ogp;
	
	String nickname = "";
	String serverIp = "";
	int serverPort = 0;
	
	DataOutputStream out;
	DataInputStream in;
	
	JPanel chatP;
	JScrollPane sp = null;
	JPanel panelSP = null;
	JTextArea ta;
	JTextField tf;
	JButton startB;
	
	static String text;
	int stoneColor = -1;	
	
	ClientFrame(){
		super("Chat");
		mf = new MainFrame(this);
		mf.setVisible(false);
		
		ogp = mf.gp;
		
		nickname = JOptionPane.showInputDialog(this,"Input Your Nickname","Nickname",JOptionPane.QUESTION_MESSAGE);
		makingFrame();		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setResizable(false);		
		setVisible(true);
	}

	public void makingFrame(){
		Container contain = this.getContentPane();
		setLayout(null);
		//setSize(toolkit.getScreenSize().width, toolkit.getScreenSize().height/2);
		setSize(750, 550);
		contain.setBackground(Color.gray);
		
		tf = new JTextField(40);
		tf.setBounds(17,330,700,33);
	
		ta = new JTextArea(15,61);
		//ta.setBounds(17,17,800,300);
		ta.setBackground(Color.lightGray);
		ta.setEditable(false);
		
		EventHandler handler = new EventHandler();
		tf.addActionListener(handler);
		tf.addFocusListener(handler);
		ta.addFocusListener(handler);
		
		startB = new JButton("Start");
		startB.setBounds(280,400,150,80);
		Font f = new Font("Serif",Font.BOLD,40);
		startB.setFont(f);
		startB.setForeground(Color.white);
		startB.setBackground(new Color(65,65,65));
		startB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(out != null){
					try{
						if(stoneColor > -1) {
							ta.append("\r\n<Warning> You are already in the game!");
						}
						else
							out.writeUTF("[start]" + nickname);
					} catch(IOException e1) {}
				}
			}
		});		

		panelSP = new JPanel();
		panelSP.setBackground(Color.gray);
		sp = new JScrollPane(ta);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panelSP.add(sp);
		panelSP.setBounds(17,17,700,300);
		
		add(tf);
		add(panelSP);
		//chatP.add(ta);
		//chatP.
		add(startB);
		
		tf.requestFocus();
	}
	
	public void startClient(){
		Socket socket;
		String serverIp = "127.0.0.1";
		
		try {
			socket = new Socket(serverIp,7777);
			ta.setText("Welcome! "+nickname+"\n상대방과 연결되었습니다.");
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			try{
				out.writeUTF(nickname);
			} catch(IOException e1) {}
			while(in != null){
				String msg = in.readUTF();
				if(msg.startsWith(nickname+">")){	
					int n = nickname.length()+1;
					ta.append("\r\n" + msg.substring(n));
				}
				/////게임시작 버튼 누르면
				else if(msg.startsWith("start")){
					
					if(msg.substring(5).equals("B"+nickname)){
						stoneColor = 1;
						ta.append("\r\n" + "You are Black!");
						while(in != null) {
							String wait = in.readUTF();
							if(wait.startsWith("startW")){
								break;
							}
						}
						mf.setTitle("Othello-Black");
						mf.setVisible(true);
					}
					else if(msg.substring(5).equals("W"+nickname)){
						stoneColor = 0;
						ta.append("\r\n" + "You are White!");
						mf.setTitle("Othello-White");
						mf.setVisible(true);
					}
				}
				else if(msg.startsWith("full")){
					if(msg.substring(4).equals(nickname))
						ta.append("\r\n" + "게임 진행 중");
				}
				
				else if(msg.startsWith("[location]")){
					String s = msg.substring(10);
					int c1 = s.indexOf(',');
					int i = Integer.parseInt((s.substring(0, c1)));
					int j = Integer.parseInt(s.substring(c1+1));
					ogp.clientPressed(i,j);
				}
				
				else
					ta.append("\r\n" + msg);
			}
			
		} catch (UnknownHostException e) {
			ta.setText("상대방과 연결할 수 없습니다.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){
		ClientFrame cf = new ClientFrame();
		cf.startClient();
	}
	
	class EventHandler extends FocusAdapter implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			text = tf.getText();
			
			if("".equals(text)) 
				return;
			
			if(out != null){
				try{
					out.writeUTF("[message]" + nickname + ">  " + text);
				} catch(IOException e1) {}
			}
			
			tf.setText("");
		}
	
		public void focusGained(FocusEvent e){
			tf.requestFocus();
		}
	}
	
	
	
}
