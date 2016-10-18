package kdy.rtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Node<DATA> implements NodeEntry {
	protected Rectangle area;
	protected List<NodeEntry> entrySet;
	protected Node<DATA> papa;
	
	public Node() {
		entrySet = new ArrayList<NodeEntry>();
		area = new Rectangle(new Point(0,0), new Point(0,0));
	}
	
	public Node(Rectangle area) {
		this.area = area;
	}
	
	public boolean isUnderflow(int M) {
		return entrySet.size() <= M/2;
	}
	
	public boolean isOverflow(int M) {
		return entrySet.size() > M;
	}
	
	public void insert(NodeEntry entry) {
		entrySet.add(entry);
		if( entry instanceof Node ) {
			((Node<DATA>)entry).setParent(this);
		}
	}
	
	/*
	 * 자신의 MBR조정
	 */
	public void adjustMBR() {
		if( entrySet.isEmpty() ) 
			return;
		
		//최소 ws.x
		//최소 ws.y
		int x = entrySet.get(0).getArea().getWs().getX();
		int y = entrySet.get(0).getArea().getWs().getY();
		
		for(NodeEntry e : entrySet ) {
			Point p = e.getArea().getWs();
			int _x = p.getX();
			int _y = p.getY(); 
			
			if( _x < x ) {
				x = _x;
			}
			if( _y < y ) {
				y = _y;
			}
		}
		area.setWs(new Point(x, y) );
		
		//최대 ne.x
		//최대 ne.y
		x = entrySet.get(0).getArea().getNe().getX();
		y = entrySet.get(0).getArea().getNe().getY();
		
		for(NodeEntry e : entrySet ) {
			Point p = e.getArea().getNe();
			int _x = p.getX();
			int _y = p.getY(); 
			
			if( _x > x ) {
				x = _x;
			}
			if( _y > y ) {
				y = _y;
			}
		}
		
		area.setNe(new Point(x, y));
	}
		
	public void setParent(Node<DATA> papa) {
		this.papa = papa;
	}
	
	public Node<DATA> getParent() {
		return this.papa;
	}
	
	public List<NodeEntry> getEntrySet() {
		return entrySet;
	}
	
	public void removeEntry(NodeEntry entry) {
		entrySet.remove(entry);
	}
	
	/*
	 * 노드를 쪼갠다.
	 * 그래서 기존 노드는 절반을 가지게 되고
	 * 나머지 절반을 새로운 노드를 생성하여 그 노드에 담고
	 * 그 노드를 리턴한다.
	 */
	public Node<DATA> split(int M) {
		return this.linearSplit(M);
	}
	
	
	/*
	 * 최종 선별된 시드 2개를 가지고 그룹핑하고
	 * 결과적으로 새로운 노드를 반환함
	 */
	private Node<DATA> grouping(NodeEntry[] finalSeeds, int M) {
		//다시한번 카피함
		List<NodeEntry > copy = new ArrayList<NodeEntry>(entrySet);
		entrySet.clear();
		entrySet.add(finalSeeds[0]);
		adjustMBR();
		
		//새로운 노드를 생성
		//리프인지 일반 노드인지 판별한다음 해당하는 노드로 생성
		Node<DATA> newNode = null;
		if( this instanceof LeafNode ) {
			newNode = new LeafNode<DATA>();
		} else {
			newNode = new PlainNode<DATA>();
		}
		newNode.insert(finalSeeds[1]);
		newNode.adjustMBR();
		
		//그룹에 넣기
		for(int i = 0 ; i<copy.size() ; i++) {
			NodeEntry e = copy.get(i);
			
			//다 되었는지 체크하여 
			//다 되었다면 나머지 노드에 다 넣음
			if( entrySet.size() >= M/2 ) {
				for(int j = i ; j<copy.size() ; j++ ) {
					NodeEntry f = copy.get(j);
					if( f != finalSeeds[0] && 
							f != finalSeeds[1] ) {
						newNode.insert(f);
						if( f instanceof Node ) {
							((Node<DATA>)f).setParent(newNode);
						}
					}
				}
				newNode.adjustMBR();
				break;
			}
			else if( newNode.entrySet.size() >= M/2 ) {
				for(int j = i ; j<copy.size() ; j++ ) {
					NodeEntry f = copy.get(j);
					if( f != finalSeeds[0] && 
							f != finalSeeds[1] ) {
						insert(f);
						if( f instanceof Node ) {
							((Node<DATA>)f).setParent(this);
						}
					}
				}				
				newNode.adjustMBR();
				break;
			}
			
			//시드가 아닌 박스들에 대하여 
			//최종 시드 2개와 확장되어야할 MBR을 비교하여
			//최소로 확장되는 시드쪽 집합으로 들어간다.
			if( e != finalSeeds[0] && 
				e != finalSeeds[1] ) {
				int mbr1 = this.area.getEnlargeMBRArea(e.getArea());
				int mbr2 = newNode.getArea().getEnlargeMBRArea(e.getArea());
				if( mbr1 < mbr2 ) {
					this.insert(e);
					this.adjustMBR();
				} 
				else if( mbr1 > mbr2 ) {
					newNode.insert(e);
					newNode.adjustMBR();
				} 
				//확장되는 MBR이 같음
				else {
					if( this.area.getAreaFigure() < 
							newNode.area.getAreaFigure() ) {
						this.insert(e); this.adjustMBR();
					} else {
						newNode.insert(e); newNode.adjustMBR();
					}
				}
			}
		}
				
		return newNode;
	}
	
	
	/*
	 * Quadratic 방식으로 스플릿하고 새로운 노드를 리턴함
	 */
	private Node<DATA> quadraticSplit(int M) {
		//엔트리 셋 카피함
		List<NodeEntry > copy = new ArrayList<NodeEntry>(entrySet);
		//시드 두개를 고름
		NodeEntry[] finalSeed = pickFinalSeedQuadratic(copy);
				
		return grouping(finalSeed, M);
	}
	
	
	/*
	 * Quadratic방식으로 최종 두개의 시드를 생성함
	 */
	private NodeEntry[] pickFinalSeedQuadratic(List<NodeEntry> copy) {
		int maxEnlarged = -1;
		NodeEntry[] finalSeeds = new NodeEntry[2];
		
		//2중 루브 Quadratic
		for(int i = 0 ; i<copy.size() ; i++ ) {
			NodeEntry n = copy.get(i);
			for(int j = i+1 ; j<copy.size() ; j++ ) {
				NodeEntry nn = copy.get(j);
				int enlarged 
					= n.getArea().getEnlargeMBRArea(nn.getArea() ) + n.getArea().getAreaFigure(); 
				if( enlarged > maxEnlarged ) {
					maxEnlarged = enlarged;
					finalSeeds[0] = n;
					finalSeeds[1] = nn;
				}
			}
		}
		
		return finalSeeds;
	}

	/*
	 * 무작위 방식으로 스플릿한다.
	 */
	private Node<DATA> exhaustiveSplit(int M) {
		//최소로 선택되어야 하는 그룹의 개수
		int start = entrySet.size() - M;
		
		List<NodeEntry> group1 = new ArrayList<NodeEntry>();
		List<NodeEntry> copy = new ArrayList<NodeEntry>(entrySet); 
		
		//새로운 노드 생성
		Node<DATA> newNode = null;
		if( this instanceof LeafNode ) {
			newNode = new LeafNode<DATA>();
		} else {
			newNode = new PlainNode<DATA>();
		}
		
		List<NodeEntry> finalGroup1 = new ArrayList<NodeEntry>();
		List<NodeEntry> finalGroup2 = new ArrayList<NodeEntry>();
		
		//배열로 넘겨 주어 계속된 재귀에서도 수정되게 함.
		int[] min = new int[1];
		min[0] = Integer.MAX_VALUE;
		
		//최소로 선택되어야할 개수부터 M까지 그루핑 시작
		//ex 2 / M-1, 3 / M-2 ... M-1 / 2
		for(int i = start+1 ; i<M ; i++ ) {
			group1.clear();
			
			//복사본, 그룹1, 그룹1로 선택해야 하는 개수, 
			//현재까지 최소 MBR, 최종 그룹1, 최종 그룹2
			exhaustiveGrouping(copy,
					group1, 
					i,
					min,
					finalGroup1, 
					finalGroup2);
		}
		
		
		/*
		 * 그룹이 완성되었으니 부모 자식관계를 확정 짓고
		 * MBR을 확정 짓는다
		 */
		entrySet.clear();
		for(NodeEntry ne : finalGroup1 ) {
			insert(ne);
		}
		adjustMBR();
		
		for(NodeEntry ne : finalGroup2 ) {
			newNode.insert(ne);
		}
		newNode.adjustMBR();
		
		return newNode;
	}
	
	/*
	 * 무작위 방식으로 그룹핑 함
	 * copy : 현재 엔트리 복사본,
	 * group1 : 현재까지 선택된 group1,
	 * select : group1에 몇개를 선택하여 넣어야 하는가,
	 * min : 현재까지 기록된 최소 MBR,
	 * finalGroup1 : 최종 그룹1,
	 * finalGroup2 : 최종 그룹2
	 */
	private void exhaustiveGrouping(List<NodeEntry> copy, 
			List<NodeEntry> group1, 
			int select,
			int[] min,
			List<NodeEntry> finalGroup1, 
			List<NodeEntry> finalGroup2) {
		
		if( select == 0 ) {
			//최종 그룹핑
			Node<DATA> node1 = new PlainNode<DATA>();
			Node<DATA> node2 = new PlainNode<DATA>();
			
			for(NodeEntry ne : group1) {
				node1.insert(ne);
			}
			for(NodeEntry ne : copy) {
				node2.insert(ne);
			}
			node1.adjustMBR();
			node2.adjustMBR();
			
			int mbr = node1.getArea().getAreaFigure() +
					node2.getArea().getAreaFigure();
			
			//현재까지 mbr보다 작으면 기록
			if( mbr < min[0] ) {
				min[0] = mbr;
				finalGroup1.clear();
				finalGroup2.clear();
				for(NodeEntry ne : group1 ) {
					finalGroup1.add(ne);
				}
				for(NodeEntry ne : copy ) {
					finalGroup2.add(ne);
				}
			}
			
			return;
		}
		
		for(int i = 0 ; i<copy.size() ; i++ ) {
			group1.add(copy.get(i));
			copy.remove(i);
			//현재 하나를 선택하였으니 
			//나머지에서 다시 select-1만큼 선택하라
			exhaustiveGrouping(copy, group1, select-1, min, finalGroup1, finalGroup2);
			
			//다시 원상 복귀
			copy.add(i, group1.get(group1.size()-1));
			group1.remove(group1.size()-1);
		}
	}
	
	/*
	 * Linear Split 방식 
	 */
	private Node<DATA> linearSplit(int M) {
		//엔트리 셋 카피함
		List<NodeEntry > copy = new ArrayList<NodeEntry>(entrySet);
		
		//시드 두개를 고름
		NodeEntry[] finalSeed = pickFinalSeedLinear(pickSeedCandidateLinear(copy));
				
		return grouping(finalSeed, M);
	}
	

	//linear cost 방식으로 4개의 박스를 골라 낸다.
	//결과로 4개 NodeEntry가 담긴 배열을 리턴한다.
	//반드시 인수로 복사본이 들어와야 함
	private NodeEntry[] pickSeedCandidateLinear(List<NodeEntry> copy) {
		NodeEntry[] candidates = new NodeEntry[4];
		
		//시드를 선택함
		for(int j = 0 ; j<4 ; j++ ) {
			NodeEntry target = copy.get(0);
			for(int i = 1 ; i<copy.size() ; i++) {
				//최대 wn.x
				if( j == 0 ) {
					if( copy.get(i).getArea().getWn().getX() > 
						target.getArea().getWn().getX() ) {
						target = copy.get(i);
					}
				}
				//최소 wn.y
				else if( j == 1 ) {
					if( copy.get(i).getArea().getWn().getY() < 
						target.getArea().getWn().getY() ) {
						target = copy.get(i);
					}
				}
				//최소 se.x
				else if( j == 2 ) {
					if( copy.get(i).getArea().getSe().getX() < 
						target.getArea().getSe().getX() ) {
						target = copy.get(i);
					}
				}
				//최대 se.y
				else if( j == 3 ) {
					if( copy.get(i).getArea().getSe().getY() > 
						target.getArea().getSe().getY() ) {
						target = copy.get(i);
					}
				}
			}
			
			//골라낸 시드를 배열에 옮김
			candidates[j] = target;
			//중복 방지를 위해 복사본에서 삭제함
			copy.remove(target);
		}
		
		return candidates;
	}
	
	//pickSeedCandidate에서 뽑은 후보4개 박스 가지고
	//최종 시드 박스를 선택한다.
	//결과로 2개의 NodeEntry가 담긴 배열을 리턴한다.
	private NodeEntry[] pickFinalSeedLinear(NodeEntry[] candidate) {
		int tempMBR = -1;
		NodeEntry[] finalSeed = new NodeEntry[2];

		//4C2 = 6번 돌아감
		for(int i = 0 ; i<candidate.length-1 ; i++ ) {
			for(int j = i+1 ; j<candidate.length ; j++ ) {
				//확장 시킨 MBR이 큰것으로 한다.
				int foo = candidate[i].getArea()
						.getEnlargeMBRArea(candidate[j].getArea()) + 
						candidate[i].getArea().getAreaFigure();
				
				if( foo > tempMBR ) {
					tempMBR = foo;
					finalSeed[0] = candidate[i];
					finalSeed[1] = candidate[j];
				}
			}
		}
		
		return finalSeed;
	}
	
	/*
	 * 현재 높이를 반환한다.
	 * 리프는 1이고
	 * 리프의 부모는 2
	 * 결국 루트의 height는 전체 트리의 높이로 반환도니다.
	 */
	public int getMyHeight() {
		Node<DATA> curr = this;
		int myHeight = 0;
		while(true) {
			myHeight++;
			//리프 노드면 리턴
			if( curr instanceof LeafNode ) {
				return myHeight;
			}
			
			curr = (Node<DATA>)curr.getEntrySet().get(0);
		}
	}
	
	/*
	 * 현재 MBR을 얻는다.
	 */
	@Override
	public Rectangle getArea() {
		return this.area;
	}
}
