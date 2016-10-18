package kdy.assembly.core;

import java.io.IOException;

public class StringSourceReader implements SourceReader {

	@Override
	public AssembleSource read(String content) throws IOException {
		//글 넘김자로 쪼개어 소스에 추가함
		String[] linebyline = content.split("\n");
		AssembleSource source = new AssembleSource();
		
		for(String line : linebyline ) {
			source.addLine(line);
		}
		
		return source;
	}

}
