package kdy.vm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kdy.vm.loader.Loader;
import kdy.vm.memory.KdyMemory;
import kdy.vm.memory.Memory;

import org.junit.Test;

public class TestClass {
	
	
	@Test
	public void regTest() throws IOException { 
		String s = "H TEST T 0 3 0 1 2 3 T 4 7 4 5 6 7 T 4 5 6 7 8 13 42 55 66 E 0";
		
		Pattern pattern = Pattern.compile("^\\s*H (?<name>\\w+) (.*) E (?<opstart>\\d+)\\s*$");
		//Pattern pattern = Pattern.compile("(?:T(?<opcode>(?:\\s+\\d+)+)+)+");
		Matcher matcher = pattern.matcher(s);
		
		String programName = null;
		int opcodeInitPoint = 0;
		
		if( matcher.find() ) {
			programName = matcher.group("name").trim();
			opcodeInitPoint = Integer.parseInt(matcher.group("opstart").trim());
		}
		System.out.println(programName);
		System.out.println(opcodeInitPoint);
		
		pattern = Pattern.compile("(?:T(?<opcode>(?:\\s+\\d+)+)+)+");
		matcher = pattern.matcher(s);
		
		List<String> rawOpcodeList = new LinkedList<String>();
		while(matcher.find()) {
			rawOpcodeList.add(matcher.group("opcode").trim());
		}
		
		System.out.println(rawOpcodeList);
		
		
		List<OpcodeFragment> opcodeFragments = new ArrayList<OpcodeFragment>(rawOpcodeList.size());
		for(String rawOpcode : rawOpcodeList) {
			OpcodeFragment of = new OpcodeFragment();
			
			String[] opcodes = rawOpcode.split(" ");
			of.setOpcodeInitPoint(Integer.parseInt(opcodes[0]));
			of.setOpcodeSize(Integer.parseInt(opcodes[1]));
			
			List<Word> wordOpcodes = new ArrayList<Word>(opcodes.length);
			for(int i = 2 ; i<opcodes.length ; i++) {
				String raw = opcodes[i];
				wordOpcodes.add(new Word(Integer.parseInt(raw) ) );
			}
			of.setOpcodeList(wordOpcodes);
			System.out.println(wordOpcodes);
		}
	}
}
