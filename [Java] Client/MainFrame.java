/* Author: Jeesoo Min
Email: kuongee@gmail.com
Completion date
Version 1: 2014-01-11 ~ 2014-01-14
Updated: 2017-12-21*/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class MainFrame extends JFrame {
	
	static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	ClientFrame cf;
	
	OGamePanel gp; 
	OScorePanel sp;

	
	public MainFrame(ClientFrame c){
		super("Othello");
		this.cf = c;
		sp = new OScorePanel();
		gp = new OGamePanel(sp, cf);
		makingFrame();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setResizable(false);		
		//setVisible(true);
	}
	
	public void makingFrame(){
		Container contain = this.getContentPane();
		setLayout(null);
		setSize(gp.wh+390, gp.wh+70);
		contain.setBackground(new Color(219,219,219));
		add(gp);
		add(sp);
	}
	
	/*public static void main(String args[]){
		new MainFrame();
	}
	*/
	
	
}
