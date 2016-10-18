package bplustree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bplustree.exception.LeafNodeOverFlowException;
import bplustree.exception.PlainNodeOverFlowException;


public class Tests {
	
	@Test
	public void leafNodeTest() {
		LeafNode<String, Integer> leafNode = 
				new LeafNode<String, Integer>(5);
		LeafNodeItem<String, Integer> item1 = 
				new LeafNodeItem<String, Integer>("abc", 10);
		LeafNodeItem<String, Integer> item2 = 
				new LeafNodeItem<String, Integer>("bcd", 11);
		LeafNodeItem<String, Integer> item3 = 
				new LeafNodeItem<String, Integer>("efg", 12);
		LeafNodeItem<String, Integer> item4 = 
				new LeafNodeItem<String, Integer>("cde", 13);
		LeafNodeItem<String, Integer> item5 = 
				new LeafNodeItem<String, Integer>("bzz", 14);
		try {
			leafNode.insert(item1);
			leafNode.insert(item2);
			leafNode.insert(item3);
			leafNode.insert(item4);
			leafNode.insert(item5);
		} catch (LeafNodeOverFlowException e) {
			e.printStackTrace();
			TempBuffer tempBuffer = 
					leafNode.makeTempBuffer(item5);
			Map<String, Object> map = 
					tempBuffer.split();
			leafNode.empty();
			leafNode.copyFrom(
					(LeafNode<String, Integer>)map.get(TempBuffer.ORIGINAL_NODE));
			leafNode.setNext((LeafNode<String, Integer>)map.get(TempBuffer.NEW_NODE));
			
			
			for(LeafNodeItem<String, Integer> i : leafNode.items ) {
				System.out.println(i.getKey());
			}
			System.out.println("---------");
			for(LeafNodeItem<String, Integer> i : leafNode.next.items ) {
				System.out.println(i.getKey());
			}
			
		}
	}
	
	@Test
	static public void plainNodeTest() {
		PlainNode<String> plainNode = new PlainNode<String>(5);
		
		PlainNodeItem<String> item1 = new PlainNodeItem<String>("abc", null);
		PlainNodeItem<String> item2 = new PlainNodeItem<String>("cba", null);
		PlainNodeItem<String> item3 = new PlainNodeItem<String>("bzz", null);
		PlainNodeItem<String> item3_5 = new PlainNodeItem<String>("bxx", null);
		PlainNodeItem<String> item4 = new PlainNodeItem<String>("def", null);
		
		try {
			plainNode.insert(item1);
			plainNode.insert(item2);
			plainNode.insert(item3);
			plainNode.insert(item3_5);
			plainNode.insert(item4);
		} catch(PlainNodeOverFlowException e) {
			e.printStackTrace();
			
			TempBuffer tempBuffer = 
					plainNode.makeTempBuffer(item4);
			Map<String, Object> map = tempBuffer.split();
			
			
			
			//plainNode.empty();
			//plainNode.copyFrom((Node<String>)map.get(TempBuffer.ORIGINAL_NODE));
			
			for(PlainNodeItem<String> item : ((PlainNode<String>)map.get(TempBuffer.ORIGINAL_NODE)).items) {
				System.out.println(item.key);
			}
			
			for(PlainNodeItem<String> item : ((PlainNode<String>)map.get(TempBuffer.NEW_NODE)).items) {
				System.out.println(item.key);
			}
			
			
			
			System.out.println(map.get(TempBuffer.SPLIT_KEY));
		}
		
	}
}

