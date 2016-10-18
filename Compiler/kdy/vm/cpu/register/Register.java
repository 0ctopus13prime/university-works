package kdy.vm.cpu.register;

import kdy.vm.Word;

/*
 * 레지스터 인터페이스
 * 레지스터는 간단하다.
 * 
 * 값을 세팅하고, 
 * 편의상 감소와 증가 메소드를 추가함.
 */
public interface Register {
	public Word get();
	public void set(Word value);
	public void inc();
	public void dec();
}
