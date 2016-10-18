package kdy.assembly.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/*
 * 어셈블의 결과물로써 
 * 그 결과물을 목적파일 형태로 바꾸어 파일로 저장하는
 * 역할을 맡은 클래스
 */
public class ObjectFile {
	//저장해야할 경로
	private String path;
	//최종 코드로 변환된 셀(cell)들.
	//이후 H : .. , T : .., E : .. 형태로 변환할 것이다.
	private List<String> results = new ArrayList<String>();
	
	public ObjectFile(String path) {
		this.path = path;
	}
	
	public void append(String code) {
		results.add(code);
	}
	
	//name으로 스트링으로 반환
	public String saveAsString(String programName) throws IOException {
		StringWriter writer = new StringWriter();
		printHeaderRecord(writer, decideName(programName));
		writeCode(writer);
		
		return writer.toString();
	}
	
	//name의 파일로 저장
	//혹은 name이 null이면 output.txt로 저장
	public void saveAsFile(String name) throws IOException {
		name = decideName(name);
		BufferedWriter writer = 
				new BufferedWriter(new FileWriter(new File(path + File.separator + 
						name + ".txt")));
		//H : 프로그램 이름 출력
		printHeaderRecord(writer, name);
		writeCode(writer);
	}
	
	private void writeCode(Writer writer) throws IOException {
		//T 코드 출력
		printTextRecord(writer);
		//E opcode시작 점 출력
		printEndRecord(writer, 0);
		
		writer.close();
	}
	
	/*
	 * 현재까지 최종 코드로 변환된 사이즈
	 * 혹은 지금 코드가 저장되어야 할 인덱스로도 해석될 수 있다.
	 */
	public int size() {
		return results.size();
	}
	
	/*
	 * 위치가 idx인 곳에 code를 쓴다.
	 * 추후조정때 NIL을 위치로 변환할 때 용이하다.
	 */
	public void write(int idx, String code) {
		results.set(idx, code);
	}
	
	/*
	 * 프로그램 헤더 레코드를 쓴다.
	 */
	protected void printHeaderRecord(Writer writer, String name) throws IOException {
		writer.write("H " + name + " ");
	}
	
	/*
	 * name이 null이면 output.txt로 쓴다.
	 */
	protected String decideName(String name) {
		return ( name == null ? "output" : name );
	}
	
	/*
	 * T : ... 형식으로 텍스트 레코드를 담당
	 */
	protected void printTextRecord(Writer writer) throws IOException {
		writer.write("T 0 " + results.size());
		
		for(int i = 0 ; i<results.size() ; i++ ) {
			writer.write(" " + results.get(i));
		}
	}
	
	/*
	 * 엔드 레코드를 담당한다.
	 */
	protected void printEndRecord(Writer writer, int startPos) throws IOException {
		writer.write(" E " + startPos);
	}
}
