package kdy.vm;

import java.util.List;

/*
 * 전체 소스에 해당됨.
 * 프로그램 이름, 처음 opcode가 실행되는 지점,
 * 코드 단편들이 들어 있음.
 */
public class Source {
	private String programName;
	//op코드의 처음 실행 포인트
	private int opcodeInitPoint;
	//전체 코드 처음 실행 포인트
	private int codeInitPoint;
	//코드 단편 리스트
	private List<OpcodeFragment> opcodeList;
	
	
	public List<OpcodeFragment> getOpcodeList() {
		return opcodeList;
	}
	public void setOpcodeList(List<OpcodeFragment> opcodeList) {
		this.opcodeList = opcodeList;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public int getOpcodeInitPoint() {
		return opcodeInitPoint;
	}
	public void setOpcodeInitPoint(int opcodeInitPoint) {
		this.opcodeInitPoint = opcodeInitPoint;
	}
	public int getCodeInitPoint() {
		return codeInitPoint;
	}
	public void setCodeInitPoint(int codeInitPoint) {
		this.codeInitPoint = codeInitPoint;
	}
}
