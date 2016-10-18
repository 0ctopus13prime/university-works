package bplustree;

class PlainNodeItem<KEY extends Comparable> extends Item<KEY> {
	protected KEY key; //키
	protected Node<KEY> biggerNode; //노드.키 >= 키를 만족하는 노드를 가르킴
	
	public PlainNodeItem(KEY key, Node<KEY> nodeP) {
		this.key = key;
		biggerNode = nodeP;
	}

	public int compareTo(Object other) {
		PlainNodeItem<KEY> otherNode = 
				(PlainNodeItem<KEY>)other;
		return this.key.compareTo(otherNode.getKey());
	}

	public KEY getKey() {
		return key;
	}

	public void setKey(KEY key) {
		this.key = key;
	}

	public Node<KEY> getBiggerNode() {
		return biggerNode;
	}

	public void setBiggerNode(Node<KEY> biggerNode) {
		this.biggerNode = biggerNode;
	}
}