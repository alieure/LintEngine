
import java.util.ArrayList;

public class Variable {
	protected String variableType; // i.e. reg, wire, parameter, localparam
	protected String variableAttribute; // i.e. Operational modes: input,
										// output,
	protected String variableSign; // i.e. Signed, Unsigned, or Unspecified
	protected String vectorParentName;
	protected String name;
	protected ArrayList<String> EdgeSens;
	protected String edgeSensitivity;
	protected int LineNumber;
	protected int arraySize;
	protected int vectorSize;

	// Default Constructor
	Variable() {
		name = "";
		variableType = "";
		variableAttribute = "";
		EdgeSens = new ArrayList();
		//vectorParentName = "";
		variableSign = "";
		arraySize = 0;
		vectorSize = 0;
		LineNumber = Parser.currentLineNumber;
	}

	// This constructor will most likely be used in conjuntion with the addEdge
	// function because the variable variableType may edge may not be registered
	// at first
	Variable(String name_in, String type_in) {
		name = name_in;
		variableType = type_in;
		variableAttribute = "";
		vectorParentName = "";
		variableSign = "";
		arraySize = 0;
		vectorSize = 0;
		LineNumber = Parser.currentLineNumber;
	}

	Variable(String name_in, String type_in, String attributeIn) {
		name = name_in;
		variableType = type_in;
		variableAttribute = attributeIn;
		vectorParentName = "";
		variableSign = "";
		arraySize = 0;
		vectorSize = 0;
		LineNumber = Parser.currentLineNumber;
	}

	Variable(String name_in, String type_in, String attributeIn, String sign_in, int arraySizeIn, int vectorSizeIn) {
		name = name_in;
		variableType = type_in;
		variableAttribute = attributeIn;
		vectorParentName = "";
		variableSign = sign_in;
		arraySize = arraySizeIn;
		vectorSize = vectorSizeIn;
		LineNumber = Parser.currentLineNumber;
	}

	// This constructor can be used to allow all three data slots to be filled.
	// Recommended defualt use of
	// Variable(String name_in, String type_in)
	// to keep duplicate edges from showing up.
	Variable(String name_in, String type_in, ArrayList<String> Edge) {
		name = name_in;
		variableType = type_in;
		EdgeSens = Edge;
		vectorParentName = "";
		variableSign = "";
		arraySize = 0;
		LineNumber = Parser.currentLineNumber;
	}

	// returns the Name of the Variable
	public String getName() {
		return name;
	}

	public String getVectorName() {
		return vectorParentName;
	}

	// returns the Type of variable
	public String getType() {
		return variableType;
	}

	public String getAttribute() {
		return variableAttribute;
	}

	// returns the Edge Sensitivity
	public ArrayList<String> getEdge() {
		return EdgeSens;
	}

	// Allows the name to be changed.
	public void setName(String Name_in) {
		name = Name_in;
	}

	// Allows the variableType to be changed
	public void setType(String Type_in) {
		variableType = Type_in;
	}

	// Allows a new list of edge sensitivity to be placed
	public void setEdge(ArrayList<String> Edge) {
		EdgeSens = Edge;
	}

	// For adding an edge sensitivity
	public boolean addEdge(String edge) {
		for (int i = 0; (i < EdgeSens.size()); i++) {
			// get integer value of relation between two strings
			int compare = EdgeSens.get(i).compareTo(edge);
			// Look for the edge sensitivity is already in the list
			if (compare == 0) {
				// return false for same
				return false;
			}
			// if it goes in the index of the current edge
			else if (compare > 0) {
				EdgeSens.add(i, edge);
				return true;
			} else {
				EdgeSens.add(edge);
				return true;
			}

		}
		// catch false
		return false;
	}

	// compares two variable objects to see if they are the same
	public boolean compareToAll(Variable V) {
		// see if the name matches
		if (name.equals(V.getName())) {
			// see if types are the same
			if (variableType.equals(V.getType())) {
				int i;
				// if the edge sensitivity arent the same Length
				if (EdgeSens.size() != V.getEdge().size()) {
					return false;
				}

				// checks each element of edge sensitivity
				for (i = 0; i < EdgeSens.size(); i++) {
					if (!(EdgeSens.get(i).equals(V.getEdge().get(i)))) {
						return false;
					}
				}
				// if they are the same
				if (i == EdgeSens.size()) {
					return true;
				}
			}
		}
		return false;
	}

