package kdy.vm;

/*
 * 가상기계에서 쓰이는 단위
 */
public class Word implements Comparable<Word> {
	//최대값과 최소값
	public final static int UNSIGNED_MAX_VALUE = 65535;
	public final static int SIGNED_MAX_VALUE = 32767;
	public final static int SIGNED_MIN_VALUE = -32768;
	
	//실제로는 int의 래퍼 클래스임
	private int value;
	
	public Word() {
	}
	
	public Word(int value) {
		this.value = value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	//값을 int값으로 변환
	public int intValue() {
		return this.value;
	}
	
	/*
	 * 사칙 연산 메소드들 
	 */
	
	public Word add(int value) {
		return 
		new Word(this.value += value);
	}
	
	public Word add(Word value) {
		return
		add(value.intValue());
	}
	
	public Word substract(int value) {
		return
		new Word(this.value -= value);
	}
	
	public Word substract(Word value) {
		return
		substract(value.intValue());
	}
	
	public Word multiply(int value) {
		return
		new Word(this.value * value);
	}
	
	public Word multiply(Word value) {
		return
		multiply(value.intValue());
	}
	
	public Word divide(int value) {
		return
		new Word(this.value/value);
	}
	
	public Word divide(Word value) {
		return
		divide(value.intValue());
	}
	
	public void negate() {
		this.value *= -1;
	}

	@Override
	public int compareTo(Word other) {
		return 
		this.value - other.value;
	}
	
	/*
	 * 동일성 비교에 대하여 값만 같으면 동일하다고 봄.
	 */
	@Override
	public boolean equals(Object other) {
		if( other instanceof Word ) {
			return this.compareTo((Word)other) == 0;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "" + value;
	}
}

