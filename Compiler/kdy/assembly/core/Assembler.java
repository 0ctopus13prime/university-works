package kdy.assembly.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * 어셈블 작업을 담당하는 클래스
 */
public class Assembler {
	//오피코드가 담겨 있는 테이블
	private Map<String, String> opcodeTable;
	//파라미터를 받는 오피코드 테이블
	private Set<String> hasParameterSet;
	//심볼 및 레이블의 위치를 담고 있는 테이블
	private Map<String, Integer> labelPositionTable;
	//정의가 되지 않은 심볼 및 레이블을 인수로 받은 위치가 담겨있는 테이블
	private Map<String, List<Integer>> undefinedLabelTable;
	//어셈블을 위한 토크나이저
	private AssemTokenizer at = null;
		
	public Assembler() {
		//파라미터 있는 opcode
		hasParameterSet = new HashSet<String>();
		hasParameterSet.add("PUSHC");
		hasParameterSet.add("PUSH");
		hasParameterSet.add("POPC");
		hasParameterSet.add("BRANCH");
		hasParameterSet.add("BREQ");
		hasParameterSet.add("BRLT");
		hasParameterSet.add("BRGT");
		
		//opcode를 삽입
		opcodeTable = new HashMap<String, String>();
		opcodeTable.put("ADD", "1");
		opcodeTable.put("SUB", "2");
		opcodeTable.put("MUL", "3");
		opcodeTable.put("DIV", "4");
		opcodeTable.put("NEG", "5");
		
		opcodeTable.put("CMPEQ", "6");
		opcodeTable.put("CMPNE", "7");
		opcodeTable.put("CMPGT", "8");
		opcodeTable.put("CMPLT", "9");
		opcodeTable.put("CMPGE", "10");
		opcodeTable.put("CMPLE", "11");
		opcodeTable.put("CMPZR", "12");
		
		opcodeTable.put("PUSHC", "13");
		opcodeTable.put("PUSH", "14");
		opcodeTable.put("POPC", "15");
		opcodeTable.put("POP", "16");
		
		opcodeTable.put("BRANCH", "17");
		opcodeTable.put("JUMP", "18");
		opcodeTable.put("BREQ", "19");
		opcodeTable.put("BRLT  ", "20");
		opcodeTable.put("BRGT", "21");
		
		opcodeTable.put("RDCHR", "22");
		opcodeTable.put("RDINT", "23");
		opcodeTable.put("WRCHR", "24");
		opcodeTable.put("WRINT", "25");
		
		opcodeTable.put("CONTS", "26");
		opcodeTable.put("HALT", "27");
		
		//어셈블 토크나이저 생성
		at = new AssemTokenizer();
	}
	
	/*
	 * 소스를 받아 목적파일을 생성한다.
	 */
	public ObjectFile assemble(AssembleSource source) {
		//라벨 위치 테이블
		labelPositionTable = new HashMap<String, Integer>();
		//추후조정을 위한 정의 되지 않은 라벨 위치 값
		undefinedLabelTable = new HashMap<String, List<Integer>>();
		//목적 파일
		ObjectFile of = new ObjectFile(source.getDirectory());
		
		//토크나이저에게 소스를 셋팅함
		at.setSource(source);
		String token = null;
		
		//토크나이저가 하나씩 토큰을 줌
		while( (token = at.nextToken() ) != null ) {
			//니모닉이면
			if( opcodeTable.containsKey(token) ) {
				of.append(opcodeTable.get(token));
				//파라미터를 취하는 니모닉이면
				if( hasParameterSet.contains(token) ) {
					handleParameter(at.nextToken(), of);
				}
			}
			//디렉티브면
			else if(token.startsWith(".")) {
				handleDirective(at.nextToken(), of);
			}
			//변수 혹은 라벨 
			else {
				handleLabel(token, of);
			}
		}
		
		//정의되지 않는 라벨이 있다면 에러
		if( !undefinedLabelTable.isEmpty() ) {
			System.out.println("정의되지 않은 라벨이 존재 합니다. ");
			Iterator<String> it = undefinedLabelTable.keySet().iterator();
			while(it.hasNext() ) {
				String tmp = it.next();
				System.out.println(tmp + " : " + undefinedLabelTable.get(tmp ) );
			}
			System.exit(0);
		}
		
		return of;
	}
	
