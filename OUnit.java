import java.awt.Toolkit;
import java.awt.geom.*;


public class OUnit {
	
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	int pw = OGamePanel.wh;   //panel�� �ʺ�
	
	private int xUnit, yUnit;
	
	private int widthUnit, heightUnit;
	
	OStone unitStone;
	private Rectangle2D rectUnit;	// �� ĭ�� Rectangle2D ��ü�� ������ �ִ�.(has-a relation)
	
	private boolean b_full = false;
	
	private int i_index, j_index;
	
	private boolean b_isOk = false;
	
	public OUnit(int x, int y, int i, int j){
		//������ �� ĭ�� ���� ���� �𼭸��� ��ǥ
		this.xUnit = x;	
		this.yUnit = y; 
		
		//�� ĭ�� ���̿� �ʺ�
		this.widthUnit = (int)(pw / 8); 
		this.heightUnit = (int)(pw / 8); 
		
		this.i_index = i;
		this.j_index = j;
		
		//Rectangle2D ��ü ����
		this.rectUnit = new Rectangle2D.Double((double)this.xUnit, (double)this.yUnit, (double)this.widthUnit, (double)this.heightUnit);
		
		this.unitStone = new OStone();
		
		this.unitStone.setXofStone(this.xUnit);
		this.unitStone.setYofStone(this.yUnit);
		this.unitStone.setSizeofStone(this.getWidthUnit());
		
		this.b_full = false;
		this.setStone(0);
	}
	
	public int getIindex() { return i_index; }
	public int getJindex() { return j_index; }
	public int getxUnit() { return xUnit;}
	public int getyUnit() { return yUnit; }
	public int getWidthUnit() {	return widthUnit; }
	public int getHeightUnit() { return heightUnit; }
	
	public int getStone() {	return unitStone.getMystone(); }
	public void setStone(int s) {
		unitStone.setMystone(s);	//stone�� ���� ������ �ƴ� ��
	}
	
	public void setRectUnit(int w, int h) {
		this.rectUnit.setRect((double)this.xUnit, (double)this.yUnit, (double)w, (double)h);
	}

	public Rectangle2D getRectUnit() { return rectUnit;	}

	public boolean isClicked(int x, int y){
		if(rectUnit.contains((double)x, (double)y))
			return true;
		else
			return false;
	}
	
	
	public void setIsFull(){
		b_full = true;        //���� �̹� �ִ� ��� true return;
	}
	public boolean isFull(){
		return b_full;
	}
	
	public void setIsOk(){
		b_isOk = true;
	}
	public void setNotOk(){
		b_isOk = false;
	}
	public boolean getIsOk(){
		return b_isOk;
	}
	
}
