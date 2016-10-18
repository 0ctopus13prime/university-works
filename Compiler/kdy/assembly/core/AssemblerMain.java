package kdy.assembly.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/*
 * 김두용
 * 2015 05 25
 * 12090850
 * ipxdscd@naver.com
 */
public class AssemblerMain {
	public static void main(String[] args) throws URISyntaxException, IOException {
		//소스 위치. 소스 위치는 패키지 euclid_source.txt를 가르킨다.
		String sourceLocation = 
				AssemblerMain.class.getProtectionDomain().getCodeSource().getLocation().getFile().toString().substring(1).replace("/", File.separator) +
				AssemblerMain.class.getPackage().getName().replace(".", File.separator) + File.separator +  "myTest.txt";
		
		//소스 리더 생성
		SourceReader reader = new FileSourceReader();
		//리더에게 소스파일 알려주고 소스로 변환
		AssembleSource source = reader.read(sourceLocation);
		
		//어셈블러 생성
		Assembler assembler = new Assembler();
		//목적파일 생성
		ObjectFile of = assembler.assemble(source);
		
		//목적파일을 소스 파일이 있는 곳에 지정된 이름으로 파일로 생성
		//null로 주면 output.txt로 생성
		of.saveAsFile(null);
	}
}
