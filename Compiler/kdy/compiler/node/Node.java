package kdy.compiler.node;

abstract public class Node {
	abstract public void genCode();
	private static int label = 0;
		
	protected String getLabel() {
		return "L" + label++;
	}
	
	protected void genLabel(String label) {
		formatPrint(label, null, null);
	}
	
	protected void formatPrint(String label, String opcode, String operand) {
		label = label == null ? "" : label + ":";
		opcode = opcode == null ? "" : opcode;
		operand = operand == null ? "" : operand;
		System.out.format("%-8s %-8s %-8s\n", label, opcode, operand);
	}
}
