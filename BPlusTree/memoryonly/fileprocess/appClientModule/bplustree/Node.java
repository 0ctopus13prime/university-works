package bplustree;

import bplustree.exception.OverFlowException;

abstract class Node<KEY extends Comparable> {	
	protected int N;
	protected Node<KEY> parent;
	
	abstract public void setParent(Node<KEY> parentNode);
	abstract public Node<KEY> getParent();
	abstract public void insert(Item<KEY> item) throws OverFlowException;
	abstract public Object get(KEY key);
	abstract public TempBuffer makeTempBuffer(Item<KEY> extra);
	abstract public void empty();
	abstract public void copyFrom(Node<KEY> otherNode);
	
}
