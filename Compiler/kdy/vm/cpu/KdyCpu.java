package kdy.vm.cpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import kdy.vm.Source;
import kdy.vm.Word;
import kdy.vm.cpu.register.KdyRegister;
import kdy.vm.cpu.register.Register;
import kdy.vm.memory.Memory;

public class KdyCpu implements Cpu {
	
	//끝났는지 알려주는 플래그
	protected boolean eof;
	//제한 레지스터
	private final Register limitRegister;
	//스택 레지스터
	private final Register stackRegister;
	//프로그램 카운터 레지스터
	private final Register countRegister;
	//메모리 참조
	private Memory memory = null;
	//opcode테이블
	private final CycleAction[] actionTable;
	//파라미터 있는 opcode리스트
	//나중에 파라미터를 가져가야 하는지 판단하는데 쓰임
	private final List<Word> hasParameterList = new ArrayList<Word>();
	
	//입력 받기 위한 리더
	private BufferedReader reader = null;
	
	//추상 클래스, 파라미터를 가지고 혹은 
	//파라미터 없이 무언가 행동한다.
	protected abstract class CycleAction {
		abstract public void action(Word param);
	}
	
	public KdyCpu() {
		eof = false;
		//레지스터 생성
		limitRegister = new KdyRegister();
		stackRegister = new KdyRegister();
		countRegister = new KdyRegister();
		
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		//인덱스 0은 버림. 따라서 28개
		actionTable = new CycleAction[28];
		
		/*
		 * 파라미터 있는 opcode
		 * PUSHC, PUSH, POPC, 
		 * BRANCH, BREQ, BRLT, BRGT
		 */
		hasParameterList.add(new Word(13));
		hasParameterList.add(new Word(14));
		hasParameterList.add(new Word(15));
		hasParameterList.add(new Word(17));
		hasParameterList.add(new Word(19));
		hasParameterList.add(new Word(20));
		hasParameterList.add(new Word(21));
		
		/*
		 * ADD연산
		 * 파라미터 없음
		 */
		actionTable[1] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				memory.write(stackRegister.get(), val1.add(val2));
				stackRegister.dec();
			}
		};
		/*
		 * SUB연산
		 * 파라미터 없음
		 */
		actionTable[2] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());				
				memory.write(stackRegister.get(), val2.substract(val1));
				stackRegister.dec();
			}
		};
		/*
		 * MUL연산
		 * 파라미터 없음
		 */
		actionTable[3] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				memory.write(stackRegister.get(), val1.multiply(val2));
				stackRegister.dec();
			}
		};
		/*
		 * DIV연산
		 * 파라미터 없음
		 */
		actionTable[4] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				memory.write(stackRegister.get(), val2.divide(val1));
				stackRegister.dec();
			}
		};
		/*
		 * NEG연산
		 * 파라미터 없음
		 */
		actionTable[5] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				val1.negate();
				stackRegister.dec();
			}
		};
		/*
		 * CMPEQ연산
		 * 파라미터 없음
		 */
		actionTable[6] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				if( val1.compareTo(val2) == 0) {
					memory.write(stackRegister.get(), new Word(1));
				} else {
					memory.write(stackRegister.get(), new Word(0));
				}
				stackRegister.dec();
			}
		};
		/*
		 * CMPNE연산
		 * 파라미터 없음
		 */
		actionTable[7] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				if( val1.compareTo(val2) != 0 ) {
					memory.write(stackRegister.get(), new Word(1));
				} else {
					memory.write(stackRegister.get(), new Word(0));
				}
				stackRegister.dec();
			}
		};
		/*
		 * CMPGT연산
		 * 파라미터 없음
		 */
		actionTable[8] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				if( val1.compareTo(val2) > 0 ) {
					memory.write(stackRegister.get(), new Word(1));
				} else {
					memory.write(stackRegister.get(), new Word(0));
				}
				stackRegister.dec();
			}
		};
		/*
		 * CMPLT 연산
		 * 파라미터 없음
		 */
		actionTable[9] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				if( val1.compareTo(val2) < 0 ) {
					memory.write(stackRegister.get(), new Word(1));
				} else {
					memory.write(stackRegister.get(), new Word(0));
				}
				stackRegister.dec();
			}
		};
		/*
		 * CMPGE연산
		 * 파라미터 없음
		 */
		actionTable[10] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				if( val1.compareTo(val2) >= 0 ) {
					memory.write(stackRegister.get(), new Word(1));
				} else {
					memory.write(stackRegister.get(), new Word(0));
				}
				stackRegister.dec();
			}
		};
		/*
		 * CMPLE연산
		 * 파라미터 없음
		 */
		actionTable[11] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				stackRegister.inc();
				Word val2 = memory.read(stackRegister.get());
				if( val1.compareTo(val2) <= 0 ) {
					memory.write(stackRegister.get(), new Word(1));
				} else {
					memory.write(stackRegister.get(), new Word(0));
				}
				stackRegister.dec();
			}
		};
		/*
		 * CMPZR연산
		 * 파라미터 없음
		 */
		actionTable[12] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word val1 = memory.read(stackRegister.get());
				if( val1.compareTo(new Word(0)) == 0 ) {
					memory.write(stackRegister.get(), new Word(1));
				} else {
					memory.write(stackRegister.get(), new Word(0));
				}
				stackRegister.dec();
			}
		};
		/*
		 * PUSHC연산
		 * 파라미터 있음
		 */
		actionTable[13] = new CycleAction() {
			@Override
			public void action(Word value) {
				memory.write(stackRegister.get(), value);
				stackRegister.dec();
			}
		};
		/*
		 * PUSH연산
		 * 파라미터 있음
		 */
		actionTable[14] = new CycleAction() {
			@Override
			public void action(Word addr) {
				Word value = memory.read(addr);
				memory.write(stackRegister.get(), value);
				stackRegister.dec();
			}
		};
		/*
		 * POPC연산
		 * 파라미터 있음
		 */
		actionTable[15] = new CycleAction() {
			@Override
			public void action(Word addr) {
				stackRegister.inc();
				Word value = memory.read(stackRegister.get());
				memory.write(addr, value);
			}
		};
		/*
		 * POP연산
		 * 파라미터 없음
		 */
		actionTable[16] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word addr = memory.read(stackRegister.get());
				stackRegister.inc();
				Word value = memory.read(stackRegister.get());
				memory.write(addr, value);
			}
		};
		/*
		 * BRANCH연산
		 * 파라미터 있음
		 */
		actionTable[17] = new CycleAction() {
			@Override
			public void action(Word addr) {
				countRegister.set(addr);
			}
		};
		/*
		 * JUMP연산
		 * 파라미터 없음
		 */
		actionTable[18] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word addr = memory.read(stackRegister.get());
				countRegister.set(addr);
			}
		};
		/*
		 * BREQ연산
		 * 파라미터 있음
		 */
		actionTable[19] = new CycleAction() {
			@Override
			public void action(Word addr) {
				stackRegister.inc();
				Word flag = memory.read(stackRegister.get());
				if( flag.compareTo(new Word(0)) == 0 ) {
					countRegister.set(addr);
				}
			}
		};
		/*
		 * BRLT연산
		 * 파라미터 있음
		 */
		actionTable[20] = new CycleAction() {
			@Override
			public void action(Word addr) {
				stackRegister.inc();
				Word flag = memory.read(stackRegister.get());
				if( flag.compareTo(new Word(0)) < 0 ) {
					countRegister.set(addr);
				}
			}
		};
		/*
		 * BRGT연산
		 * 파라미터 있음
		 */
		actionTable[21] = new CycleAction() {
			@Override
			public void action(Word addr) {
				stackRegister.inc();
				Word flag = memory.read(stackRegister.get());
				if( flag.compareTo(new Word(0)) > 0 ) {
					countRegister.set(addr);
				}
			}
		};
		/*
		 * RDCHR연산
		 * 파라미터 없음
		 */
		actionTable[22] = new CycleAction() {
			@Override
			public void action(Word param) {
				int c;
				try {
					c = System.in.read();
					// \r\n 두개 떨쳐내 버리기
					System.in.read();
					System.in.read();
					
					memory.write(stackRegister.get(), new Word(c));
					stackRegister.dec();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		/*
		 * RDINT
		 * 파라미터 없음
		 */
		actionTable[23] = new CycleAction() {
			@Override
			public void action(Word param) {
				int c;
				try {
					c = Integer.parseInt(reader.readLine());					
					memory.write(stackRegister.get(), new Word(c));
					stackRegister.dec();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		/*
		 * WRCHR연산
		 * 파라미터 없음
		 */
		actionTable[24] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word chr = memory.read(stackRegister.get());
				System.out.println((char)chr.intValue());
			}
		};
		/*
		 * WRINT연산
		 * 파라미터 없음
		 */
		actionTable[25] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word chr = memory.read(stackRegister.get());
				System.out.println(chr.intValue());
			}
		};
		/*
		 * CONTS연산
		 * 파라미터 없음
		 */
		actionTable[26] = new CycleAction() {
			@Override
			public void action(Word param) {
				stackRegister.inc();
				Word addr = memory.read(stackRegister.get());
				Word value = memory.read(addr);
				memory.write(stackRegister.get(), value);
				stackRegister.dec();
			}
		};
		/*
		 * HALT
		 */
		actionTable[27] = new CycleAction() {
			@Override
			public void action(Word param) {
				eof = true;
			}
		};
	}
	
	@Override
	public void connectMemory(Memory memory) {
		//메모리를 참조함
		this.memory = memory;
	}

	@Override
	public void setSource(Source source) {
		//레지스터 값 설정
		//제한 레지스터는 opcode시작점 바로 전을 가르킨다.
		limitRegister.set(new Word(source.getOpcodeInitPoint()-1));
		//프로그램 카운터는 opcode시작점을 가르킴
		countRegister.set(new Word(source.getOpcodeInitPoint()));
		//스택은 메모리 최 하단 부분을 가르킨다.
		//그래서 힙은 윗부분, 스택은 아래부분에서 서로 자란다.
		stackRegister.set(new Word(Word.UNSIGNED_MAX_VALUE));
	}

	@Override
	public Word[] fetch() {
		//명령어 가져오고 증가시킴
		Word code = memory.read(countRegister.get());
		//프로그램 카운터 증가
		countRegister.inc();
		Word[] codes = new Word[2];
		
		//파라미터 있으면 한번 더 가져오고
		if( this.hasParameterList.indexOf(code) > -1 ) {
			codes[0] = code;
			
			code = memory.read(countRegister.get());
			countRegister.inc();
			codes[1] = code;
		} 
		//아니면 말고
		else {
			codes[0] = code;
			codes[1] = null;
		}
		return codes;
	}

	@Override
	public void execute(Word op, Word param) {
		//단순히 opcode테이블을 뒤져 실행 시킴
		//param이 null이면 파라미터를 필요치 않은 행동이니까
		//param이 null이여도 신경쓸 필요가 없음.
		
		this.actionTable[op.intValue()].action(param);
		//memory.dump();
	}

	@Override
	public boolean EOF() {
		//IO클로즈
		if( eof == true ) {
			try {
				reader.close();
			} catch (IOException ignore) {
				ignore.printStackTrace();
			}
		}
		//끝 플래그 변수 값 반환
		return eof;
	}
}
