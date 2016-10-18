package kdy.assembly.core;

import java.util.ArrayList;
import java.util.List;

/*
 * 어셈블을 하기 위한 토크나이저.
 * 공백 및 단어로 소스 라인을 잘라 토큰으로 만든다.
 * 즉, loop: -> [loop, :]로 생성한다.
 */
public class AssemTokenizer {
	//토큰저장소
	private List<String> tokenList = new ArrayList<String>();
	//다음으로 전달해야 할 토큰의 위치
	private int idx = 0;
	
	/*
	 * 소스를 받아 
	 * 소스를 모두 토큰으로 쪼개어 토큰리스트에 저장함
	 */
	public void setSource(AssembleSource source) {
		String line = null;
		
		//라인을 단어로 잘라 토크나이징 한다.
		while( (line = source.nextLine() ) != null ) {
			int idx = line.indexOf(';');
			//주석 제거
			if( idx >= 0 ) {
				line = line.substring(0, idx);
			}
			//공백으로 자름
			String[] tokenArr = line.split("\\s");
			for(String token : tokenArr ) {
				token = token.trim();
				if( token.length() > 0 )
					tokenList.add(token.trim());
			}
		}
	}
	
	/*
	 * 토큰리스트에서 하나씩 뽑아 쓴다.
	 */
	public String nextToken() {
		if( idx < tokenList.size() ) {
			String token = tokenList.get(idx++);
			//System.out.println("token : " + token);
			return token;
		}
		return null;
	}
}
