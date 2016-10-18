package kdy.vm.cpu;

import kdy.vm.Source;
import kdy.vm.Word;
import kdy.vm.memory.Memory;

/*
 * CPU 인터페이스 
 */
public interface Cpu {
	//메모리와 이 CPU를 연결시킴
	public void connectMemory(Memory memory);
	//소스를 세팅 시켜 관련 작업을 할 수 있도록 함.
	public void setSource(Source source);
	//명령어를 가져옴 
	public Word[] fetch();
	//op로 들어온 명령어를 실행 시킴
	//파라미터가 필요한 명령어라면 param을 참고함.
	public void execute(Word op, Word param);
	//끝났는지 알려주는 함수.
	public boolean EOF();
}
