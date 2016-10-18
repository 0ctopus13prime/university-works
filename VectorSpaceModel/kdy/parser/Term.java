package kdy.parser;

public class Term implements Comparable<Term> {
	private String value;
	private double freq;
	
	public Term() { }
	
	public Term(String value, int freq) {
		this(value);
		this.freq = freq;
	}
	
	public Term(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public double getFreq() {
		return freq;
	}

	public void setFreq(double freq) {
		this.freq = freq;
	}

	@Override
	public int compareTo(Term o) {
		return value.compareTo(o.value);
	}
}
