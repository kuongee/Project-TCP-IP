
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;


public class OGamePanel extends JPanel implements MouseListener, ActionListener {
	ClientFrame cf;
	
	static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	static int wh = toolkit.getScreenSize().height/2;
	
	public OUnit [][] u = new OUnit[8][8];
	public static ArrayList<OUnit> unitArray = new ArrayList <OUnit> ();
	OUnit uA;
	
	public static int number = 1; // Ȧ�� ���� ��, ¦�� �� ��
	
	public static String okstr = "";				
	public static int blackNum = 0, whiteNum = 0;	// Score panel���� ���� �� ����
	
	boolean b_click = false, b_full = false, b_place = false;
	static int i_click, j_click;

	boolean bb_click = false, b_hint = false;
	
	int a1, a2, a3, a4, a5, a6, a7, a8, check=0;
	
	OScorePanel sp;
	
	public OGamePanel(OScorePanel s, ClientFrame c){
		this.sp = s;
		this.cf = c;
		
		setBounds(10, 10, wh, wh);
		setBackground(Color.gray);
		
		addMouseListener(this);
		
////////�� ĭ�� ��ü ����
		for(int i=0; i<u.length; i++)
			for(int j=0; j<u[i].length; j++)
				u[i][j] = new OUnit(j*wh/8, i*wh/8, i, j);
		
		u[3][3].setStone(1);
		u[3][4].setStone(-1);
		u[4][3].setStone(-1);
		u[4][4].setStone(1);
		unitArray.add(u[3][3]);
		unitArray.add(u[4][4]);
		unitArray.add(u[3][4]);
		unitArray.add(u[4][3]);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;
		
		lineDrawing(g2D);		// ������ �׸���
	
		if(bb_click){
			if(number % 2 == 1){		//Ȧ���� ���� ��
				drawStone(-1,i_click,j_click,g2D);
				number++;
				bb_click = false;
			}
			else{						//¦���� ��
				drawStone(1,i_click,j_click,g2D);
				number++;
				bb_click = false;
			}	
		}
		
			if(!bb_click){
			rePaint(g2D);	// ���� ���� ������ �� �ٽ� �׷����� ��
			//noPlace();
			bb_click = false;
		}
		
	}
		
	
////������ ���� �׸���
	public void lineDrawing(Graphics2D g){
		int height = this.wh;
		height = height / 8;
		
		// ���� �׸���
		for(int i=0; i<9; i++){
			g.setStroke(new BasicStroke(5));
			g.setColor(new Color(57,57,57));
			g.drawLine(0, i*height, wh, i*height);
			g.drawLine(i*height, 0, i*height, wh);
		}
		
	}
	
	public void rePaint(Graphics2D g){
		for(int k=0; k<unitArray.size(); k++){
			uA = unitArray.get(k);
			int i = uA.getIindex();
			int j = uA.getJindex();
			drawStone(uA.getStone(),i,j,g);
		}
	}
	
	public void noPlace(){
		int ok=0;
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				isAvailable1(i,j);	isAvailable2(i,j);	isAvailable3(i,j);	isAvailable4(i,j);
				isAvailable5(i,j);	isAvailable6(i,j);	isAvailable7(i,j);	isAvailable8(i,j);
				if(u[i][j].getIsOk() && !u[i][j].isFull())
					ok++;
			}
		}
		
		if(ok==0){
			
			okstr = "No Place!";
			
			for(int i=0; i<8; i++)
				for(int j=0; j<8; j++){
					if(u[i][j].getStone() == -1 && u[i][j].isFull() && whiteNum == 0){
						okstr = "All Black";
						sp.op.repaint();
						return;
					}
					else if(u[i][j].getStone() == 1 && u[i][j].isFull() && blackNum == 0){
						okstr = "All White";
						sp.op.repaint();
						return;
					}
				}
			sp.op.repaint();
			number = number + 1;
		}
		else{
			for(int i=0; i<8; i++){
				for(int j=0; j<8; j++){
					u[i][j].setNotOk();
				}
			}
		}
		
		return;
	}
	
	public void hint(Graphics2D g){
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				isAvailable1(i,j);	isAvailable2(i,j);	isAvailable3(i,j);	isAvailable4(i,j);
				isAvailable5(i,j);	isAvailable6(i,j);	isAvailable7(i,j);	isAvailable8(i,j);
				if(u[i][j].getIsOk() && !u[i][j].isFull())
					g.fillRect(u[i][j].getxUnit()+10, u[i][j].getyUnit()+10,10, 10);
				
			}
		}
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				u[i][j].setNotOk();
			}
		}
		
	//	b_hint = false;
		
	}
	
