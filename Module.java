/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;

/**
 *

 */
public class Module extends Block {

	protected ArrayList<Variable> preParameters;
	protected ArrayList<Parameter> parameters;
	protected ArrayList<Variable> portVariables;
	protected ArrayList<ModuleInstantiation> subModules;
	String statementText;

	Module() {
		BlockType = "";
		BlockName = "";
		statementText = "";
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		preParameters = new ArrayList();
		parameters = new ArrayList();
		portVariables = new ArrayList();
		subModules = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	Module(String type, Block comesFrom) {
		BlockType = type;
		BlockName = "";
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		parent = comesFrom;
		preParameters = new ArrayList();
		parameters = new ArrayList();
		portVariables = new ArrayList();
		subModules = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	Module(String type, Block comesFrom, String name) {
		BlockType = type;
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		parent = comesFrom;
		preParameters = new ArrayList();
		parameters = new ArrayList();
		portVariables = new ArrayList();
		subModules = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	void addVariableToModule(Variable newVar) {
		vars.add(newVar);
		// sortVariables();
	}

	void addVariableToModulePortList(Variable newVar) {
		portVariables.add(newVar);
		addVariableToModule(newVar);
	}

	void addModuleInstantiation(ModuleInstantiation subModule) {
		subModules.add(subModule);
	}

	public Variable findVariableInModule(String searchName) {
		for (int i = 0; i < vars.size(); i++) {
			if (vars.get(i).name.equals(searchName)) {
				if (vars.get(i).getClass() != TaskCall.class && vars.get(i).getClass() != FunctionCall.class) {
					return vars.get(i);
				}
			}
		}
		return null;
	}

	public void removeVariableFromModule(String removeName, ArrayList<Variable> rm) {
		for (int i = 0; i < rm.size(); i++) {
			if (rm.get(i).name.equals(removeName) || rm.get(i).vectorParentName.equals(removeName)) {
				rm.remove(i);
				i--;
			}
		}
	}

	public void updatePortVariableInModule(String varName, String newType, ArrayList<Variable> newVars) {
		Variable newVar;
		/*
		 * If a variable by that name exists in both the overall variable list
		 * "vars" and in the "portVariables" list, then remove it, and add the
		 * updated variable.
		 */
		if (findVariableInModule(varName) != null) {
			removeVariableFromModule(varName, vars);
			vars.addAll(newVars);
		}
		if (findVariableInModulePortList(varName) != null) {
			removeVariableFromModule(varName, portVariables);
			portVariables.addAll(newVars);
		}
	}

	public void sortModuleVariables() {
		int i = 0, j = 0;
		Variable temp;
		for (i = 0; i < vars.size(); i++) {
			for (j = 0; j < vars.size() - (i + 1); j++) {
				if (vars.get(j).compareVariable(vars.get(j + 1)) == 1) {
					temp = vars.get(j);
					vars.set(j, vars.get(j + 1));
					vars.set(j + 1, temp);
				}
			}
		}
	}

	public Variable findVariableInModulePortList(String searchName) {
		for (int i = 0; i < portVariables.size(); i++) {
			if (portVariables.get(i).name.equals(searchName)) {
				return portVariables.get(i);
			}
		}
		for (int i = 0; i < portVariables.size(); i++) {
			if (portVariables.get(i).vectorParentName.equals(searchName)) {
				return portVariables.get(i);
			}
		}
		return null;
	}

	public Parameter findParameterInModule(String searchName) {
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).name.equals(searchName)) {
				return parameters.get(i);
			}
		}
		return null;
	}

	public static boolean checkVariableName(String name) {
		char let;
		for (int i = 0; i < name.length(); i++) {
			let = name.charAt(i);
			if (let < 48 || (let > 57 && let < 65) || (let > 90 && let < 97 && let != 95) || let > 122) {
				return false;
			}
			if (i == 0) {
				if (!((let > 64 && let < 91) || (let > 96 && let < 123))) {
					return false;
				}
			}
		}
		return true;
	}

