import java.awt.*;


public class OStone {
	
	private int sizeofStone;
	private int xofStone;
	private int yofStone;
	private Color color;
	private int myStone = 0;	//1�� �� �� -1�� ���� ��
	
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
	
////���� ũ��� x,y��ǥ�� ��
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