	public int compareVariable(Variable b) {
		if (this.variableAttribute.equals(b.getAttribute())) {
			if (this.variableType.equals(b.getType())) {
				return this.name.compareTo(b.getName());
			} else {
				return this.variableType.compareTo(b.getType());
			}
		} else {
			return this.variableAttribute.compareTo(b.getAttribute());
		}
	}

	// compares two variable's name and variableType for a lower level compare
	public boolean compareTo(Variable V) {
		if (name.equals(V.getName())) {
			if (variableType.equals(V.getType())) {
				return true;
			}
		}
		return false;
	}

	public String getEdgeSensitivity() {
		return edgeSensitivity;
	}

	public boolean setEdgeSensitivity(String sensitive) {
		if (!edgeSensitivity.equals("")) {
			return false;
		} else if (sensitive.equals("posedge") || sensitive.equals("negedge")) {
			edgeSensitivity = sensitive;
			return true;
		}
		return false;
	}

	// the "params" array list is assumed to come from the module that is
	// currently being worked in


	
	public static ArrayList createVariablesFromVector(Variable template, String vecStart, String vecEnd) {
		ArrayList<Variable> vectorVars = new ArrayList();

		String temp; // parser "freshPiece" string value should currently be set
						// to a "["

		String number;
		int MSB = 0;
		int LSB = 0;
		number = Parser.parseNumberFromExpression(vecStart);
		if (Parser.isANumber(number) == 1) {
			MSB = Integer.parseInt(number);
		} else {
			MSB = 0;
		}
		number = Parser.parseNumberFromExpression(vecEnd);
		if (Parser.isANumber(number) == 1) {
			LSB = Integer.parseInt(number);
		} else {
			LSB = 0;
		}
		if (MSB < LSB) {
			int num = MSB;
			MSB = LSB;
			LSB = num;
		}

		for (int i = 0; MSB - i >= LSB; i++) {
			if (template.getClass() == Reg.class) {
				vectorVars.add(new Reg(template.name + "_" + (LSB + i), template.variableAttribute, template.name,
						template.variableSign, template.arraySize));
			} else if (template.getClass() == Wire.class) {
				vectorVars.add(new Wire(template.name + "_" + (LSB + i), template.variableAttribute, template.name,
						template.variableSign, template.arraySize));
			}
		}
		System.out.println(vectorVars.size());
		return vectorVars;
	}


	
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////

	
	public static int parseVariableVectorSize(String vecStart, String vecEnd) {
		String number;
		int MSB = 0;
		int LSB = 0;
		number = Parser.parseNumberFromExpression(vecStart);
		if (Parser.isANumber(number) == 1) {
			MSB = Integer.parseInt(number);
		} else {
			return -1;
		}
		number = Parser.parseNumberFromExpression(vecEnd);
		if (Parser.isANumber(number) == 1) {
			LSB = Integer.parseInt(number);
		} else {
			return -1;
		}
		
			if (MSB < LSB) {
				int num = MSB;
				MSB = LSB;
				LSB = num;
			}
		return MSB + 1 - LSB;
	} 
	
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	
	public static int parseVariableArraySize(String arrayStart, String arrayEnd) {
		String number;
		int MSB = 0;
		int LSB = 0;
		number = Parser.parseNumberFromExpression(arrayStart);
		if (Parser.isANumber(number) == 1) {
			MSB = Integer.parseInt(number);
		} else {
			return -1;
		}
		number = Parser.parseNumberFromExpression(arrayEnd);
		if (Parser.isANumber(number) == 1) {
			LSB = Integer.parseInt(number);
		} else {
			return -1;
		}
		
			if (MSB < LSB) {
				int num = MSB;
				MSB = LSB;
				LSB = num;
			}
		return MSB + 1 - LSB;
	}