	protected void parseVariableInModuleHead(String temp, Parser parser) {
		while (!temp.equals(")") && !temp.equals("##END_OF_MODULE_CODE")) {
			ArrayList<String> varNames = new ArrayList();
			ArrayList<String> vecTypes = new ArrayList();
			ArrayList<String> vecAttributes = new ArrayList();
			String vecTypeTemp = "";
			String vecAttributeTemp = "";
			ArrayList<String> vecStart = new ArrayList();
			ArrayList<String> vecEnd = new ArrayList();
			String vecStartTemp = "";
			String vecEndTemp = "";
			String lastSign = "";
			for (int i = 0; (!(temp.equals("input") && vecAttributeTemp.equals("output"))
					&& !(temp.equals("output") && vecAttributeTemp.equals("input"))) && !temp.equals(")")
					&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				if (temp.equals("$#")) {
					parser.updateLineNumber();
				} else if (temp.equals("input") || temp.equals("output")) {
					vecStartTemp = "";
					vecEndTemp = "";
					vecAttributeTemp = temp;
				} else if (temp.equals("reg") || temp.equals("wire")) {
					vecTypeTemp = temp;
				} else if (temp.equals("signed") || temp.equals("unsigned")) {
					lastSign = temp;
				} else if (temp.equals(",")) {
					// vecStart.set(i,vecStartTemp);
					// vecEnd.set(i,vecEndTemp);
					vecStartTemp = "";
					vecEndTemp = "";
					// vecStart.add("");
					// vecEnd.add("");
					i++;
				} else if (temp.equals("[")) {
					for (temp = parser.getNextPiece(); !temp.equals(":")
							&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						vecStartTemp += temp + " ";
					}
					for (temp = parser.getNextPiece(); !temp.equals("]")
							&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						vecEndTemp += temp + " ";
					}

				} else {
					if (vecAttributeTemp.equals("") && i > 0) {
						vecAttributes.add(vecAttributes.get(i - 1));
					} else {
						vecAttributes.add(vecAttributeTemp);
					}
					if (vecStartTemp.equals("") && vecAttributeTemp.equals("") && i > 0) {
						vecStart.add(vecStart.get(i - 1));
						vecEnd.add(vecEnd.get(i - 1));
					} else {
						vecStart.add(vecStartTemp);
						vecEnd.add(vecEndTemp);
					}
					vecTypes.add(vecTypeTemp);
					if (!checkVariableName(temp) || parser.pieceIsKeyword(temp)) {
						parser.addInstanceOfError18UnexpectedToken(temp);
						parser.stopParsing();
					}
					varNames.add(temp);
					vecAttributeTemp = "";
					vecStartTemp = "";
					vecEndTemp = "";
				}
			}
			for (int i = 0; i < varNames.size() && !temp.equals("##END_OF_MODULE_CODE"); i++) {
				ArrayList<Variable> newVars = new ArrayList();
				// set up the template
				Variable template = findVariableInModulePortList(varNames.get(i));
				if (vecTypes.get(i).equals("reg")) {
					template = new Reg(varNames.get(i), vecAttributes.get(i), lastSign, 0);
				} else {
					template = new Wire(varNames.get(i), vecAttributes.get(i), lastSign, 0);
				}

				// parse the vector if necessary
				if (vecStart.get(i).equals("")) {
					newVars.add(template);
				} else {
					newVars.addAll(Variable.createVariablesFromVector(template, vecStart.get(i), vecEnd.get(i)));
				}
				vars.addAll(newVars);
				portVariables.addAll(newVars);
			}
		}
		temp = parser.getNextPiece(); // to remove the final ";"
	}

