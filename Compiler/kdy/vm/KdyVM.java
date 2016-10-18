package kdy.vm;

import kdy.vm.cpu.Cpu;
import kdy.vm.cpu.KdyCpu;
import kdy.vm.loader.Loader;
import kdy.vm.memory.KdyMemory;
import kdy.vm.memory.Memory;

public class KdyVM implements VirtualMachine {

	//씨피유
	private Cpu cpu;
	//메모리
	private Memory memory;
	//로더, 로더는 외부에서 주입될 수도, 디폴트로 생성 할 수 있다.
	//어차피 인터페이스이므로 네트워크로 끌어오든, 파일 시스템에서 끌어오든, DB에서 긁어 오든 
	//구현상의 문제.
	private Loader loader;
	
	//디폴트 생성자
	public KdyVM() {
		cpu = new KdyCpu();
		memory = new KdyMemory();
		
		//cpu에게 메모리를 주어 메모리에 접근할 수 있도록 한다.
		cpu.connectMemory(memory);
	}
	
	//씨피유, 메모리, 로더 모두 외부 주입
	public KdyVM(Cpu cpu, Memory memory, Loader loader) {
		this.cpu = cpu;
		this.memory = memory;
		this.loader = loader;
		//cpu에게 메모리를 주어 메모리에 접근할 수 있도록 한다.
		cpu.connectMemory(memory);
	}
	
	//로더만 외부 주입
	public KdyVM(Loader loader ) {
		this();
		this.loader = loader;		
	}
	
	@Override
	public void setLoader(Loader loader) {
		this.loader = loader;
	}

	@Override
	public void execute() {
		if( loader == null ) 
			return;
		
		//소스 얻기
		Source source = loader.getSource();
		//cpu소스 세팅
		//그래서 레지스터를 셋팅 할 수 있도록 한다.
		cpu.setSource(source);
		//메모리에 소스 로드
		memory.loadSource(source);
		
		//명령어를 가져오면, [opcode, 파라미터]로 가져온다.
		//파라미터가 없는 명령어라면 [opcode, null]로 가져온다.
		Word[] temp = null;
		
		System.out.println(source.getProgramName() + " START ");
		
		//프로그램 끝 까지 실행함
		while(!cpu.EOF()) {
			//명령어 페치
			temp = cpu.fetch();
			//명령어 실행
			//첫번째 파라미터는 opcode,
			//두번째 파라미터는 명령어 실행 파라미터
			cpu.execute(temp[0], temp[1]);
		}
		
		System.out.println(source.getProgramName() + " END ");
	}
	
}
