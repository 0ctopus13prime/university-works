package kdy.rtree;

/*
 * 영역과 데이터를 래핑한 
 * Rtree용 데이터
 * 노드의 자식으로 들어가므로 NodeEntry를 구현하였다.
 */
class RData<Data> implements NodeEntry{
	private Rectangle area;
	private Data data;
	
	public RData() { }
	public RData(Rectangle area, Data data) {
		this.area = area;
		this.data = data;
	}
	
	@Override
	public Rectangle getArea() {
		return area;
	}
	
	public void setArea(Rectangle area) {
		this.area = area;
	}
	
	public Data getData() {
		return data;
	}
	
	public void setData(Data data) {
		this.data = data;
	}
}
