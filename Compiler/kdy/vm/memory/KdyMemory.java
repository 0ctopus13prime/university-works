package kdy.vm.memory;

import java.util.ArrayList;
import java.util.List;

import kdy.vm.OpcodeFragment;
import kdy.vm.Source;
import kdy.vm.Word;

public class KdyMemory implements Memory {

	//실제로 쓰는 메모리 공간
	protected List<Word> memorySpace;

	public KdyMemory() {
		//실제로 쓰는 메모리 공간을 초기화 한다.
		memorySpace = new ArrayList<Word>(Word.UNSIGNED_MAX_VALUE);
		for(int i = 0 ; i<=Word.UNSIGNED_MAX_VALUE ; i++ ) {
			memorySpace.add(new Word(0));
		}
	}
	
	/*
	 * 소스를 실제 쓰는 메모리 공간에 로드 시킴
	 */
	@Override
	public void loadSource(Source source) {
		//opcodeFragment는 여러개일 수 있다.
		//그래서 루프를 돌면서 하나하나 불러 
		//memorySpace에 넣음
		for(OpcodeFragment of : source.getOpcodeList()) {
			int idx = source.getCodeInitPoint();
			List<Word> opcodeList = of.getOpcodeList();
			for(int i = 0 ; i<opcodeList.size() ; i++ ) {
				memorySpace.set(idx+i, opcodeList.get(i));
			}
		}
	}

	/*
	 * addr에 해당되는 곳에 value를 씀
	 */
	@Override
	public void write(Word addr, Word value) {
		memorySpace.set(addr.intValue(), value);
	}

	/*
	 * addr에 해당되는 곳의 Word를 리턴함
	 */
	@Override
	public Word read(Word addr) {
		Word temp = memorySpace.get(addr.intValue());
		return new Word(temp.intValue());
	}

	@Override
	public void dump() {
		for(int i = 0 ; i<memorySpace.size() ; i++ ) {
			Word temp = memorySpace.get(i);
			int tempInt = temp.intValue();
			System.out.println("CODE : " + temp + " " + temp.hashCode());
			if( tempInt == 27 ) {
				break;
			}
		}
		
		for(int i = memorySpace.size() -1 ; i>=0 ; --i) {
			Word temp = memorySpace.get(i);
			System.out.println("STACK FROM BOTTOM : " + temp.intValue() + " " + temp.hashCode());
			if( temp.intValue() == 0 ) 
				break;
		}
	}
}
