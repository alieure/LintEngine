public class Reg extends Variable {
	// private String edgeSensitivity;
	Reg(String name_in) {
		name = name_in;
		variableType = "reg";
		variableAttribute = "";
		edgeSensitivity = "";
		vectorParentName = "";
		LineNumber = Parser.currentLineNumber;
		variableSign = "";
		arraySize = 0;
	}
	
	
	Reg(String name_in, String attributeIn) {
		name = name_in;
		variableType = "reg";
		variableAttribute = attributeIn;
		edgeSensitivity = "";
		vectorParentName = "";
		LineNumber = Parser.currentLineNumber;
		variableSign = "";
		arraySize = 0;
	}

	Reg(String name_in, String attributeIn, String vectorParent) {
		name = name_in;
		variableType = "reg";
		variableAttribute = attributeIn;
		edgeSensitivity = "";
		vectorParentName = vectorParent;
		LineNumber = Parser.currentLineNumber;
		variableSign = "";
		arraySize = 0;
	}

	Reg(String name_in, String attributeIn, String vectorParent, String signIn, int arraySizeIn) {
		name = name_in;
		variableType = "reg";
		variableAttribute = attributeIn;
		edgeSensitivity = "";
		vectorParentName = vectorParent;
		LineNumber = Parser.currentLineNumber;
		variableSign = signIn;
		arraySize = arraySizeIn;
	}

	Reg(String name_in, String attributeIn, String signIn, int arraySizeIn) {
		name = name_in;
		variableType = "reg";
		variableAttribute = attributeIn;
		edgeSensitivity = "";
		vectorParentName = "";
		LineNumber = Parser.currentLineNumber;
		variableSign = signIn;
		arraySize = arraySizeIn;
	}

	
	@Override
	public String toString() {
		return variableAttribute + " " + variableType + " " + variableSign + " " + name + "; Array Size: " + arraySize
				+ " LINE: " + LineNumber;
	}
}
