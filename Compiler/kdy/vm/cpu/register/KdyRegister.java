package kdy.vm.cpu.register;

import kdy.vm.Word;

public class KdyRegister implements Register {

	//레지스터 값
	protected Word value = new Word(0);
	
	@Override
	public Word get() {
		return value;
	}

	/*
	 * 값 설정
	 */
	@Override
	public void set(Word value) {
		this.value.setValue(value.intValue());
	}

	/*
	 * 값을 1증가 시킴
	 */
	@Override
	public void inc() {
		this.value.setValue(value.intValue()+1);
	}

	/*
	 * 값을 1감소 시킴
	 */
	@Override
	public void dec() {
		this.value.setValue(value.intValue()-1);
	}
}
