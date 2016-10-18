package kdy.rtree;

import java.util.Comparator;

class Rectangle {
	private Point ws;
	private Point ne;
	
	public Rectangle() { }
	public Rectangle(Point ws, Point ne) {
		this.ws = ws;
		this.ne = ne;
	}
	
	public Point getWn() {
		return
		new Point(ws.getX(), ne.getY());
	}
	
	public void setWs(Point ws) {
		this.ws = ws;
	}
	
	public Point getSe() {
		return
		new Point(ne.getX(), ws.getY());
	}
	
	public void setNe(Point ne) {
		this.ne = ne;
	}	
	
	public Point getWs() {
		return ws;
	}
	
	public Point getNe() {
		return ne;
	}
	
	/*
	 * 다른 영역을 두고 
	 * 서로 겹치는지 검사함
	 */
	public boolean isOverlap(Rectangle other) {
		Rectangle small = null;
		Rectangle big = null;
		if( ws.getX() < other.ws.getX() ) {
			small = this;
			big = other;
		} else {
			small = other;
			big = this;
		}
		
		//X와 Y가 겹치는지에 대한 결과
		if( small.ws.getX() <= big.ws.getX() && 
				big.ws.getX() < small.ne.getX() ) {
			if( ws.getY() < other.ws.getY() ) {
				small = this;
				big = other;
			} else {
				small = other;
				big = this;
			}
			
			if( small.ws.getY() <= big.ws.getY() &&
					big.ws.getY() < small.ne.getY() ) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * 현재 자신의 MBR에 other를 더하면
	 * 얼마나 MBR이 증가하는지 알려주는 메소드
	 */
	public int getEnlargeMBRArea(Rectangle other) {
		int x1,y1, x2,y2;
		x1 = Math.min(ws.getX(), other.ws.getX());
		y1 = Math.min(ws.getY(), other.ws.getY());
		x2 = Math.max(ne.getX(), other.ne.getX());
		y2 = Math.max(ne.getY(), other.ne.getY());
				
		int newMbr = Math.abs((x1-x2)*(y1-y2));
		int currentMbr = getAreaFigure();
		return newMbr - currentMbr;
	}
	
	//영역 넓이를 리턴
	public int getAreaFigure() {
		return Math.abs(ne.getX() - ws.getX()) * 
				Math.abs(ne.getY() - ws.getY());
	}
	
	@Override
	public boolean equals(Object other) {
		if( other instanceof Rectangle ) {
			Rectangle o = (Rectangle)other;
			
			return
			ws.equals(o.ws) && ne.equals(o.ne);
		}
		
		return false;
	}

	@Override
	public String toString() {
		return ws.toString() + " / " + ne.toString();
	}
}
