package bplustree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bplustree.exception.LeafNodeOverFlowException;

class LeafNode<KEY extends Comparable, DATA> extends Node<KEY> {
	
	List<LeafNodeItem<KEY, DATA>> items;
	LeafNode<KEY, DATA> next;
	
	//n차 노드를 만듬
	//p1 k1p2 k2p3 ... kn-1pn
	public LeafNode(int n) {
		next = null;
		N = n;
		parent = null;
		items = new ArrayList<LeafNodeItem<KEY, DATA>>(N-1);
	}

	@Override
	public void setParent(Node<KEY> parentNode) {
		parent = parentNode;
	}

	@Override
	public Node<KEY> getParent() {
		return parent;
	}

	@Override
	public void insert(Item<KEY> item) throws LeafNodeOverFlowException {
		if( items.size() >= N-1 ) {
			throw new LeafNodeOverFlowException();
		}
		items.add((LeafNodeItem<KEY,DATA>)item);
		//쏘트함
		Collections.sort(items);
	}

	@Override
	public DATA get(KEY key) {
		for(LeafNodeItem<KEY, DATA> item : items) {
			KEY _key = item.getKey();
			if( _key.compareTo(key) == 0 ) {
				return item.getData();
			}
		}
		
		return null;
	}

	@Override
	public TempBuffer makeTempBuffer(final Item<KEY> extra) {
		
		final List<LeafNodeItem<KEY, DATA>> buffer =
				new ArrayList<LeafNodeItem<KEY, DATA>>(this.items); 
		
		buffer.add((LeafNodeItem<KEY, DATA>)extra);
		Collections.sort(buffer);
				
		return
		new TempBuffer() {
			public Map<String, Object> split() {
				int splitIndex = (int)Math.ceil(N/2f);
				KEY splitKey = buffer.get(splitIndex).getKey();
				
				LeafNode<KEY, DATA> orgNode = 
						new LeafNode<KEY, DATA>(N);
				LeafNode<KEY, DATA> newNode = 
						new LeafNode<KEY, DATA>(N);
				
				orgNode.items = buffer.subList(0, splitIndex);
				newNode.items = buffer.subList(splitIndex, buffer.size() );
								
				Map<String, Object> map = new TreeMap<String, Object>();
				
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
		//리프노드는 리프노드끼리만 복사가 가능하다.
		if( otherNode instanceof LeafNode ) {
			LeafNode<KEY, DATA> _otherNode
				= (LeafNode<KEY, DATA>)otherNode;
			//복사
			items = new ArrayList<LeafNodeItem<KEY, DATA>>(_otherNode.items);
			//쏘팅
			Collections.sort(items);
		}
	}
	
	public void setNext(LeafNode<KEY, DATA> nextNode) {
		LeafNode<KEY, DATA> temp = next;
		this.next = nextNode;
		nextNode.next = temp;
	}

}
