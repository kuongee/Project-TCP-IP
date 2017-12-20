/* Author: Jeesoo Min
Email: kuongee@gmail.com
Completion date
Version 1: 2014-01-11 ~ 2014-01-14
Updated: 2017-12-21*/
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
	
	public static int number = 1; // 홀수 검은 돌, 짝수 흰 돌
	
	public static String okstr = "";				
	public static int blackNum = 0, whiteNum = 0;	// Score panel에서 보여 줄 정보
	
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
		
////////한 칸씩 객체 생성
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
		
		lineDrawing(g2D);		// 게임판 그리기
	
		if(bb_click){
			if(number % 2 == 1){		//홀수는 검은 돌
				drawStone(-1,i_click,j_click,g2D);
				number++;
				bb_click = false;
			}
			else{						//짝수는 흰돌
				drawStone(1,i_click,j_click,g2D);
				number++;
				bb_click = false;
			}	
		}
		
			if(!bb_click){
			rePaint(g2D);	// 같은 곳을 눌렀을 때 다시 그려지게 함
			//noPlace();
			bb_click = false;
		}
		
	}
		
	
////게임판 라인 그리기
	public void lineDrawing(Graphics2D g){
		int height = this.wh;
		height = height / 8;
		
		// 라인 그리기
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
	
////돌을 그림	
	public void drawStone(int s, int i, int j, Graphics2D g){
		u[i][j].setStone(s);		
		g.setColor(u[i][j].unitStone.getColorofStone());
		g.fillOval(u[i][j].unitStone.getXofStone(), u[i][j].unitStone.getYofStone(), u[i][j].unitStone.getSizeofStone(), u[i][j].unitStone.getSizeofStone());
		u[i][j].setIsFull();
	}
	
////게임 칸을 클릭하기 전에 빈 공간에 놓을 자리를 확인	
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
				if(k == j-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
					go = 0;
					break;	
				}
				else{ 
					ok = go + 1;
					u[i][j].setIsOk();
					break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
				}
			}		
				
			else{
				if(k == 0)
					go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
				else
					go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}		
			
		}	

		
		return;	// 1번 방향
		
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
						if(m == i-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(m == 0)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
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
						if(n == j-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(n == 0)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
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
						if(m == i-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(m == 0)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
					}
				}
			}
		
		
		
		return;	// 2번 방향	
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
				if(k == i-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
					go = 0;
					break;	
				}
				else{ 
					ok = go + 1;
					u[i][j].setIsOk();
					break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
				}
			}		
				
			else{
				if(k == 0)
					go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
				else
					go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
			}		
		}
			
		
		
		return;	// 3번 방향
		
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
					if(m == i-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						u[i][j].setIsOk();
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}
				
				else{
					if(m == 0 || n == 7)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}	
			}
			
		
		
		return;	// 4번 방향	
		
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
					if(k == j+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						u[i][j].setIsOk();
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}		
				
				else{
					if(k == 7)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}		
			
		}	

		
		return;	// 5번 방향
		
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
						if(m == i+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(n == 7)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
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
						if(n == j+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(m == 7)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
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
						if(m == i+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							u[i][j].setIsOk();
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(m == 7)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
					}
				}
			}
		
		
	
		
		return ;	// 6번 방향	
		
		
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
					if(k == i+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						u[i][j].setIsOk();
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}		
				
				else{
					if(k == 7)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}		
			}
			

		
		return;	// 5번 방향
		
		
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
					if(m == i+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						u[i][j].setIsOk();
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}
				
				else{
					if(m == 7 || n == 0)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}	
			}
		
		
		return;	// 8번 방향		
	}
	
////게임 칸을 클릭했을 때 그 공간이 가능한지 확인	
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
				if(k == j-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
					go = 0;
					break;	
				}
				else{ 
					ok = go + 1;
					break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
				}
			}		
				
			else{
				if(k == 0)
					go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
				else
					go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}		
			
		}	

		
		return go;	// 1번 방향
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
						if(m == i-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(m == 0)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
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
						if(n == j-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(n == 0)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
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
						if(m == i-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(m == 0)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
					}
				}
			}
		
		
		
		return go;	// 2번 방향	
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
					if(k == i-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}		
				
				else{
					if(k == 0)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}		
			}
			
		
		
		return go;	// 3번 방향
		
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
					if(m == i-1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}
				
				else{
					if(m == 0 || n == 7)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}	
			}
			
		
		
		return go;	// 4번 방향	
	
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
					if(k == j+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}		
				
				else{
					if(k == 7)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}		
			
		}	

		
		return go;	// 5번 방향
		
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
						if(m == i+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(n == 7)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
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
						if(n == j+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(m == 7)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
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
						if(m == i+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
							go = 0;
							break;	
						}
						else{ 
							ok = go + 1;
							break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
						}
					}
					else{
						if(m == 7)
							go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
						else
							go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
					}
				}
			}
		
		
	
		
		return go;	// 6번 방향	
		
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
					if(k == i+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}		
				
				else{
					if(k == 7)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}		
			}
			

		
		return go;	// 5번 방향
		
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
					if(m == i+1){	// 바로 옆에 자기 자신과 같으면 바로 break;
						go = 0;
						break;	
					}
					else{ 
						ok = go + 1;
						break; 			// 다른 색 옆에 자기 자신과 같은 색이 있다면 ok에 저장한 후 break;
					}
				}
				
				else{
					if(m == 7 || n == 0)
						go = 0;			// 게임판의 끝까지 자기와 같은 색이 나오지 않는다면 go 방향은 0
					else
						go = go + 1;	// 바로 옆에 자기 자신과 다르면 continue 그리고 go 방향은 1
				}	
			}
		
		
		return go;	// 8번 방향	
	}
		
////게임 칸을 클릭한 후 색이 바뀌는 곳
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


	
	
////게임의 칸을 클릭 했을 때
	public void mousePressed(MouseEvent e) {
		if(number % 2 == cf.stoneColor){
			try{
				cf.out.writeUTF("[location]"+e.getX()+","+e.getY());
			} catch(IOException e1) {}
		}
	}
	
	
	public void clientPressed(int x, int y){
		int i=0, j=0;
		
/////////click 된 칸의 index
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
				
			///////빈 공간에 돌을 놓을 수 있는지 확인하는 작업
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
			okstr = "×";
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
	
////힌트 버튼을 눌렀을 때
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
