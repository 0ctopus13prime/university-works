package kdy.assembly.core;

import java.util.ArrayList;
import java.util.List;

/*
 * 소스파일에서 읽어 어셈블하기 용이하도록
 * 변환 시킨 소스형태.
 */
public class AssembleSource {
	private List<String> source = new ArrayList<String>();
	private int idx = 0;
	private String directory = null;
	
	public AssembleSource() { }
	
	public AssembleSource(String directory) {
		this.directory = directory;
	}
		
	public void addLine(String line) {
		source.add(line);
	}
	
	/*
	 * 소스의 라인 라인을 반환함
	 */
	public String nextLine() {
		if( idx < source.size() ) {
			return source.get(idx++);
		}
		
		return null;
	}
		
	public int size() {
		return source.size();
	}
	
	public String getDirectory() {
		return this.directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}	
}
