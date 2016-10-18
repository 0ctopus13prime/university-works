package kdy.assembly.core;

import java.io.IOException;

public interface SourceReader {
	public AssembleSource read(String path) throws IOException;
}
