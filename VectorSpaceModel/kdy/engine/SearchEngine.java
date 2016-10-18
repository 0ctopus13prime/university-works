package kdy.engine;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import kdy.engine.document.Document;
import kdy.engine.document.Posting;
import kdy.engine.document.ScoreDoc;
import kdy.parser.BigramParser;
import kdy.parser.Parser;
import kdy.parser.Term;
import kdy.parser.TermVector;

public class SearchEngine {
	
	private PostingList postingList;
	private Parser parser;
	private Comparator<ScoreDoc> scoreDocComparator;
	
	public SearchEngine() {
		parser = new BigramParser();
		postingList = new PostingList(parser );
		scoreDocComparator = new Comparator<ScoreDoc>() {
			@Override
			public int compare(ScoreDoc o1, ScoreDoc o2) {
				double sub = o1.getScore() - o2.getScore();
				if( sub < 0 ) {
					return -1;
				}
				else if( sub == 0 ) {
					return 0;
				}
				else {
					return 1;
				}
			}
		};
	}
	
	public void writeData(Document doc) {
		//색인
		postingList.insertPosting(doc);
	}
	
	public ScoreDoc[] search(String query) {
		//파서가 쿼리를 파싱함
		TermVector tv = parser.parse(query);
		//idf값을 곱함
		Iterator<Term> it = tv.iterator();
		while(it.hasNext() ) {
			Term t = it.next();
			//idf값 먹임
			t.setFreq(t.getFreq()*postingList.getIdf(t.getValue()));
		}
		
		//바이그램으로 파싱된 텀들을 기반으로 문서리스트를 가져옴
		List<Posting[]> results = new LinkedList<Posting[]>();
		it = tv.iterator();
		while(it.hasNext() ) {
			Term t = it.next();
			Posting[] tmp = postingList.searchPostings(t.getValue());
			if( tmp != null ) {
				results.add(tmp);
			}
		}
		
		//결과가 존재한다면
		if( !results.isEmpty() ) {
			//곧 병함 들어갈 리스트
			List<Posting> mergeList = new LinkedList<Posting>();
			//처음 결과를 복사해둠
			for(Posting pst : results.get(0) ) {
				mergeList.add(pst);
			}
			
			//결과들을 병함합
			List<Posting> merged = merge(results, mergeList, 0);
			
			//스코어 기반으로 정렬할 셋
			TreeSet<ScoreDoc> pq = new TreeSet<ScoreDoc>(scoreDocComparator);
			
			//결과들과 유사도를 계산하여 셋에 삽입
			for(Posting pst : merged) {
				ScoreDoc sd = pst.produceScoreDoc(tv);
				pq.add(sd);
			}
			
			//배열로 변환
			ScoreDoc[] sdArr = new ScoreDoc[pq.size()];
			return pq.toArray(sdArr);
		}
		else {
			return null;
		}
	}
	
	//색인된 문서리스트를 병합함
	private List<Posting> merge(List<Posting[]> listlist, List<Posting> mergeList, int curr) {
		if( curr + 1 >= listlist.size() ) {
			return mergeList;
		}
		
		Posting[] next = listlist.get(curr+1);
		int i = 0; int j = 0;
		while( i<mergeList.size() && j<next.length ) {
			if( mergeList.get(i).getId() < next[j].getId() ) {
				i++;
			} 
			//병합된 리스트에 존재 하지 않으므로 삽입
			else if( mergeList.get(i).getId() > next[j].getId() ) {
				mergeList.add(i, next[j]);
				j++;
			}
			//이비 병합된 리스트에 들어 있음
			else {
				i++; j++;
			}
		}
		
		//나머지
		for( ; j<next.length ; j++ ) {
			mergeList.add(next[j]);
		}
		
		//남은것과 다시 병합
		return merge(listlist, mergeList, curr+1);
	}
}