	/*
	 * 라벨을 다룸. 
	 * 라벨을 받아서 라벨 테이블에 그 위치를 저장시킴
	 * 심볼(변수)인지 레이블인지에 따라 한번 더 토큰을 받을지를 결정
	 * 그리고 추후 조정을 통해 NIL로 쓰여져 있던 목적파일을 다 해당되는 위치값으로 변경
	 */
	private void handleLabel(String label, ObjectFile of) {
		//System.out.println(label + "을 위치 " + of.size() + "로 위치시킴");
		int labelPosition = of.size();
		
		//변수임
		if( !label.contains(":") ) {
			//System.out.println("handle symbol : " + label);
			//변수인지 라벨인지
			String next = at.nextToken();
			handleParameter(next, of);
		} else {
			//System.out.println("handle label : " + label);
			label = label.replace(":", "").trim();
		}
		
		labelPositionTable.put(label, labelPosition);
		
		//추후조정함
		//System.out.println("post control " + label);
		List<Integer> list = undefinedLabelTable.get(label);
		if( list != null && !list.isEmpty() ) {
			for(int pos : list ) {
				//System.out.println("change idx : " + pos + " to " + labelPosition);
				of.write(pos, "" + labelPosition);
			}
		}
		
		//조정이 끝났으면 삭제함
		undefinedLabelTable.remove(label);
	}

	/*
	 * 파라미터를 다룸
	 * 파라미터는 심볼, 레이블, 숫자, 캐릭터가 될 수 있으며
	 * 그에 따른 행동을 취함
	 * 특히, 심볼 또는 레이블을 파라미터로 받았는데
	 * 정의가 되어 있지 않다면 추후조정을 위한 
	 * undefinedLabelTable에 그 위치를 추가함
	 */
	private void handleParameter(String param, ObjectFile of) {
		//System.out.println("handle parameter : " + param);

		//니모닉은 파라미터가 될 수 없다
		if( this.opcodeTable.containsKey(param) ) {
			//System.out.println(param + " cannot be parameter. Rather it's a nemonic");
			return;
		}
		
		try {
			//숫자인지 판별
			Double.parseDouble(param);
			//System.out.println("handle parameter as number");
			of.append(param);
			return;
		} catch(NumberFormatException ignore) { }
		
		//문자인지 판별 예를 들면 'B, '?
		if( param.matches("'.") ) {
			//System.out.println("handle parameter as character " + (int)param.charAt(1));
			of.append("" + (int)param.charAt(1));
			return;
		}
		
		//System.out.println("handle parameter as label");
		//라벨의 정의가 있음
		if( labelPositionTable.containsKey(param) ) {
			//System.out.println("parameter is defined , " + param);
			//System.out.println("write defined label pos : " + labelPositionTable.get(param));
			of.append("" + labelPositionTable.get(param));
		} 
		//라벨의 정의가 없음
		else {
			//System.out.println("parameter is undefined , " + param);
			List<Integer> list = undefinedLabelTable.get(param);
			if( list == null ) {
				list = new ArrayList<Integer>();
				undefinedLabelTable.put(param, list);
			}
			//System.out.println("push undefined label : " + param + " pos : " + of.size() );
			list.add(of.size());
			
			of.append("NIL");
		}
	}
	
	/*
	 * 디렉티브를 다룸
	 * 현재는 .BLOCK밖에 없음
	 */
	private void handleDirective(String directive, ObjectFile of) {
		//System.out.println("handle directive : " + directive);
		if( directive.equals("BLOCK") ) {
			//얼마나 블록으로 잡을 것인지?
			String next = at.nextToken();
			try {
				int howmuch = Integer.parseInt(next);
				//예약한 만큼 0으로 채움
				for(int i = 0 ; i<howmuch ; i++ ) {
					of.append("0");
				}
			} catch(NumberFormatException e) {
				//디렉티브 인수가 잘못 됨
				System.out.println("디렉티브 인수가 잘못 되었습니다. .BLOCK뒤에는 숫자가 와야 합니다.");
				System.exit(0);
			}
		} 
		//잘못된 디렉티브 임
		else {
			System.out.println("디렉티브가 잘못 되었습니다. .BLOCK만 존재 합니다.");
			System.exit(0);
		}
	}
}
