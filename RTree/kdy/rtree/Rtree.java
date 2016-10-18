package kdy.rtree;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Rtree<DATA> {
	private Node<DATA> root = null;
	private int elemNum = 0;
	private int M;
	
	public Rtree(int M) {
		if( M < 4 ) {
			throw new IllegalArgumentException();
		}
		this.M = M;
	}
	
	/*
	 * 영역과 데이터를 가지고 삽입함
	 */
	public void insert(Rectangle area, DATA data) {
		RData<DATA> rdata = new RData<DATA>(area, data);
		insertInner(rdata, null);
	}
	
	/*
	 * 범용 삽입 메소드.
	 * 일반적인 삽입에서 재삽입 할 때 이 메소드를 사용한다.
	 * height는 재삽입시 높이를 맞추기위한 인수로써
	 * 일반적인 삽입에서는 null이다.
	 */
	private void insertInner(NodeEntry entry, Integer height) {
		//System.out.println("-insert inner");
		Node<DATA> splitedNode = null;
		
		//비어 있으면 바로 삽입
		if( root == null ) {
			//System.out.println("  create initial root");
			root = new LeafNode<DATA>();
			root.insert((RData<DATA>)entry);
			return;
		}
		
		//삽입해야할 리프 노드를 선택
		Node<DATA> node = chooseLeaf(entry.getArea(), height);
		
		node.insert(entry);
		
		//오버플러나면 스플릿 함
		if( node.isOverflow(M) ) {
			//System.out.println("  overflow node! split node");
			splitedNode = node.split(M);
		}
		
		//트리 조정함
		adjustTree(node, splitedNode);
	}
	
	/*
	 * 삽입해야 하는 리프노드를 선택해서 리턴한다.
	 * height는 높이를 맞추어 삽입해야 할 때
	 * 삽입해야 하는 노드를 리턴한다.
	 * 따라서, 일반적인 삽입시에는 height는 null이다.
	 */
	private Node<DATA> chooseLeaf(Rectangle area, Integer height) {
		//System.out.println("-choose leaf");
		Node<DATA> curr = root;
				
		//리프노드가 아닐때 까지
		while(!(curr instanceof LeafNode) ) {
			if( height != null ) {
				if( curr.getMyHeight() == height ) {
					return curr;
				}				
			}
			
			List<NodeEntry> entry = curr.getEntrySet();
			NodeEntry min = null;
			int minEnlargement = Integer.MAX_VALUE;
			for(int i = 0 ; i<entry.size() ; i++ ) {
				int foo = entry.get(i).getArea().getEnlargeMBRArea(area);
				if( foo < minEnlargement ) {
					min = entry.get(i);
					minEnlargement = foo;
				}
			}
			
			curr = (Node<DATA>)min;
		}
		
		return curr;
	}
	
	/*
	 * 트리에 삽입하고 루트까지 
	 * 트리를 조정하는 메소드
	 * n은 방금 삽입된 노드이고 nn은 스플릿된 노드이다.
	 */
	private void adjustTree(Node<DATA> n, Node<DATA> nn) {
		//System.out.println("-adjust tree");
		Node<DATA> p = n.getParent();
		Node<DATA> pp = null;
		
		//MBR조정하고
		n.adjustMBR();
		if( nn != null ) {
			nn.adjustMBR();
		}
		
		//새로운 루트를 생성해야 하는지?
		if( n == root ) {
			if( nn != null ) {
				//System.out.println("  create new root");
				Node<DATA> newRoot = new PlainNode<DATA>();
				newRoot.insert(n);
				newRoot.insert(nn);
				newRoot.adjustMBR();
				root = newRoot;
				return;
			}
			return;
		}
		
		//스플릿된 노드가 있다면
		if( nn != null ) {
			//부모에 삽입
			p.insert(nn);
			if( p.isOverflow(M) ) {
				pp = p.split(M);
			}
		}
		
		//트리 윗부분으로 전파함
		adjustTree(p, pp);
	}
	
	/*
	 * 영역을 받아 데이터를 삭제한다.
	 */
	public void delete(Rectangle area) {
		//System.out.println("-delete");
		LeafNode<DATA> leaf = findLeafNode(root, area);
		
		if( leaf == null ) {
			//System.out.println("  delete failed! cannot find data");
			return;
		}
		
		//리프 노드에서 데이터를 뒤진다.
		for(NodeEntry e : leaf.getEntrySet() ) {
			//데이터를 찾았다.
			if( e.getArea().equals(area) ) {
				System.out.println("  remove data from leafnode");
				//엔트리에서 삭제
				leaf.removeEntry(e);
				break;
			}
		}
		
		//트리 압축
		condenseTree(leaf);
		
		//루트 삭제 여부
		if( root.getEntrySet().size() == 1 && root instanceof PlainNode ) {
			//루트의 자식을 루트로 만듬
			root = (Node<DATA>)root.getEntrySet().get(0);
			root.setParent(null);
		}
		else if( root.getEntrySet().isEmpty() ) {
			root = null;
		}
	}
	
	/*
	 * 삭제하고 나서 tree를 압축한다.
	 */
	private void condenseTree(LeafNode<DATA> leaf) {
		//System.out.println("-condenseTree");
		Stack<Node<DATA>> stack = new Stack<Node<DATA>>();
		Node<DATA> curr = leaf;
		Node<DATA> papa = null;
		
		do {
			papa = curr.getParent();
			
			//삭제가 일어난 노드가 underflow가 발생하면
			if( curr.isUnderflow(M) ) {
				//루트이고 잎노드임(삭제는 잎노드에서만 발생함)
				if( curr == root ) {
					curr.adjustMBR(); break;
				}	
				papa.removeEntry(curr);
				//스택에 그 노드를 삽입
				stack.push(curr);
			} else {
				curr.adjustMBR();
				if( curr == root ) {
					 break;
				}
				papa.adjustMBR();
			}
						
			curr = papa;
		} while(true);
		
		//재 삽입
		while( !stack.isEmpty() ) {
			Node<DATA> elem = stack.pop();
			int myHeight = elem.getMyHeight();
			//높이를 맞추어서 삽입한다.
			for(NodeEntry ne : elem.getEntrySet() ) {
				insertInner(ne, myHeight);
			}
		}
	}

	/*
	 * 영역을 받아 데이터를 검색한다.
	 */
	public DATA search(Rectangle area) {
		//뒤져서 데이터 발견
		LeafNode<DATA> leaf = findLeafNode(root, area);
		
		//리프 노드에서 데이터를 뒤진다.
		if( leaf != null ) {
			for(NodeEntry e : leaf.getEntrySet() ) {
				if( e.getArea().equals(area) ) {
					RData<DATA> data = (RData<DATA>)e;
					//데이터를 찾아 리턴
					return data.getData();
				}
			}
		}
		
		return null;
	}
	
	/*
	 * 범용 메소드임. 
	 * 
	 * seach에서도 쓰이고
	 * delete에서도 쓰임
	 * 
	 * 재귀적으로 찾아 들어가며
	 * 영역을 받아 그 영역과 정확하게 일치하는 RData를 가진 리프노드를 반환한다.
	 */
	private LeafNode<DATA> findLeafNode(Node<DATA> node, Rectangle area) {
		if( node == null ) {
			//System.out.println("node is null!");
			return null;
		} else {
			System.out.println("find leaf node MBR : " + node.getArea());	
			
			//리프노드일때 RData를 일일이 검사함
			if( node instanceof LeafNode ) {
				for(NodeEntry e : node.getEntrySet() ) {
					//영역 비교해서 같으면 찾으려는 데이터를 가지고 있는 리프노드를 반환
					if( e.getArea().equals(area) ) {
						return (LeafNode<DATA>)node;
					}
				}
			} 
			//일반 노드일때에는 겹치는 자식노드로 검색들어감
			else {
				for(NodeEntry e : node.getEntrySet() ) {
					System.out.println("OVERLAP ?? " + e.getArea() + " with " + area);
					if( e.getArea().isOverlap(area) ) {
						//일반 노드일 때 NodeEntry는 Node타입
						//재귀적으로 찾아 들어감
						LeafNode<DATA> found = findLeafNode((Node<DATA>)e, area);
						if( found != null ) {
							return found;
						}
					}
				}			
			}
		}
		
		return null;
	}
	
	/*
	 * 현재 트리의 노드와 Rtree의 높이를 나타냄
	 */
	public void test() {
		if( root != null ) {
			System.out.println("RTREE HEIGHT : " + root.getMyHeight());
		}
		else {
			System.out.println("RTREE HEIGHT : 0");
			return;
		}
		int elemNum = 0;
		Queue<NodeEntry> queue = new LinkedList<NodeEntry>();
		queue.add(root);
		NodeEntry curr = null;
		
		NodeEntry delim = new RData<DATA>();
		
		while(!queue.isEmpty() ) {
			curr = queue.remove();
			if( curr == delim ) {
				System.out.println("~~~~~~~~~~~");
				continue;
			}
			
			if( curr instanceof LeafNode ) {
				elemNum += ((Node<DATA>)curr).getEntrySet().size();
				LeafNode<DATA> foo = (LeafNode<DATA>)curr;
				System.out.println("=========");
				System.out.println("$$ MBR $$" + foo.getArea());
				
				for(NodeEntry ne : foo.getEntrySet() ) {
					RData<DATA> fee = (RData<DATA>)ne;
					System.out.println("  $$ RDATA $$ " + fee.getArea());
				}
					
				System.out.println("=========");
			} 
			else if( curr instanceof PlainNode ) {
				Node<DATA> foo = (Node<DATA>)curr;
				System.out.println("$$ MBR $$" + foo.getArea() + "\n");
				for(NodeEntry ne : foo.getEntrySet() ) {
					queue.add(ne);
				}
				queue.add(delim);
			}
		}
		
		System.out.println("elemNum : " + elemNum);
	}
}
