package kdy.assembly.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
 * 원본 소스 파일을 읽어
 * 어셈블하기 쉬운 형태인 Source를 리턴함.
 */
public class FileSourceReader implements SourceReader {
	/*
	 * path에 있는 원본 소스를 읽어
	 * Source로 리턴함.
	 */
	@Override
	public AssembleSource read(String path) throws IOException {
		AssembleSource source = new AssembleSource(Paths.get(path).getParent().toString());
		
		BufferedReader reader = 
				new BufferedReader(new FileReader(path));
		
		String line = null;
		while( (line = reader.readLine()) != null ) {
			source.addLine(line);
		}
		
		reader.close();
		
		return source;
	}
}
