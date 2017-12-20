/* Author: Jeesoo Min 
Email: kuongee@gmail.com
Completion date
Version 1: 2014-01-11 ~ 2014-01-14
Updated: 2017-12-21*/
import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;


public class OScorePanel extends JPanel {
	
	BlackScore bs;
	WhiteScore ws;
	OkPanel op;
	
	
	public OScorePanel(){
		
		bs = new BlackScore();
		ws = new WhiteScore();
		op = new OkPanel();
	
		
		setLayout(null);
		setBounds(OGamePanel.wh+20,10,350,OGamePanel.wh+1);
		setBackground(Color.gray);	
		
		add(op);
		add(bs);
		add(ws);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;
		
		g.setColor(new Color(57,57,57));
		g2D.setStroke(new BasicStroke(6));
		g2D.drawLine(0,0,350,0);
		g2D.drawLine(0,0,0,OGamePanel.wh+1);
		g2D.drawLine(350,0,350,OGamePanel.wh+1);
		g2D.drawLine(0,OGamePanel.wh+1,350,OGamePanel.wh+1);		//score panelÀÇ Å×µÎ¸®
	
	}
	
	
	
	class OkPanel extends JPanel{
		
		public OkPanel(){
			setLayout(null);
			setBounds(15,200,320,240);
			setBackground(Color.gray);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			Graphics2D g2D = (Graphics2D)g;
			
			Font f2 = new Font("Serif", Font.BOLD, 50);
			if(OGamePanel.blackNum+OGamePanel.whiteNum == 64){
				g2D.setFont(f2);
				g2D.drawString("Game Over",35,130);
			}
			
			else if(OGamePanel.okstr == "No Place!"){
				g2D.setFont(f2);
				g2D.drawString(OGamePanel.okstr,60,130);
			}
			else if(OGamePanel.okstr == "All Black"){
				g2D.setFont(f2);
				g2D.drawString(OGamePanel.okstr, 60, 130);
			}
			else if(OGamePanel.okstr == "All White"){
				g2D.setFont(f2);
				g2D.drawString(OGamePanel.okstr, 60, 130);
			}
			else{
				Font f1 = new Font("Serif", Font.BOLD, 180);
				g2D.setFont(f1);
				g2D.drawString(OGamePanel.okstr,100,170);
			}
		}
		
	}
	
	class BlackScore extends JPanel{
		
		LineBorder b;
		
		public BlackScore(){
			setLayout(null);
			setBounds(15,15,320,90);
			setBackground(new Color(45,45,45));
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			Graphics2D g2D = (Graphics2D)g;
			
			
			if(OGamePanel.number % 2 == 1){
				b = new LineBorder(new Color(255,230,0),5);
				setBorder(b);
			}
			else{
				b = new LineBorder(new Color(45,45,45),5);
				setBorder(b);
			}
						
			Font f1 = new Font("Serif", Font.BOLD, 70);
			
			g2D.setColor(Color.white);
			g2D.setFont(f1);
			g2D.drawString(String.valueOf(OGamePanel.blackNum),140,70);
			
		}
		
		
	}
	
	class WhiteScore extends JPanel{
		
		LineBorder b;
		
		public WhiteScore(){
			setLayout(null);
			setBounds(15,130,320,90);
			setBackground(Color.white);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			Graphics2D g2D = (Graphics2D)g;
			

			
			if(OGamePanel.number % 2 == 0){
				b = new LineBorder(new Color(255,230,0),5);
				setBorder(b);
			}
			else{
				b = new LineBorder(Color.white,5);
				setBorder(b);
			}
			
			Font f1 = new Font("Serif", Font.BOLD, 70);
			g2D.setFont(f1);
			g2D.drawString(String.valueOf(OGamePanel.whiteNum),140,70);
		
		}
	}
	
	
	
}
