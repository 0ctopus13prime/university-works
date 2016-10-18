package bplustree;

class LeafNodeItem<KEY extends Comparable, DATA> extends Item<KEY> {
	private KEY key; //키
	private DATA data; //블럭을 나타내고 있는 주소값
	
	public LeafNodeItem(KEY key, DATA data) {
		this.key = key;
		this.data = data;
	}

	public int compareTo(Object other) {
		LeafNodeItem<KEY, DATA> otherNode = 
				(LeafNodeItem<KEY, DATA>)other;
		return this.key.compareTo(otherNode.key);
	}

	public KEY getKey() {
		return key;
	}

	public void setKey(KEY key) {
		this.key = key;
	}

	public DATA getData() {
		return data;
	}

	public void setData(DATA data) {
		this.data = data;
	}
}