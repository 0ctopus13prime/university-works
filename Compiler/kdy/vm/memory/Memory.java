package kdy.vm.memory;

import kdy.vm.Source;
import kdy.vm.Word;

/*
 * 메모리 인터페이스
 * 특별할 것 없이 소스를 로드하고 
 * 주소값에 쓰고 읽는다.
 */
public interface Memory {
	//소스 로드
	public void loadSource(Source source);
	//주소값에 값을 씀
	public void write(Word addr, Word value);
	//주소값의 워드를 읽음
	public Word read(Word addr);
	//메모리 덤프 함
	public void dump();
}
