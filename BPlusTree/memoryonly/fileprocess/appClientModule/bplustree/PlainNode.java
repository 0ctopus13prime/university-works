package bplustree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bplustree.exception.PlainNodeOverFlowException;

class PlainNode<KEY extends Comparable> extends Node<KEY> {
	
	
	private static int N;
	
	Node<KEY> p1;
	List<PlainNodeItem<KEY>> items;
	
	//n차 노드를 만듬
	//p1 k1p2 k2p3 ... kn-1pn
	public PlainNode(int n) {
		N = n;
		p1 = null;
		items = new ArrayList<PlainNodeItem<KEY>>(n-1);
	}
	
	public void insert(KEY key, Node<KEY> nodeP) throws PlainNodeOverFlowException {
		if( items.size() == N-1 ) {
			throw new PlainNodeOverFlowException();
		}
		PlainNodeItem<KEY> item = new PlainNodeItem<KEY>(key, nodeP);
		items.add(item);
	}
	

	@Override
	public void setParent(Node<KEY> parentNode) {
		this.parent = parentNode;
	}

	@Override
	public Node<KEY> getParent() {
		return this.parent;
	}

	@Override
	public void insert(Item<KEY> item) throws PlainNodeOverFlowException {
		if( items.size() >= N-1 ) {
			throw new PlainNodeOverFlowException();
		}
		
		items.add((PlainNodeItem<KEY>)item);
		Collections.sort(items);
	}

	@Override
	public Node<KEY> get(KEY key) {
		//뒤에 있다고 가정
		int biggestIndex = items.size()-1;
		for(int i = 0 ; i<items.size() ; i++ ) {
			if( key.compareTo(items.get(i).getKey()) < 0 ) {
				biggestIndex = i-1;
				break;
			}
		}
		
		//가장 작음
		if( biggestIndex < 0 ) {
			return p1;
		}
		
		return items.get(biggestIndex).getBiggerNode();
	}

	@Override
	public TempBuffer makeTempBuffer(Item<KEY> _extra) {
		PlainNodeItem<KEY> extra = (PlainNodeItem<KEY>)_extra;

		final List<PlainNodeItem<KEY>> buffer = 
				new ArrayList<PlainNodeItem<KEY>>(this.items);
		buffer.add((PlainNodeItem<KEY>)_extra);
		
		Collections.sort(buffer);
		
		final Node<KEY> orgP1 = this.p1;
		
		return new TempBuffer() {
			@Override
			public Map<String, Object> split() {
				Map<String, Object> map = new TreeMap<String, Object>();
				
				int splitIndex = (int)Math.ceil((N+1)/2f) - 1;
				PlainNode<KEY> orgNode = new PlainNode<KEY>(N);
				PlainNode<KEY> newNode = new PlainNode<KEY>(N);
				
				orgNode.p1 = orgP1;
				orgNode.items = buffer.subList(0, splitIndex);
				
				newNode.p1 = buffer.get(splitIndex).biggerNode;
				newNode.items = buffer.subList(splitIndex + 1, buffer.size());
				
				KEY splitKey = buffer.get(splitIndex).getKey();
				
				map.put(TempBuffer.NEW_NODE, newNode);
				map.put(TempBuffer.ORIGINAL_NODE, orgNode);
				map.put(TempBuffer.SPLIT_KEY, splitKey);
				
				return map;
			}
		};
	}

	@Override
	public void empty() {
		items.clear();
	}

	@Override
	public void copyFrom(Node<KEY> otherNode) {
		if( otherNode instanceof PlainNode ) {
			items = new ArrayList<PlainNodeItem<KEY>>(((PlainNode<KEY>)otherNode).items);
		}
	}
}
