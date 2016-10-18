package kdy.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import kdy.engine.PostingList;
import kdy.engine.document.Document;
import kdy.engine.document.Posting;
import kdy.engine.document.ScoreDoc;
import kdy.parser.BigramParser;
import kdy.parser.Parser;
import kdy.parser.Term;
import kdy.parser.TermVector;

import org.junit.Test;

public class Tests {
	//@Test
	public void test() {
		TermVector tv1 = new TermVector();
		TermVector tv2 = new TermVector();
		
		Term term1 = new Term("가나", 1);
		Term term2 = new Term("나가", 2);
		Term term3 = new Term("마바", 3);
		Term term4 = new Term("다라", 4);
		Term term5 = new Term("라바", 5);
		
		
		tv1.addTerm(term1);
		tv1.addTerm(term2);
		tv1.addTerm(term3);
		tv1.addTerm(term4);
		tv1.addTerm(term5);
				
		tv2.addTerm(new Term("없는", 3));
		tv2.addTerm(new Term("일부", 5));
		
		System.out.println(tv1.getSimilarity(tv2));
	}
	
	//@Test
	public void pltest() {
		PostingList pl = new PostingList(new BigramParser());
		Document doc = new Document();
		doc.setData("데이터1");
		doc.setText("자바를 자바라!!");
		
		pl.insertPosting(doc);
		
		Posting[] list = pl.searchPostings("자바");
		Posting p = list[0];
		TermVector tv = p.getTermVector();
		
		Iterator<Term> it = tv.iterator();
		while(it.hasNext() ) {
			Term t = it.next();
			System.out.println(t.getValue() + " : " + t.getFreq());
		}		
	}
	
	@Test
	public void searchTest() {
		//데이터 삽입
		Parser parser = new BigramParser();
		PostingList pl = new PostingList(parser);
		Document doc = new Document();
		doc.setData("데이터1");
		doc.setText("자바를 자바라!!");
		
		pl.insertPosting(doc);
		
		doc = new Document();
		doc.setData("데이터2");
		doc.setText("자바를 자바라!! 자바야 하느니라! 꼭!");
		pl.insertPosting(doc);
		
		String query = "자바";
		TermVector tv = parser.parse(query);
		Iterator<Term> it = tv.iterator();
		while(it.hasNext() ) {
			Term t = it.next();
			//idf값 먹임
			t.setFreq(t.getFreq()*pl.getIdf(t.getValue()));
		}
		
		List<Posting[]> results = new LinkedList<Posting[]>();
		it = tv.iterator();
		while(it.hasNext() ) {
			Term t = it.next();
			results.add(pl.searchPostings(t.getValue()));
		}
		
		
		if( !results.isEmpty() ) {
			List<Posting> mergeList = new LinkedList<Posting>();
			for(Posting pst : results.get(0) ) {
				mergeList.add(pst);
			}
			
			List<Posting> merged = merge(results, mergeList, 0);
			
			TreeSet<ScoreDoc> pq = new TreeSet<ScoreDoc>(new Comparator<ScoreDoc>() {
				@Override
				public int compare(ScoreDoc o1, ScoreDoc o2) {
					double sub = o2.getScore() - o1.getScore();
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
			});
					
			for(Posting pst : merged) {
				ScoreDoc sd = pst.produceScoreDoc(tv);
				pq.add(sd);
			}
			
			ScoreDoc[] sdArr = new ScoreDoc[pq.size()];
			pq.toArray(sdArr);
			
			for(ScoreDoc sd : sdArr ) {
				System.out.println(sd.getData() + " " + sd.getScore());
			}
		}
	}
	
	public List<Posting> merge(List<Posting[]> listlist, List<Posting> mergeList, int curr) {
		if( curr + 1 >= listlist.size() ) {
			return mergeList;
		}
		
		Posting[] next = listlist.get(curr+1);
		int i = 0; int j = 0;
		while( i<mergeList.size() && j<next.length ) {
			if( mergeList.get(i).getId() < next[j].getId() ) {
				i++;
			} 
			else if( mergeList.get(i).getId() > next[j].getId() ) {
				mergeList.add(i, next[j]);
				j++;
			}
			else {
				i++; j++;
			}
		}
		
		//나머지
		for( ; j<next.length ; j++ ) {
			mergeList.add(next[j]);
		}
		
		return merge(listlist, mergeList, curr+1);
	}
}