	public static String safelyParseVariableFromLine(Parser parser, Block workingBlock, String varName,
			ArrayList<Variable> varListToAddTo, ArrayList<Variable> arrayIndexVars) {
		ArrayList<Variable> vars = new ArrayList();
		ArrayList<String> pieces = new ArrayList();
		int originalIndex = parser.getFreshPieceIndex();
		String temp = parser.getNextPiece();
		if (workingBlock.findVariableInParentBlockHierarchy(varName) != null) {
			vars.add(workingBlock.findVariableInParentBlockHierarchy(varName));
		} else if (!workingBlock.findVectorNameInParentBlockHierarchy(varName).isEmpty()) {
			vars.addAll(workingBlock.findVectorNameInParentBlockHierarchy(varName));
		} else {
			parser.addInstanceOfError8UndeclaredSignal(varName);
			parser.stopParsing();
		}
		boolean arrayIndexParsed = false;
		int startNum = -1, stopNum = -1, arrayIndex = -1;
		for (; temp.equals("[") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
			pieces.add(temp);
			if (temp.equals("$#")) {
				parser.updateLineNumber();
			}
			// if there is indexing present, and the signal is an array, handle
			// it differently than a vector
			else if (vars.get(0).arraySize > 1 && !arrayIndexParsed) {
				for (arrayIndexParsed = true, temp = parser.getNextPiece(); !temp.equals("]")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					if (temp.equals("$#")) {
						parser.updateLineNumber();
					} else {
						pieces.add(temp);
						if (workingBlock.findVariableInParentBlockHierarchy(temp) != null
								|| !workingBlock.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
							temp = Variable.safelyParseVariableFromLine(parser, workingBlock, temp, arrayIndexVars,
									arrayIndexVars);
						} else if (Module.checkVariableName(temp) && Parser.isANumber(temp) == 0) {
							parser.addInstanceOfError8UndeclaredSignal(temp);
							parser.stopParsing();
						}
					}
				}
				arrayIndexParsed = true;
			}
			// this else parses the vector variables
			else {
				String expression = "";
				for (temp = parser.getNextPiece(); (!temp.equals("]") && !temp.equals(":"))
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					if (temp.equals("$#")) {
						parser.updateLineNumber();
					} else {
						pieces.add(temp);
						if (Parser.isANumber(temp) == 1 || AssignmentStatement.expressionPiece(temp)) {
							expression += temp + " ";
						} else if (!temp.equals(":") && !temp.equals("]")) {
							String errorText;
							String errorNum;
							if (!Module.checkVariableName(temp)) {
								errorText = "Error: illegal token in vector bounds: (" + temp + ")";
								errorNum = "20b";
							} else {
								errorText = "Warning: variable (" + temp + ") in vector bounds:\n"
										+ "\tThis will very likely not produce the results you intend.";
								errorNum = "20c";
							}
							parser.addInstanceOfError20ParseError( errorText);
							parser.stopParsing();
						}
					}
				}
				startNum = Integer.parseInt(Parser.parseNumberFromExpression(expression));
				expression = "";
				if (temp.equals(":")) {
					for (temp = parser.getNextPiece(); !temp.equals("]")
							&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						if (temp.equals("$#")) {
							parser.updateLineNumber();
						} else {
							pieces.add(temp);
							if (Parser.isANumber(temp) == 1 || AssignmentStatement.expressionPiece(temp)) {
								expression += temp + " ";
							} else if (!temp.equals("]")) {
								String errorText;
								String errorNum;
								if (!Module.checkVariableName(temp)) {
									errorText = "Error: illegal token in vector bounds: (" + temp + ")";
									errorNum = "20b";
								} else {
									errorText = "Warning: variable (" + temp + ") in vector bounds:\n"
											+ "\tThis will very likely not produce the results you intend.";
									errorNum = "20c";
								}
								parser.addInstanceOfError20ParseError( errorText);
								parser.stopParsing();
							}
						}
					}
					stopNum = Integer.parseInt(Parser.parseNumberFromExpression(expression));
				}
			}
			pieces.add(temp);
		}

		if (startNum < stopNum) {
			int tempNum = startNum;
			startNum = stopNum;
			stopNum = tempNum;
		}
		if (startNum != -1) {
			Variable tempVar;
			if (stopNum == -1) {
				stopNum = startNum;
			}
			for (int i = stopNum; i < startNum + 1; i++) {
				tempVar = workingBlock.findVariableInParentBlockHierarchy(varName + "_" + i);
				if (tempVar != null) {
					varListToAddTo.add(tempVar);
				} else {
					String errorText = "Error: Parser error, index of (" + varName + ") might be out of bounds\n";
					parser.addInstanceOfError20ParseError( errorText);
					parser.stopParsing();
				}
			}
		} else if (workingBlock.findVariableInParentBlockHierarchy(varName) == null
				&& !workingBlock.findVectorNameInParentBlockHierarchy(varName).isEmpty()) {
			varListToAddTo.addAll(workingBlock.findVectorNameInParentBlockHierarchy(varName));
		} else if (workingBlock.findVariableInParentBlockHierarchy(varName) != null
				&& workingBlock.findVectorNameInParentBlockHierarchy(varName).isEmpty()) {
			varListToAddTo.add(workingBlock.findVariableInParentBlockHierarchy(varName));
		} else {
			parser.addInstanceOfError20ParseError(
					"Error: unknown parser error\n\tMake sure your design compiles in modelsim.");
			parser.stopParsing();
		}

		parser.setFreshPieceIndex(parser.getFreshPieceIndex() - 1);
		return temp;
	}