	protected void parseVariableInModule(String temp, Parser parser) {
		ArrayList<String> varNames = new ArrayList();
		String lastType = "";
		String lastAttribute = "";
		String vecStart = "";
		String vecEnd = "";
		String arrayStartTemp = "";
		String arrayEndTemp = "";
		ArrayList<Integer> arraySize = new ArrayList();
		arraySize.add(new Integer(0));
		ArrayList<Integer> vectorSize = new ArrayList();
		vectorSize.add(new Integer(0));
		String lastSign = "";
		boolean parVector = true;
		boolean singleArrayParsed = false;
		for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
			if (temp.equals("$#")) {
				parser.updateLineNumber();
			} else if (temp.equals("input") || temp.equals("output")) {
				lastAttribute = temp;
			} else if (temp.equals("reg") || temp.equals("wire")) {
				lastType = temp;
			} else if (temp.equals("signed") || temp.equals("unsigned")) {
				lastSign = temp;
			} else if (temp.equals(",")) {
				arraySize.add(new Integer(0));
				vectorSize.add(new Integer(0));
			} else if (temp.equals("[") && parVector) {
				for (temp = parser.getNextPiece(); !temp.equals(":")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					vecStart += temp + " ";
				}
				for (temp = parser.getNextPiece(); !temp.equals("]")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					vecEnd += temp + " ";
				}
				
			vectorSize.remove(vectorSize.size() - 1);
			vectorSize.add(Variable.parseVariableVectorSize(arrayStartTemp, arrayEndTemp));

			} else if (temp.equals("[") && !parVector && !singleArrayParsed) {
				for (temp = parser.getNextPiece(); !temp.equals(":")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					arrayStartTemp += temp + " ";
				}
				for (temp = parser.getNextPiece(); !temp.equals("]")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					arrayEndTemp += temp + " ";
				}
			arraySize.remove(arraySize.size() - 1);
			arraySize.add(Variable.parseVariableArraySize(arrayStartTemp, arrayEndTemp));
			singleArrayParsed = true;
			} else if (temp.equals("[") && !parVector && singleArrayParsed) {
				String errorText = "Error: Parser error on signal (" + varNames.get(varNames.size() - 1)
						+ "), (Multi-Dimentional Arrays not suppored with this tool)";
				parser.addInstanceOfError20ParseError( errorText);
				parser.stopParsing();
			} else if (temp.equals("=")) {
				int breakFlag = 0;
				boolean inQuote = false;
				boolean escapeChar = false;
				for (temp = parser.getNextPiece(); !temp.equals(";") && breakFlag != -1
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					if (temp.equals("{")) {
						breakFlag++;
					} else if (temp.equals("\\")) {
						escapeChar = true;
					} else if (temp.equals("\"")) {
						if (escapeChar) {
							escapeChar = false;
						} else if (inQuote) {
							inQuote = false;
						} else {
							inQuote = true;
						}
					} else if (temp.equals("}")) {
						breakFlag--;
					} else if (Module.checkVariableName(temp) && !inQuote) {
						String errorText = "Non-Constant value ( " + temp + " ) in signal initialization";
						parser.addInstanceOfError20ParseError( errorText);
						parser.stopParsing();
						breakFlag = -1;
					} else if (temp.equals(",") && breakFlag == 0) {
						arraySize.add(new Integer(0));
						breakFlag = -1;
					}
				}
				parser.backOneStep();
			} else {
				parVector = false;
				if (!checkVariableName(temp)) {
					parser.addInstanceOfError18UnexpectedToken(temp);
					parser.stopParsing();
				}
				varNames.add(temp);
				singleArrayParsed = false;
			}
		}
		for (int i = 0; i < varNames.size() && !temp.equals("##END_OF_MODULE_CODE"); i++) {
			ArrayList<Variable> newVars = new ArrayList();
			// set up the template
			Variable template = findVariableInModulePortList(varNames.get(i));
			if (template != null) {
				template.name = varNames.get(i);
				if (!template.variableType.equals("") && !template.variableType.equals("DEFAULT_NET_TYPE"))
					lastType = template.variableType;
				if (!template.variableAttribute.equals(""))
					lastAttribute = template.variableAttribute;
				if (!template.variableSign.equals(""))
					lastSign = template.variableSign;
			}
			if (lastType.equals("reg")) {
				template = new Reg(varNames.get(i), lastAttribute, lastSign, arraySize.get(i));
			} else {
				template = new Wire(varNames.get(i), lastAttribute, lastSign, arraySize.get(i));
			}

			// parse the vector if necessary
			if (vecStart.equals("")) {
				newVars.add(template);
			} else {
				newVars.addAll(Variable.createVariablesFromVector(template, vecStart, vecEnd));
			}

			// take the newVars list and either update the existing variable, or
			// add the new variables to the list
			if (findVariableInModulePortList(varNames.get(i)) != null) {
				updatePortVariableInModule(template.name, lastType, newVars);
			} else {
				vars.addAll(newVars);
			}

		}

	}

	public static String parseModule(Block current, Parser parser) {
		String temp = parser.getNextPiece(); // should be equal to the module
												// head name
		String lastType = "DEFAULT_NET_TYPE";
		String lastAttribute = "";
		Module module = new Module("module", current, temp);
		createParameterList(module, parser);
		// module = parseModuleHead(module,parser);
		temp = parser.getNextPiece(); // should be a "(" or a "#" if parameter
										// passing is used
		if (temp.equals("#")) {
			for (; !temp.equals(")") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				parser.removeCurentPiece();
			}
			parser.removeCurentPiece();
			temp = parser.getNextPiece();
		}
		if (temp.equals("(")) {
			temp = parser.getNextPiece(); // should be a the first intereting
											// piece of the port lists
			module.parseVariableInModuleHead(temp, parser);
		}
		current.addSubBlock(module);
		Variable tempVar;
		int piecePlaceholder = 0; // used for checking vector names
		do {
			temp = parser.getNextPiece();
			if (temp.equals("input") || temp.equals("output") || temp.equals("reg") || temp.equals("wire")) {
				module.parseVariableInModule(temp, parser);
			} else if (temp.equals("integer")) {
				ArrayList<String> integerPieces = new ArrayList();
				for (temp = parser.getNextPiece(); !temp.equals(";")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					integerPieces.add(temp);
				}
				module.addVariableToModule(new IntegerVariable(integerPieces));
			}
			// This effectively skips over the parameters, which have already
			// been parsed
			else if (temp.equals("parameter") || temp.equals("localparam")) {
				for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece())
					;
			} else if (temp.equals("assign")) {
				for (module.statementText = "", temp = ""; !temp.equals(";")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					module.statementText += temp + " ";
				}
				module.assignments.add(new ContinuousAssignment(module.statementText, module, parser));
			} else if (!parser.pieceIsKeyword(temp) && (module.findVariableInModule(temp) == null)
					&& parser.checkTaskOrFunctionName(temp) == 0 && Module.checkVariableName(temp)) {
				String temp2 = parser.getNextPiece(); // if this is a submodule
														// then this will be the
														// instantiation name
				if (Module.checkVariableName(temp2)) {
					ArrayList<String> subModule = new ArrayList();
					String subModuleText = "";
					int line = Parser.currentLineNumber;

					subModule.add(temp);
					subModuleText += temp;
					for (subModule.add(temp2), subModuleText += " " + temp2; !temp.equals(";")
							&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						if (temp.equals("$#")) {
							parser.updateLineNumber();
						} else {
							subModuleText += " " + temp;
							subModule.add(temp);
						}
					}
					module.addModuleInstantiation(new ModuleInstantiation(subModuleText, subModule, line));
				} else {
					parser.addInstanceOfError21UndeclaredModuleTaskOrFunction(temp);
					parser.stopParsing();
				}
			}
			// check for a task call, and ignore it if its present
			else if (parser.checkTaskOrFunctionName(temp) != 0) {
				int type = parser.checkTaskOrFunctionName(temp);
				String CallText = "";
				ArrayList<String> CallElements = new ArrayList();
				for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					CallText += temp + " ";
					CallElements.add(temp);
				}
				if (type == 1) {
					module.addVariable(new TaskCall(CallText, CallElements));
				} else if (type == 2) {
					module.addVariable(new FunctionCall(CallText, CallElements));
				}
			} else if (!temp.equals("##END_OF_MODULE_CODE")) {
				/*
				 * If the string temp is a keyword then once it is back in the
				 * it is checked to see if it is part of a block, and if so then
				 * it is passed back to the parser to be handled to another
				 * block type
				 */
				if (parser.pieceIsKeyword(temp))
					parser.checkForNewBlock(module, temp);
				else {
					parser.addInstanceOfError18UnexpectedToken(temp);
					parser.stopParsing();
				}
			}
		} while (!temp.equals("endmodule") && !temp.equals("##END_OF_MODULE_CODE"));
		return temp;
	}

	private static Module parseModuleHead(Module current, Parser parser) {
		Module module = current;
		String piece = parser.getNextPiece();
		String lastType = "DEFAULT_NET_TYPE";
		String lastAttribute = "";
		do {
			piece = parser.getNextPiece();
			if (piece.equals("$#")) {
				parser.checkForNewBlock(current, piece);
			} else if (piece.equals("input")) {
				lastAttribute = "input";
				lastType = "DEFAULT_NET_TYPE";
				piece = parser.getNextPiece();
				if (piece.equals("reg")) {
					lastType = "ERROR";
					piece = parser.getNextPiece();
					module.addVariableToModulePortList(new Reg(piece, "input"));
				} else if (piece.equals("wire")) {
					lastType = "wire";
					piece = parser.getNextPiece();
					module.addVariableToModulePortList(new Wire(piece, "input"));
				} else {
					module.addVariableToModulePortList(new Variable(piece, lastType, "input"));
				}
			} else if (piece.equals("output")) {
				lastAttribute = "output";
				lastType = "DEFAULT_NET_TYPE";
				piece = parser.getNextPiece();
				if (piece.equals("reg")) {
					lastType = "reg";
					piece = parser.getNextPiece();
					module.addVariableToModulePortList(new Reg(piece, "output"));
				} else if (piece.equals("wire")) {
					lastType = "wire";
					piece = parser.getNextPiece();
					module.addVariableToModulePortList(new Wire(piece, "output"));
				} else {
					module.addVariableToModulePortList(new Variable(piece, lastType, "input"));
				}
			} else if (piece.equals("reg")) {
				lastType = "ERROR";
				piece = parser.getNextPiece();
				module.addVariableToModulePortList(new Variable(piece, lastType, lastAttribute));
			} else if (piece.equals("wire")) {
				lastType = "wire";
				piece = parser.getNextPiece();
				module.addVariableToModulePortList(new Wire(piece, lastAttribute));
			} else if (!piece.equals(")")) {
				module.addVariableToModulePortList(new Variable(piece, lastType, lastAttribute));
			}
		} while (!piece.equals(")") && !piece.equals("##END_OF_MODULE_CODE"));
		piece = parser.getNextPiece(); // used to remove the final semicolon
										// from the parse string
		return module;
	}

	private static void createParameterList(Module current, Parser parser) {
		Parameter param;
		int currentPieceIndex = parser.getFreshPieceIndex();
		int currentParameterIndex = currentPieceIndex;
		String paramType = "";
		String paramName = "";
		String paramAssignmentValue = "";
		String temp = parser.getNextPiece();
		boolean multiParamList = false;
		for (; !temp.equals("endmodule") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
			if (temp.equals("$#")) {
				parser.updateLineNumber();
			} else if (temp.equals("#")) {
				temp = parser.getNextPiece();
			} else if ((temp.equals("parameter") || temp.equals("localparam")) && !multiParamList) {
				paramType = temp;
				multiParamList = true;
				temp = parser.getNextPiece();
				if (temp.equals("[")) {
					for (; !temp.equals("]") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece())
						;
					temp = parser.getNextPiece(); // this last piece retrieval
													// should be the parameter
													// name
				}
				if (!checkVariableName(temp)) {
					parser.addInstanceOfError18UnexpectedToken(temp);
					parser.stopParsing();
				}
				paramName = temp;
				temp = parser.getNextPiece();
				if (!temp.equals(";")) {
					temp = parser.getNextPiece();
				}
				for (paramAssignmentValue = ""; !temp.equals(";") && !temp.equals(",") && !temp.equals(")")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					paramAssignmentValue += temp + " ";
				}
				param = new Parameter(paramName, paramType, paramAssignmentValue);
				current.vars.add(param);
				current.parameters.add(param);
				if (temp.equals(";") || temp.equals(")")) {
					multiParamList = false;
				}
				// *
				currentParameterIndex = parser.getFreshPieceIndex();
				// This loop replaces all instances of the current parameter
				// with the parameter's actual value
				for (; !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					if (temp.equals(paramName))
						parser.replaceCurrentPieceText(param.getParameterValue());
				}
				parser.setFreshPieceIndex(currentParameterIndex);
				// */
			} else if (multiParamList) {
				paramType = paramType;
				if (temp.equals("[")) {
					for (; !temp.equals("]") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece())
						;
					temp = parser.getNextPiece(); // this last piece retrieval
													// should be the parameter
													// name
				}
				if (!checkVariableName(temp)) {
					parser.addInstanceOfError18UnexpectedToken(temp);
					parser.stopParsing();
				}
				paramName = temp;
				temp = parser.getNextPiece();
				if (!temp.equals(";")) {
					temp = parser.getNextPiece();
				}
				for (paramAssignmentValue = ""; !temp.equals(";") && !temp.equals(",") && !temp.equals(")")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					paramAssignmentValue += temp + " ";
				}
				param = new Parameter(paramName, paramType, paramAssignmentValue);
				current.vars.add(param);
				current.parameters.add(param);
				if (temp.equals(";") || temp.equals(")")) {
					multiParamList = false;
				}
				// *
				currentParameterIndex = parser.getFreshPieceIndex();
				// This loop replaces all instances of the current parameter
				// with the parameter's actual value
				for (; !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					if (temp.equals(paramName))
						parser.replaceCurrentPieceText(param.getParameterValue());
				}
				parser.setFreshPieceIndex(currentParameterIndex);
				// */
			}
		}

		parser.setLineNumber(1);
		parser.setFreshPieceIndex(currentPieceIndex);
	}

	
	@Override
	public String toString() {
		int i = 0;

		// This section prints out the module Header
		String out = "module " + BlockName + " (\n";
		if (!portVariables.isEmpty()) {
			for (i = 0; !(portVariables.get(i).getAttribute().equals("input")
					|| portVariables.get(i).getAttribute().equals("output")) && i < portVariables.size(); i++)
				;
			out += portVariables.get(i).toString();
			for (i = i + 1; i < portVariables.size(); i++) {
				if (portVariables.get(i).getAttribute().equals("input")
						|| portVariables.get(i).getAttribute().equals("output")) {
					out += "\t,\n";
					out += portVariables.get(i).toString();
				}
			}
		}
		out += "\n);\n";

		// This Section Prints out the Module Body
		for (i = 0; i < parameters.size(); i++)
			out += parameters.get(i).toString();
		if (!vars.isEmpty()) {
			// for(i=0; (vars.get(i).getAttribute().equals("input")
			// || vars.get(i).getAttribute().equals("output"))
			// && i<vars.size(); i++);
			// out += vars.get(i).toString();
			for (i = 0; i < vars.size(); i++) {
				if (!vars.get(i).getAttribute().equals("input") && !vars.get(i).getAttribute().equals("output")
						&& vars.get(i).getClass() != Parameter.class) {
					out += vars.get(i).toString();
					out += ";\n";
				}
			}
		}
		out += "\n";
		for (i = 0; i < subModules.size(); i++) {
			out += subModules.get(i).toString();
		}
		out += "\n";
		for (i = 0; i < subBlocks.size(); i++) {
			out += subBlocks.get(i).toString();
		}
		out += "\n";
		for (i = 0; i < assignments.size(); i++) {
			out += assignments.get(i).toString();
		}
		out += "\nendmodule\n";
		return out;
	}
}
