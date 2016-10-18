package bplustree;

import java.util.Map;

import bplustree.exception.LeafNodeOverFlowException;
import bplustree.exception.PlainNodeOverFlowException;

public class BPlusTreeImpl<KEY extends Comparable, DATA> implements BPlusTree<KEY, DATA> {
	private static int N;
	
	private Node<KEY> root = null;
	
	public BPlusTreeImpl(int n) {
		N = n;
		root = new LeafNode<KEY, DATA>(N);
	}

	public void insert(KEY key, DATA data) {
		LeafNode<KEY, DATA> curr = findLeafNode(key);
		LeafNodeItem<KEY, DATA> item = null;
		try {
			 item = new LeafNodeItem<KEY, DATA>(key, data);
			curr.insert(item);
		} catch(LeafNodeOverFlowException e) {
			//리프 노드가 오버플로 남.
			
			System.out.println("KEY " + key + " 에서 리프노드 오버플러");
			
			TempBuffer tempBuffer = 
					curr.makeTempBuffer(item);
			Map<String, Object> map = 
					tempBuffer.split();
			curr.empty();
			curr.copyFrom((Node<KEY>)map.get(tempBuffer.ORIGINAL_NODE) );
			curr.setNext((LeafNode<KEY, DATA>)map.get(tempBuffer.NEW_NODE));
			insertIntoParent(curr, 
					(Node<KEY>)map.get(tempBuffer.NEW_NODE),
					(KEY)map.get(tempBuffer.SPLIT_KEY));
		}
		
	}
	
	protected void insertIntoParent(Node<KEY> orgNode,
			Node<KEY> newNode, KEY splitKey) {
				
		PlainNode<KEY> parentNode = null;
		if( orgNode == root ) {
			parentNode = new PlainNode<KEY>(N);
			orgNode.setParent(parentNode);
			parentNode.p1 = orgNode;
			newNode.setParent(parentNode);
			root = parentNode;
		} else {
			parentNode = (PlainNode<KEY>)orgNode.getParent();
			newNode.setParent(parentNode);
		}
				
		PlainNodeItem<KEY> item = null;
		try {
			 item = new PlainNodeItem<KEY>(splitKey, newNode);
			 parentNode.insert(item);
		} catch(PlainNodeOverFlowException e) {
			TempBuffer tempBuffer = 
					parentNode.makeTempBuffer(item);
			Map<String, Object> map = tempBuffer.split();
			
			parentNode.empty();
			parentNode.copyFrom((Node<KEY>)map.get(TempBuffer.ORIGINAL_NODE));
			insertIntoParent(
					parentNode, (Node<KEY>)map.get(TempBuffer.NEW_NODE),
					(KEY)map.get(TempBuffer.SPLIT_KEY));
		}
	}

	public void delete(KEY key) {
		
	}
	
	//key를 가지고 있을 법한 리프 노드를 반환
	protected LeafNode<KEY, DATA> findLeafNode(KEY key) {
		if( root instanceof LeafNode ) {
			return (LeafNode<KEY, DATA>)root;
		}
		
		//plainNode임
		Node<KEY> curr = root;
		while(true) {
			Node<KEY> next = (Node<KEY>)curr.get(key);
			curr = next;
			if( next instanceof LeafNode ) {
				break;
			}
		}
		
		return (LeafNode<KEY, DATA>)curr;
	}

	public DATA find(KEY key) {
		LeafNode<KEY, DATA> leafNode = findLeafNode(key);
		return (DATA)leafNode.get(key);
	}

	@Override
	public void printAll() {
		Node<KEY> curr = root;
		
		while( !(curr instanceof LeafNode) ) {
			curr = ((PlainNode<KEY>)curr).p1;
		}
		
		LeafNode<KEY, DATA> leafNode = (LeafNode<KEY, DATA>)curr;
		
		while(leafNode != null) {
			for( LeafNodeItem<KEY, DATA> item :  leafNode.items ) {
				System.out.println("KEY : " + item.getKey() + "  DATA : " + item.getData());
			}
			
			System.out.println(" ||| ");
			leafNode = leafNode.next;
		}
	}
	
}
