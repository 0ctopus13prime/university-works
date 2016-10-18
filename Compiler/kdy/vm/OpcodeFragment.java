package kdy.vm;

import java.util.List;

/*
 * T ~~ 
 * 에 해당되는 부분
 * opcode시작 지점과 사이즈, 
 * 그리고 opcode들이 있음.
 */
public class OpcodeFragment {
	//opcode시작지점
	private int opcodeInitPoint;
	//opcode사이즈
	private int opcodeSize;
	//opcode들
	private List<Word> opcodeList;
	
	public int getOpcodeInitPoint() {
		return opcodeInitPoint;
	}
	public void setOpcodeInitPoint(int opcodeInitPoint) {
		this.opcodeInitPoint = opcodeInitPoint;
	}
	public int getOpcodeSize() {
		return opcodeSize;
	}
	public void setOpcodeSize(int opcodeSize) {
		this.opcodeSize = opcodeSize;
	}
	public List<Word> getOpcodeList() {
		return opcodeList;
	}
	public void setOpcodeList(List<Word> opcodeList) {
		this.opcodeList = opcodeList;
	}	
}
