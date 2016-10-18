package kdy.vm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kdy.vm.loader.Loader;

/*
 * 컴파일러론 화1,2 목3
 * 김두용 12090850 작성
 * 2015년 04월 09일 작성 및 제출
 */
public class Main {
	public static void main(String[] args) {
		//가상 머신 생성
		VirtualMachine vm = 
				new KdyVM();
		
		//로더 생성
		Loader loader = new Loader() {
			@Override
			public Source getSource() {
				String s = "H output T 0 86 17 6 0 0 0 0 13 63 24 23 15 3 13 63 24 23 15 2 14 2 14 3 9 19 39 14 3 15 4 14 2 15 3 14 4 15 2 17 39 14 3 14 2 14 3 14 2 4 3 2 15 5 13 0 14 5 8 19 82 14 2 15 3 14 5 15 2 14 3 14 2 14 3 14 2 4 3 2 15 5 17 52 14 2 25 27 E 0";
				
				Pattern pattern = Pattern.compile("^\\s*H (?<name>\\w+) (.*) E (?<opstart>\\d+)\\s*$");
				Matcher matcher = pattern.matcher(s);
				
				String programName = null;
				int opcodeInitPoint = 0;
				
				if( matcher.find() ) {
					/*
					 * 프로그램 이름과 opcode처음 실행 지점을 얻음
					 */
					programName = matcher.group("name").trim();
					opcodeInitPoint = Integer.parseInt(matcher.group("opstart").trim());
				}
							
				pattern = Pattern.compile("(?:T(?<opcode>(?:\\s+\\d+)+)+)+");
				matcher = pattern.matcher(s);

				/*
				 * opcode들을 얻음
				 * 
				 * T가 한번 이상 나올 수 있다.
				 * T ~~
				 * T ~~ ... 이런식으로 그래서 while로 돌면서 
				 * opcode를 수집함
				 */
				
				//raw opcode들을 수집함
				List<String> rawOpcodeList = new LinkedList<String>();
				while(matcher.find()) {
					rawOpcodeList.add(matcher.group("opcode").trim());
				}
								
				//raw opcode를 기반으로 OpcodeFragment객체 생성
				List<OpcodeFragment> opcodeFragments = new ArrayList<OpcodeFragment>(rawOpcodeList.size());
				for(String rawOpcode : rawOpcodeList) {
					OpcodeFragment of = new OpcodeFragment();
					
					String[] opcodes = rawOpcode.split(" ");
					//처음 두개는 opcode가 아니다. 
					//시작지점과 사이즈이다.
					of.setOpcodeInitPoint(Integer.parseInt(opcodes[0]));
					of.setOpcodeSize(Integer.parseInt(opcodes[1]));
					
					//이제 가상머신에서 쓰일 Word로 변환
					List<Word> wordOpcodes = new ArrayList<Word>(opcodes.length);
					for(int i = 2 ; i<opcodes.length ; i++) {
						String raw = opcodes[i];
						wordOpcodes.add(new Word(Integer.parseInt(raw) ) );
					}
					of.setOpcodeList(wordOpcodes);
					
					opcodeFragments.add(of);
				}
				
				//소스 처음 로드 부분 설정
				int minIdx = Integer.MAX_VALUE;
				for(OpcodeFragment of : opcodeFragments) {
					if( of.getOpcodeInitPoint() < minIdx ) {
						minIdx = of.getOpcodeInitPoint();
					}
				}
								
				//소스생성
				Source source = new Source();
				source.setCodeInitPoint(minIdx);
				source.setOpcodeInitPoint(opcodeInitPoint);
				source.setOpcodeList(opcodeFragments);
				source.setProgramName(programName);
												
				return source;
			}
		};
		
		
		//로더 장착
		vm.setLoader(loader);
		//실행
		vm.execute();
	}
}
