package kdy.compiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kdy.assembly.core.Assembler;
import kdy.assembly.core.ObjectFile;
import kdy.assembly.core.AssembleSource;
import kdy.assembly.core.SourceReader;
import kdy.assembly.core.StringSourceReader;
import kdy.compiler.lexer.ImplementLexicalAnalyzer;
import kdy.compiler.lexer.LexicalAnalyzer;
import kdy.compiler.lexer.TestLexicalAnalyzer;
import kdy.vm.KdyVM;
import kdy.vm.OpcodeFragment;
import kdy.vm.Source;
import kdy.vm.VirtualMachine;
import kdy.vm.Word;
import kdy.vm.loader.Loader;

public class CompilerMain {    
	public static void main(String ... args) throws IOException {
		File curr = 
				new File(CompilerMain.class.getProtectionDomain().getCodeSource().getLocation().getFile() + 
						CompilerMain.class.getPackage().getName().replace(".", File.separator) + File.separator + "test.txt");
		
		PrintStream save = System.out;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream localPrint = new PrintStream(baos);
		/**테스트**/
		System.setOut(localPrint);
		/**테스트**/		
		
		//파스 함
		Parser parser = new Parser(new ImplementLexicalAnalyzer(curr.toString()));
		//Parser parser = new Parser(new TestLexicalAnalyzer());
		
		parser.parse();
		
		System.setOut(save);
	
		
		//파스한 결과를 읽어 어셈블할 소스로 만듬
		SourceReader reader = new StringSourceReader();
		AssembleSource source = reader.read(baos.toString());
		
		//어셈블러 생성
		Assembler assembler = new Assembler();
		//목적파일 생성
		final ObjectFile of = assembler.assemble(source);
		
		/**테스트**/
		//System.out.println(of.saveAsString(null));
		/**테스트**/
		
		//가상 머신 생성
		VirtualMachine vm = 
				new KdyVM();
				
		//로더 생성
		Loader loader = new Loader() {
			@Override
			public Source getSource() {
				String s = null;
				try {
					s = of.saveAsString(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
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
