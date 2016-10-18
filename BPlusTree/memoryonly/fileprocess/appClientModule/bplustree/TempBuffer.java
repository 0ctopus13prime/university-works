package bplustree;

import java.util.Map;

interface TempBuffer {
	static final String NEW_NODE = "NEWNODE";
	static final String ORIGINAL_NODE = "ORGNODE";
	static final String SPLIT_KEY = "SPLITKEY";
	
	public Map<String, Object> split(); 
}
