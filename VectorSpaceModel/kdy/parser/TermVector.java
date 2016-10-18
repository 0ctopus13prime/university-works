package kdy.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TermVector {
	private Set<Term> termSet;
	
	public TermVector() {
		termSet = new TreeSet<Term>();
	}
	
	/*
	 * 각 텀들은 미리 리스트에 점수계산 한 다음에 삽입해야 함
	 * 안그러면 점수가 꼬일 수 있음
	 */
	public void addTerm(Term term) {
		termSet.add(term);
	}
	
	public void removeTerm(Term term) {
		termSet.remove(term);
	}
	
	public double getSimilarity(TermVector other) {
		Term[] arr1 = new Term[termSet.size()];
		termSet.toArray(arr1);
		Term[] arr2 = new Term[other.termSet.size()];
		other.termSet.toArray(arr2);
		
		double innerPrd = 0;
		int i = 0, j = 0;
		
		//내적을 구한다.
		while( i<arr1.length && j <arr2.length ) {			
			if( arr1[i].compareTo(arr2[j]) <0 ) {
				i++;
			}
			else if( arr2[j].compareTo(arr1[i]) <0 ) {
				j++;
			}
			else {
				//같다
				innerPrd += arr1[i].getFreq()*arr2[j].getFreq();
				i++; j++;
			}
		}
								
		if( innerPrd > 0 ) {
			//각 벡터 사이즈 구함
			float s1 = 0;
			float s2 = 0;
			
			Iterator<Term> it = termSet.iterator();
			while(it.hasNext() ) {
				Term t = it.next();
				s1 += t.getFreq()*t.getFreq();
			}
			
			it = other.termSet.iterator();
			while(it.hasNext() ) {
				Term t = it.next();
				s2 += t.getFreq()*t.getFreq();
			}
			
			//내적값 사이즈로 나눔
			innerPrd = innerPrd/(Math.sqrt(s1*s2));
		}
		
		//내적값 사이즈로 나눈 값이 코싸인이므로 아크코사인 날림
		return Math.acos(innerPrd);
	}
	
	public Iterator<Term> iterator() {
		return termSet.iterator();
	}
}
