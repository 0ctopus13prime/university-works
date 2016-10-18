package kdy.parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class BigramParser implements Parser {
	
	@Override
	public TermVector parse(String str) {
		char[] charArr = str.toCharArray();
		
		Map<String, Term> map = new TreeMap<String, Term>();
		
		char f,s;
		char a = '가', b = '힣';
		double maxFreq = 1;
		
		for(int i = 0 ; i<charArr.length-1 ; i++ ) {
			f = charArr[i];
			s = charArr[i+1];
			if( ( (int)a <= f && 
					f <= (int)b ) && ( (int)a <= s && s <= (int)b ) ) {
				String bigram = "" + f + s;
				Term term = map.get(bigram);
				if( term == null ) {
					//없으면 그냥 삽입
					term = new Term(bigram, 1);
					map.put(bigram, term);
				} else {
					//텀 빈도 기록
					term.setFreq(term.getFreq() + 1);
					//max 텀 빈도 기록
					if( term.getFreq() > maxFreq)  {
						maxFreq = term.getFreq();
					}
				}
			}
		}
		
		//생성한 바이그램 텀을 삽입
		Iterator<Entry<String, Term>> it = map.entrySet().iterator();
		TermVector termVector = new TermVector();
		while(it.hasNext() ) {
			Term t = it.next().getValue();
			t.setFreq(t.getFreq()/maxFreq);
			termVector.addTerm(t);
		}
		
		return termVector;
	}

}