	public static String safelyParseVariableForAssignmentStatement(Parser parser, Block workingBlock,
			AssignmentStatement statement, String varName, ArrayList<Variable> varListToAddTo,
			ArrayList<Variable> arrayIndexVars) {
		ArrayList<Variable> vars = new ArrayList();
		ArrayList<String> pieces = new ArrayList();
		String temp = statement.getNextPiece();
		if (workingBlock.findVariableInParentBlockHierarchy(varName) != null) {
			vars.add(workingBlock.findVariableInParentBlockHierarchy(varName));
		} else if (!workingBlock.findVectorNameInParentBlockHierarchy(varName).isEmpty()) {
			vars.addAll(workingBlock.findVectorNameInParentBlockHierarchy(varName));
		} else {
			parser.addInstanceOfError8UndeclaredSignal(varName);
			parser.stopParsing();
		}
		boolean arrayIndexParsed = false;
		int startNum = -1, stopNum = -1, arrayIndex = -1;
		for (; (temp.equals("[") || temp.equals("$#"))
				&& !temp.equals("##END_OF_STATEMENT"); temp = statement.getNextPiece()) {
			pieces.add(temp);
			if (temp.equals("$#")) {
				temp = statement.getNextPiece();
				parser.setLineNumber(Integer.parseInt(temp));
			}
			// if there is indexing present, and the signal is an array, handle
			// it differently than a vector
			else if (vars.get(0).arraySize > 1 && !arrayIndexParsed) {
				for (arrayIndexParsed = true, temp = statement.getNextPiece(); !temp.equals("]")
						&& !temp.equals("##END_OF_STATEMENT");) {
					if (temp.equals("$#")) {
						temp = statement.getNextPiece();
						parser.setLineNumber(Integer.parseInt(temp));
					} else {
						pieces.add(temp);
						if (workingBlock.findVariableInParentBlockHierarchy(temp) != null
								|| !workingBlock.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
							temp = Variable.safelyParseVariableForAssignmentStatement(parser, workingBlock, statement,
									temp, arrayIndexVars, arrayIndexVars);
							// temp = statement.getNextPiece();
						} else if (Module.checkVariableName(temp) && Parser.isANumber(temp) == 0) {
							parser.addInstanceOfError8UndeclaredSignal(temp);
							parser.stopParsing();
						}
					}
					if (!temp.equals("$#") && !temp.equals("]")) {
						temp = statement.getNextPiece();
					}
				}
				arrayIndexParsed = true;
			}
			// this else parses the vector variables
			else {
				String expression = "";
				for (temp = statement.getNextPiece(); (!temp.equals("]") && !temp.equals(":"))
						&& !temp.equals("##END_OF_STATEMENT"); temp = statement.getNextPiece()) {
					if (temp.equals("$#")) {
						temp = statement.getNextPiece();
						parser.setLineNumber(Integer.parseInt(temp));
					} else {
						pieces.add(temp);
						if (Parser.isANumber(temp) == 1 || AssignmentStatement.expressionPiece(temp)) {
							expression += temp + " ";
						} else if (!temp.equals(":") && !temp.equals("]")) {
							String errorText;
							String errorNum;
							if (!Module.checkVariableName(temp)) {
								errorText = "Error: illegal token in vector bounds: (" + temp + ")";
								errorNum = "20b";
							} else {
								errorText = "Warning: variable (" + temp + ") in vector bounds:\n"
										+ "\tThis will very likely not produce the results you intend.";
								errorNum = "20c";
							}
							parser.addInstanceOfError20ParseError( errorText);
							parser.stopParsing();
						}
					}
				}
				startNum = Integer.parseInt(Parser.parseNumberFromExpression(expression));
				expression = "";
				if (temp.equals(":")) {
					for (temp = statement.getNextPiece(); !temp.equals("]")
							&& !temp.equals("##END_OF_STATEMENT"); temp = statement.getNextPiece()) {
						if (temp.equals("$#")) {
							temp = statement.getNextPiece();
							parser.setLineNumber(Integer.parseInt(temp));
						} else {
							pieces.add(temp);
							if (Parser.isANumber(temp) == 1 || AssignmentStatement.expressionPiece(temp)) {
								expression += temp + " ";
							} else if (!temp.equals("]")) {
								String errorText;
								String errorNum;
								if (!Module.checkVariableName(temp)) {
									errorText = "Error: illegal token in vector bounds: (" + temp + ")";
									errorNum = "20b";
								} else {
									errorText = "Warning: variable (" + temp + ") in vector bounds:\n"
											+ "\tThis will very likely not produce the results you intend.";
									errorNum = "20c";
								}
								parser.addInstanceOfError20ParseError( errorText);
								parser.stopParsing();
							}
						}
					}
					stopNum = Integer.parseInt(Parser.parseNumberFromExpression(expression));
				}
			}
			pieces.add(temp);
		}

		if (startNum < stopNum) {
			int tempNum = startNum;
			startNum = stopNum;
			stopNum = tempNum;
		}
		if (startNum != -1) {
			Variable tempVar;
			if (stopNum == -1) {
				stopNum = startNum;
			}
			for (int i = stopNum; i < startNum + 1; i++) {
				tempVar = workingBlock.findVariableInParentBlockHierarchy(varName + "_" + i);
				if (tempVar != null) {
					varListToAddTo.add(tempVar);
				} else {
					String errorText = "Error: Parser error, index of (" + varName + ") might be out of bounds\n";
					parser.addInstanceOfError20ParseError( errorText);
					parser.stopParsing();
				}
			}
		} else if (workingBlock.findVariableInParentBlockHierarchy(varName) == null
				&& !workingBlock.findVectorNameInParentBlockHierarchy(varName).isEmpty()) {
			varListToAddTo.addAll(workingBlock.findVectorNameInParentBlockHierarchy(varName));
		} else if (workingBlock.findVariableInParentBlockHierarchy(varName) != null
				&& workingBlock.findVectorNameInParentBlockHierarchy(varName).isEmpty()) {
			varListToAddTo.add(workingBlock.findVariableInParentBlockHierarchy(varName));
		} else {
			parser.addInstanceOfError20ParseError(
					"Error: unknown parser error\n\tMake sure your design compiles in modelsim.");
			parser.stopParsing();
		}

		return temp;
	}

	// Convert all data in the object to be output.
	
	
	@Override
	public String toString() {
		// //System.out.println("Name: "+name + " \t\tType:
		// "+variableType+"\nEdges");
		// for(int i = 0; i <EdgeSens.size();i++)
		// {
		// //System.out.println("\t"+EdgeSens.get(i));
		// }
		
		String retVariables = variableAttribute + " " + variableType + " " + name + " Array Size: " + arraySize+ " Vector Size: " + vectorSize + " LINE: "
				+ LineNumber ;
		
		return retVariables;
	} 
}