////���� �׸�	
	public void drawStone(int s, int i, int j, Graphics2D g){
		u[i][j].setStone(s);		
		g.setColor(u[i][j].unitStone.getColorofStone());
		g.fillOval(u[i][j].unitStone.getXofStone(), u[i][j].unitStone.getYofStone(), u[i][j].unitStone.getSizeofStone(), u[i][j].unitStone.getSizeofStone());
		u[i][j].setIsFull();
	}
	
////���� ĭ�� Ŭ���ϱ� ���� �� ������ ���� �ڸ��� Ȯ��	
	public void isAvailable1(int i, int j){
			
		int ustone, clickstone;
		int ok = 0;
		int go = 0;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		if(j == 0){
			return;
		}
		
		for(int k=j-1; k>=0; k--){
			
			ustone = u[i][k].getStone();
			if(!u[i][k].isFull()){
				go = 0;
				break;
			}
						
			if(ustone == clickstone){
				if(k == j-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
					go = 0;
					break;	
				}
				else{ 
					ok = go + 1;
					u[i][j].setIsOk();
					break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
				}
			}		
				
			else{
				if(k == 0)
					go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
				else
					go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}		
			
		}	

		
		return;	// 1�� ����
		
	}
	public void isAvailable2(int i, int j){
		int ustone = 0, clickstone;
		int ok;
		int go = 0;
		boolean c = false, d = false;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		int m, n;
		
		if(i==0 || j==0){
			return;
		}
	
		if( (i-1) < (j-1) ){
				
			for(m=i-1, n=j-1; m>=0; m--, n--){
				
				ustone = u[m][n].getStone();
				if(!u[m][n].isFull()){
					go = 0;
					break;
				}
					
				if(ustone == clickstone){
						if(m == i-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(m == 0)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
			
			else if( (i-1) > (j-1) ){
				
				for(m=i-1, n=j-1; n>=0; m--,n--){
					
					ustone = u[m][n].getStone();
					if(!u[m][n].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(n == j-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(n == 0)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
			
			else{
				
				for(m=i-1; m>=0; m--){
					
					ustone = u[m][m].getStone();
					if(!u[m][m].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(m == i-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(m == 0)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
		
		
		
		return;	// 2�� ����	
	}
	public void isAvailable3(int i, int j){
	
		int ustone, clickstone;
		int ok = 0;
		int go = 0;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		if(i == 0){
			return;
		}
			
		for(int k=i-1; k>=0; k--){
				
			ustone = u[k][j].getStone();
			if(!u[k][j].isFull()){
				go = 0;
				break;
			}
				
			if(ustone == clickstone){
				if(k == i-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
					go = 0;
					break;	
				}
				else{ 
					ok = go + 1;
					u[i][j].setIsOk();
					break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
				}
			}		
				
			else{
				if(k == 0)
					go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
				else
					go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
			}		
		}
			
		
		
		return;	// 3�� ����
		
	}
	public void isAvailable4(int i, int j){
		
		int ustone = 0, clickstone;
		int ok;
		int go = 0;
		boolean c = false, d = false;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		int m, n;
		
		if(i==0 || j==7){
			return;
		}
		
			for(m = i-1, n = j+1; m>=0 && n<8; m--,n++){
				
				ustone = u[m][n].getStone();
				if(!u[m][n].isFull()){
					go = 0;
					break;
				}
				
				if(ustone == clickstone){
					if(m == i-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						u[i][j].setIsOk();
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}
				
				else{
					if(m == 0 || n == 7)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}	
			}
			
		
		
		return;	// 4�� ����	
		
	}
	public void isAvailable5(int i, int j){
		
		int ustone, clickstone;
		int ok = 0;
		int go = 0;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		if(j == 7){
			return;
		}
		
		
			for(int k=j+1; k<8; k++){
				
				ustone = u[i][k].getStone();
				if(!u[i][k].isFull()){
					go = 0;
					break;
				}
					
				if(ustone == clickstone){
					if(k == j+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						u[i][j].setIsOk();
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}		
				
				else{
					if(k == 7)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}		
			
		}	

		
		return;	// 5�� ����
		
	}
	public void isAvailable6(int i, int j){
		
		int ustone = 0, clickstone;
		int ok;
		int go = 0;
		boolean c = false, d = false;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		int m, n;
		
		if(i==7 || j==7){
			return;
		}
		
		
			if( (i+1) < (j+1) ){
				for(m=i+1, n=j+1; n<8; m++, n++){
					
					ustone = u[m][n].getStone();
					if(!u[m][n].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(m == i+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(n == 7)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
			
			else if( (i+1) > (j+1) ){
				
				for(m=i+1, n=j+1; m<8; m++,n++){
					ustone = u[m][n].getStone();
					if(!u[m][n].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(n == j+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(m == 7)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
			
			else{
				
				for(m=i+1; m<8; m++){
					ustone = u[m][m].getStone();
					if(!u[m][m].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(m == i+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(m == 7)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
		
		
	
		
		return ;	// 6�� ����	
		
		
	}
	public void isAvailable7(int i, int j){
		
		int ustone, clickstone;
		int ok = 0;
		int go = 0;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		if(i == 7){
			return;
		}
		
		
			for(int k=i+1; k<8; k++){
			
				ustone = u[k][j].getStone();
				if(!u[k][j].isFull()){
					go = 0;
					break;
				}
				
				if(ustone == clickstone){
					if(k == i+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						u[i][j].setIsOk();
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}		
				
				else{
					if(k == 7)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}		
			}
			

		
		return;	// 5�� ����
		
		
	}
	public void isAvailable8(int i, int j){
		
		int ustone = 0, clickstone;
		int ok;
		int go = 0;
		boolean c = false, d = false;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		int m, n;
		
		if(i==7 || j==0){
			return ;
		}
	
			
			for(m = i+1, n = j-1; m<8 && n>=0; m++,n--){
				
				ustone = u[m][n].getStone();
				if(!u[m][n].isFull()){
					go = 0;
					break;
				}
				
				if(ustone == clickstone){
					if(m == i+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						u[i][j].setIsOk();
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}
				
				else{
					if(m == 7 || n == 0)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}	
			}
		
		
		return;	// 8�� ����		
	}
	
////���� ĭ�� Ŭ������ �� �� ������ �������� Ȯ��	
	public int isAvailable1(){
		int i = i_click;
		int j = j_click;
		
		int ustone, clickstone;
		int ok = 0;
		int go = 0;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		if(j == 0){
			return 0;
		}
		
		for(int k=j-1; k>=0; k--){
			
			ustone = u[i][k].getStone();
			if(!u[i][k].isFull()){
				go = 0;
				break;
			}
						
			if(ustone == clickstone){
				if(k == j-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
					go = 0;
					break;	
				}
				else{ 
					ok = go + 1;
					break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
				}
			}		
				
			else{
				if(k == 0)
					go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
				else
					go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}		
			
		}	

		
		return go;	// 1�� ����
	}		
	public int isAvailable2(){

		int i = i_click;
		int j = j_click;
		int ustone = 0, clickstone;
		int ok;
		int go = 0;
		boolean c = false, d = false;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		int m, n;
		
		if(i==0 || j==0){
			return 0;
		}
		
		
			
			if( (i-1) < (j-1) ){
				
				
				for(m=i-1, n=j-1; m>=0; m--, n--){
					
					ustone = u[m][n].getStone();
					if(!u[m][n].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(m == i-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(m == 0)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
			
			else if( (i-1) > (j-1) ){
				
				for(m=i-1, n=j-1; n>=0; m--,n--){
					
					ustone = u[m][n].getStone();
					if(!u[m][n].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(n == j-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(n == 0)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
			
			else{
				
				for(m=i-1; m>=0; m--){
					
					ustone = u[m][m].getStone();
					if(!u[m][m].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(m == i-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(m == 0)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
		
		
		
		return go;	// 2�� ����	
	}
	public int isAvailable3(){
		int i = i_click;
		int j = j_click;
		
		int ustone, clickstone;
		int ok = 0;
		int go = 0;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		if(i == 0){
			return 0;
		}
		
	
			for(int k=i-1; k>=0; k--){
			
				
				
				ustone = u[k][j].getStone();
				if(!u[k][j].isFull()){
					go = 0;
					break;
				}
				
				//System.out.println("color"+ustone);
				
				if(ustone == clickstone){
					if(k == i-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}		
				
				else{
					if(k == 0)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}		
			}
			
		
		
		return go;	// 3�� ����
		
	}
	public int isAvailable4(){
		
		int i = i_click;
		int j = j_click;
		int ustone = 0, clickstone;
		int ok;
		int go = 0;
		boolean c = false, d = false;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		int m, n;
		
		if(i==0 || j==7){
			return 0;
		}
		
			for(m = i-1, n = j+1; m>=0 && n<8; m--,n++){
				
				ustone = u[m][n].getStone();
				if(!u[m][n].isFull()){
					go = 0;
					break;
				}
				
				if(ustone == clickstone){
					if(m == i-1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}
				
				else{
					if(m == 0 || n == 7)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}	
			}
			
		
		
		return go;	// 4�� ����	
	
	}
	public int isAvailable5(){
		int i = i_click;
		int j = j_click;
		
		int ustone, clickstone;
		int ok = 0;
		int go = 0;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		if(j == 7){
			return 0;
		}
		
		
			for(int k=j+1; k<8; k++){
				
				ustone = u[i][k].getStone();
				if(!u[i][k].isFull()){
					go = 0;
					break;
				}
					
				if(ustone == clickstone){
					if(k == j+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}		
				
				else{
					if(k == 7)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}		
			
		}	

		
		return go;	// 5�� ����
		
	}
	public int isAvailable6(){
		int i = i_click;
		int j = j_click;
		int ustone = 0, clickstone;
		int ok;
		int go = 0;
		boolean c = false, d = false;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		int m, n;
		
		if(i==7 || j==7){
			return 0;
		}
		
		
			if( (i+1) < (j+1) ){
				for(m=i+1, n=j+1; n<8; m++, n++){
					
					ustone = u[m][n].getStone();
					if(!u[m][n].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(m == i+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(n == 7)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
			
			else if( (i+1) > (j+1) ){
				
				for(m=i+1, n=j+1; m<8; m++,n++){
					ustone = u[m][n].getStone();
					if(!u[m][n].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(n == j+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(m == 7)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
			
			else{
				
				for(m=i+1; m<8; m++){
					ustone = u[m][m].getStone();
					if(!u[m][m].isFull()){
						go = 0;
						break;
					}
					
					if(ustone == clickstone){
						if(m == i+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
						}
					}
					else{
						if(m == 7)
							go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
						else
							go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
					}
				}
			}
		
		
	
		
		return go;	// 6�� ����	
		
	}
	public int isAvailable7(){
		int i = i_click;
		int j = j_click;
		
		int ustone, clickstone;
		int ok = 0;
		int go = 0;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		if(i == 7){
			return 0;
		}
		
		
			for(int k=i+1; k<8; k++){
			
				ustone = u[k][j].getStone();
				if(!u[k][j].isFull()){
					go = 0;
					break;
				}
				
				if(ustone == clickstone){
					if(k == i+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}		
				
				else{
					if(k == 7)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}		
			}
			

		
		return go;	// 5�� ����
		
	}
	public int isAvailable8(){
		
		int i = i_click;
		int j = j_click;
		int ustone = 0, clickstone;
		int ok;
		int go = 0;
		boolean c = false, d = false;
		
		if(number % 2==1)
			clickstone = -1;
		else
			clickstone = 1;
		
		int m, n;
		
		if(i==7 || j==0){
			return 0;
		}
	
			
			for(m = i+1, n = j-1; m<8 && n>=0; m++,n--){
				
				ustone = u[m][n].getStone();
				if(!u[m][n].isFull()){
					go = 0;
					break;
				}
				
				if(ustone == clickstone){
					if(m == i+1){	// �ٷ� ���� �ڱ� �ڽŰ� ������ �ٷ� break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// �ٸ� �� ���� �ڱ� �ڽŰ� ���� ���� �ִٸ� ok�� ������ �� break;
					}
				}
				
				else{
					if(m == 7 || n == 0)
						go = 0;			// �������� ������ �ڱ�� ���� ���� ������ �ʴ´ٸ� go ������ 0
					else
						go = go + 1;	// �ٷ� ���� �ڱ� �ڽŰ� �ٸ��� continue �׸��� go ������ 1
				}	
			}
		
		
		return go;	// 8�� ����	
	}
		
////���� ĭ�� Ŭ���� �� ���� �ٲ�� ��
	public void colorChange1(int c){
		int i = i_click;
		int j = j_click;
		
		int n;
		
		if(number % 2 == 1)
			n = -1;
		else
			n = 1;
	
		for(int m=1; m <= c; m++){
			for(int k=0; k < unitArray.size(); k++){
				if(unitArray.get(k).equals(u[i][j-m])){
					unitArray.get(k).setStone(n);
					//System.out.println("true??"+unitArray.get(k).getStone());
					b_full = true;
				}
			}
		}
		
		//repaint();
	}	
	public void colorChange2(int c){
		
		int i = i_click;
		int j = j_click;
		
		int n;
		
		if(number % 2 == 1)
			n = -1;
		else
			n = 1;
	
		for(int m=1; m <= c; m++){
			for(int k=0; k < unitArray.size(); k++){
				if(unitArray.get(k).equals(u[i-m][j-m])){
					unitArray.get(k).setStone(n);
					//System.out.println("true??"+unitArray.get(k).getStone());
					b_full = true;
				}
			}
		}
	//	repaint();
		
	}
	public void colorChange3(int c){
		int i = i_click;
		int j = j_click;
		
		int n;
		
		if(number % 2 == 1)
			n = -1;
		else
			n = 1;
		
		for(int m=1; m <= c; m++){
			for(int k=0; k<unitArray.size(); k++){
				if(unitArray.get(k).equals(u[i-m][j])){
					unitArray.get(k).setStone(n);
					b_full = true;
				}
			}
		}
		
	//	repaint();
		
	}
	public void colorChange4(int c){
		int i = i_click;
		int j = j_click;
		
		int n;
		
		if(number % 2 == 1)
			n = -1;
		else
			n = 1;
		
		for(int m=1; m <= c; m++){
			for(int k=0; k<unitArray.size(); k++){
				if(unitArray.get(k).equals(u[i-m][j+m])){
					unitArray.get(k).setStone(n);
					b_full = true;
				}
			}
		}
		
		repaint();
		
	}
	public void colorChange5(int c){
		int i = i_click;
		int j = j_click;
		int n;
		
		if(number % 2 == 1)
			n = -1;
		else
			n = 1;
		
		for(int m=1; m <= c; m++){
			for(int k=0; k<unitArray.size(); k++){
				if(unitArray.get(k).equals(u[i][j+m])){
					unitArray.get(k).setStone(n);
					b_full = true;
				}
			}
		}
		
		repaint();
		
	}
	public void colorChange6(int c){
		int i = i_click;
		int j = j_click;
		
		int n;
		
		if(number % 2 == 1)
			n = -1;
		else
			n = 1;
		
		for(int m=1; m <= c; m++){
			for(int k=0; k<unitArray.size(); k++){
				if(unitArray.get(k).equals(u[i+m][j+m])){
					unitArray.get(k).setStone(n);
					b_full = true;
				}
			}
		}
		
		repaint();
		
	}
	public void colorChange7(int c){
		int i = i_click;
		int j = j_click;
		int n;
		
		if(number % 2 == 1)
			n = -1;
		else
			n = 1;
		
		for(int m=1; m <= c; m++){
			for(int k=0; k<unitArray.size(); k++){
				if(unitArray.get(k).equals(u[i+m][j])){
					unitArray.get(k).setStone(n);
					b_full = true;
				}
			}
		}
		
		repaint();
	}
	public void colorChange8(int c){
		
		int i = i_click;
		int j = j_click;
		
		int n;
		
		if(number % 2 == 1)
			n = -1;
		else
			n = 1;
		
		for(int m=1; m <= c; m++){
			for(int k=0; k<unitArray.size(); k++){
				if(unitArray.get(k).equals(u[i+m][j-m])){
					unitArray.get(k).setStone(n);
					b_full = true;
				}
			}
		}
		
		repaint();
		
	}


	
	
////������ ĭ�� Ŭ�� ���� ��
	public void mousePressed(MouseEvent e) {
		if(number % 2 == cf.stoneColor){
			try{
				cf.out.writeUTF("[location]"+e.getX()+","+e.getY());
			} catch(IOException e1) {}
		}
	}
	
	
	public void clientPressed(int x, int y){
		int i=0, j=0;
		
/////////click �� ĭ�� index
		for(i=0; i<u.length; i++)
			for(j=0; j<u[i].length; j++){
				b_click = u[i][j].isClicked(x, y);
				
				if(b_click){
					i_click = i; j_click = j;	
					break;
				}
			}
				
		int n;
		
		if(number%2 == 1)
			n = -1;
		else
			n = 1;
		
		
		if(u[i_click][j_click].isFull()){
			bb_click = false;
			return;
		}
		
		else{		
				
			///////�� ������ ���� ���� �� �ִ��� Ȯ���ϴ� �۾�
			a1 = isAvailable1();
			a2 = isAvailable2();
			a3 = isAvailable3();
			a4 = isAvailable4();
			a5 = isAvailable5();
			a6 = isAvailable6();
			a7 = isAvailable7();
			a8 = isAvailable8();
			
			if(a1!=0){
				bb_click = true;
				u[i_click][j_click].setStone(n);
				u[i_click][j_click].setNotOk();
				okstr = "";
				unitArray.add(u[i_click][j_click]);
			}
			else if(a2!=0){
				bb_click = true;
				u[i_click][j_click].setStone(n);
				u[i_click][j_click].setNotOk();
				okstr = "";
				unitArray.add(u[i_click][j_click]);
			}
			else if(a3!=0){
				bb_click = true;
				u[i_click][j_click].setStone(n);
				u[i_click][j_click].setNotOk();
				okstr = "";
				unitArray.add(u[i_click][j_click]);
			}
			else if(a4!=0){
				bb_click = true;
				u[i_click][j_click].setStone(n);
				u[i_click][j_click].setNotOk();
				okstr = "";
				unitArray.add(u[i_click][j_click]);
			}
			else if(a5!=0){
				bb_click = true;
				u[i_click][j_click].setStone(n);
				u[i_click][j_click].setNotOk();
				okstr = "";
				unitArray.add(u[i_click][j_click]);
			}
			else if(a6!=0){
				bb_click = true;
				u[i_click][j_click].setStone(n);
				u[i_click][j_click].setNotOk();
				okstr = "";
				unitArray.add(u[i_click][j_click]);
			}
			else if(a7!=0){
				bb_click = true;
				u[i_click][j_click].setStone(n);
				u[i_click][j_click].setNotOk();
				okstr = "";
				unitArray.add(u[i_click][j_click]);
			}
			else if(a8!=0){
				bb_click = true;
				u[i_click][j_click].setStone(n);
				u[i_click][j_click].setNotOk();
				okstr = "";
				unitArray.add(u[i_click][j_click]);
			}
			
		}
		
		colorChange1(a1); colorChange2(a2);	colorChange3(a3); colorChange4(a4);
		colorChange5(a5); colorChange6(a6); colorChange7(a7); colorChange8(a8);
		
		if(a1+a2+a3+a4+a5+a6+a7+a8 == 0){
			okstr = "��";
			sp.op.repaint();
			return;
		}
		
		blackNum = 0; whiteNum = 0;
		for(int k=0; k<unitArray.size(); k++){
			uA = unitArray.get(k);
			
			if(uA.getStone() == -1)
				blackNum++;
			else if(uA.getStone() == 1)
				whiteNum++;
			else
				break;
		}
		
		
		repaint();

		
		sp.bs.repaint();
		sp.ws.repaint();
		sp.op.repaint();
	}
	
////��Ʈ ��ư�� ������ ��
	public void actionPerformed(ActionEvent e) {
		/*
		String b = ((JButton)e.getSource()).getText();
		
		switch(b){
		case "HINT":
			b_hint = true;
			repaint();
			break;
		}*/
		
	}
	
	
	public void mouseReleased(MouseEvent arg0) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}	
	public void mouseExited(MouseEvent arg0) {}
}
