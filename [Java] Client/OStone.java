/* Author: Jeesoo Min 
Email: kuongee@gmail.com
Completion date
Version 1: 2014-01-11 ~ 2014-01-14
Updated: 2017-12-21*/
import java.awt.*;


public class OStone {
	
	private int sizeofStone;
	private int xofStone;
	private int yofStone;
	private Color color;
	private int myStone = 0;	//1은 흰 돌 -1은 검은 돌
	
	private int numberofStone = 32;
	
	public OStone(){
		myStone = 0;
	}
	
	public void setColorofStone(){
		if(myStone == 1)
			color = Color.white;
		else if(myStone == -1)
			color = Color.black;
	}
	public Color getColorofStone(){
		return color;
	}
	
	public void setMystone(int s){
		myStone = s;
		setColorofStone();
	}
	public int getMystone(){
		return myStone;
	}
	
////돌의 크기와 x,y좌표와 색
	public void setXofStone(int x){
		xofStone = x+10;
	}
	public void setYofStone(int y){
		yofStone = y+10;
	}
	public void setSizeofStone(int s){
		sizeofStone = s-20;
	}
	
	public int getXofStone() {
		return xofStone;
	}
	public int getYofStone() {
		return yofStone;
	}
	public int getSizeofStone() {
		return sizeofStone;
	}

	public int getNumberofStone() {
		return numberofStone;
	}
	public void setNumberofStone(int numberofStone) {
		this.numberofStone = numberofStone;
	}

}
