package kdy.engine.document;

import kdy.parser.TermVector;

public class Posting {
	private int id;
	private Object data;
	private TermVector termVector;
	
	public Posting(int id) {
		this.id = id;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public TermVector getTermVector() {
		return termVector;
	}
	public void setTermVector(TermVector termVector) {
		this.termVector = termVector;
	}
	
	public int getId() {
		return id;
	}
	
	public ScoreDoc produceScoreDoc(TermVector query) {
		ScoreDoc sd = new ScoreDoc();
		
		sd.setData(data);
		sd.setScore(termVector.getSimilarity(query));
		
		return sd;
	}
}
