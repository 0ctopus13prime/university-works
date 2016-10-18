package kdy.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import kdy.engine.document.Document;
import kdy.engine.document.Posting;
import kdy.parser.Parser;
import kdy.parser.Term;
import kdy.parser.TermVector;

public class PostingList {
	private Map<String, TreeSet<Posting>> map;
	private Parser parser;
	//전체 문서 수
	private int total;
	//문서 아이디
	private int id;
	//포스팅 비교자, id로 정렬하려 함
	private Comparator<Posting> postingComparator;
	
	public PostingList(Parser parser) {
		this.parser = parser;
		map = new HashMap<String, TreeSet<Posting>>();
		total = 0;
		id = 0;
		postingComparator = new Comparator<Posting>() {
			@Override
			public int compare(Posting o1, Posting o2) {
				return o1.getId() - o2.getId();
			}
		};
	}
	
	public double getIdf(String term) {
		int docs = 1;
		if( map.containsKey(term) ) {
			docs = map.get(term).size();
		}
		
		double r = Math.log10(total/docs);
		r = r <= 0 ? 1 : r;
		return r;
	}
	
	public Posting[] searchPostings(String term) {
		if( map.containsKey(term) ) {
			TreeSet<Posting> pstSet = map.get(term);
			Posting[] postings = new Posting[pstSet.size()];
			pstSet.toArray(postings);
			return postings;
		}
		
		return null;
	}
	
	public void insertPosting(Document doc) {
		Posting posting = new Posting(id++);
		//데이터 옮기고
		posting.setData(doc.getData());
		
		//텀벡터 셋팅
		TermVector tv = parser.parse(doc.getText() );
		posting.setTermVector(tv);
		
		//각 텀에 대하여 포스팅 리스트에 삽입
		Iterator<Term> it = tv.iterator();
		while(it.hasNext() ) {
			Term t = it.next();
			TreeSet<Posting> list = map.get(t.getValue());
			if( list == null ) {
				//문서번호로 정렬하게 됨
				list = new TreeSet<Posting>(postingComparator);
				map.put(t.getValue(), list);
			}
			
			list.add(posting);
		}
	}
}
