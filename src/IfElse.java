/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;

/**
 *

 */
public class IfElse extends Block {
	private ConditionStatement condition;
	/*
	 * "ifElseBlockOrder" preserves the structureal order of statements and
	 * subBlocks in and always block. 0 - indicates an assignment statement, 1-
	 * indicates the presence of another subBlock
	 */
	private ArrayList<Integer> ifElseBlockOrder;
	private int ifElseType; // 0="if", 1="else if", 2="else"

	IfElse(Block comesFrom, String name) {
		BlockType = "";
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		ifElseBlockOrder = new ArrayList();

		parent = comesFrom;
		LineNumber = Parser.currentLineNumber;
	}

	public static IfElse parseIf(Block current, Parser parser) {
		IfElse ie = new IfElse(current, "");
		current.addSubBlock(ie);
		String temp = "";
		String statementText = "";
		int ifConditionStatementBreak = 0;
		temp = parser.getNextPiece(); // temp should get a "(" back
		// This loop parses the condition statement at the beginning of the if
		// block
		for (ifConditionStatementBreak = 1, temp = parser
				.getNextPiece(), statementText = ""; ifConditionStatementBreak != -1
						&& !temp.equals("##END_OF_MODULE_CODE");) {
			if (temp.equals("$#")) {
				parser.updateLineNumber();
			} else if (temp.equals("(")) {
				ifConditionStatementBreak++;
				statementText += temp + " ";
				temp = parser.getNextPiece();
			} else if (temp.equals(")")) {
				ifConditionStatementBreak--;
				if (ifConditionStatementBreak != 0) {
					statementText += temp + " ";
					temp = parser.getNextPiece();
				} else {
					ifConditionStatementBreak = -1;
				}
			} else {
				statementText += temp + " ";
				temp = parser.getNextPiece();
			}
		}
		ie.condition = new ConditionStatement(statementText, ie, parser);

		temp = parser.getNextPiece();
		// if there will be multiple sublocks or assginments
		if (temp.equals("$#")) {
			parser.updateLineNumber();
			temp = parser.getNextPiece();
		}
		if (temp.equals("begin")) {
			for (temp = parser.getNextPiece(); !temp.equals("end")
					&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				if (parser.pieceIsKeyword(temp)) {
					if (!temp.equals("always")) {
						parser.checkForNewBlock(ie, temp);
						if (!temp.equals("$#"))
							ie.ifElseBlockOrder.add(new Integer(1));
					} else {
						String errorText = "Error: nested always blocks not allowed";
						parser.addErrorToParserErrorList(new Display( errorText, Parser.getCurrentLineNumber()));
						parser.stopParsing();
					}
				} else if (parser.checkTaskOrFunctionName(temp) != 0) {
					String taskCallText = "";
					ArrayList<String> taskCallElements = new ArrayList();
					for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						taskCallText += temp + " ";
						taskCallElements.add(temp);
					}
					ie.addVariable(new TaskCall(taskCallText, taskCallElements));
					ie.ifElseBlockOrder.add(new Integer(2));
				} else if (ie.findVariableInParentBlockHierarchy(temp) != null
						|| !ie.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
					for (statementText = ""; !temp.equals(";")
							&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						statementText += temp + " ";
					}
					ie.addAssignment(new AssignmentStatement(statementText, ie, parser));
					ie.ifElseBlockOrder.add(new Integer(0));
				} else {
					// add piece identification error here
				}
			}
		} else {
			if (temp.equals("$#")) {
				parser.checkForNewBlock(ie, temp);
				temp = parser.getNextPiece();
			}
			if (parser.pieceIsKeyword(temp)) {
				if (!temp.equals("always")) {
					parser.checkForNewBlock(ie, temp);
					if (!temp.equals("$#"))
						ie.ifElseBlockOrder.add(new Integer(1));
				} else {
					String errorText = "Error: nested always blocks not allowed";
					parser.addErrorToParserErrorList(new Display( errorText, Parser.getCurrentLineNumber()));
					parser.stopParsing();
				}
			} else if (parser.checkTaskOrFunctionName(temp) != 0) {
				String taskCallText = "";
				ArrayList<String> taskCallElements = new ArrayList();
				for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					taskCallText += temp + " ";
					taskCallElements.add(temp);
				}
				ie.addVariable(new TaskCall(taskCallText, taskCallElements));
				ie.ifElseBlockOrder.add(new Integer(2));
			} else if (ie.findVariableInParentBlockHierarchy(temp) != null
					|| !ie.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
				for (statementText = ""; !temp.equals(";")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					statementText += temp + " ";
				}
				ie.addAssignment(new AssignmentStatement(statementText, ie, parser));
				ie.ifElseBlockOrder.add(new Integer(0));
			} else {
				// add piece identification error here
			}
		}

		return ie;
	}

	public static String parseElse(Block current, Parser parser) {
		String temp = parser.getNextPiece();
		String statementText = "";
		if (temp.equals("$#")) {
			parser.updateLineNumber();
			temp = parser.getNextPiece();
		}
		// if this "else" statement is really and "else if" then parse it like
		// an "if" statment, othewise parse it as an "else"
		if (temp.equals("if")) {
			IfElse ie = parseIf(current, parser);
			ie.setIfElseType(1);
		} else if (temp.equals("begin")) {
			IfElse ie = new IfElse(current, "");
			current.addSubBlock(ie);
			ie.setIfElseType(2);
			for (temp = parser.getNextPiece(); !temp.equals("end")
					&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				if (parser.pieceIsKeyword(temp)) {
					parser.checkForNewBlock(ie, temp);
					if (!temp.equals("$#"))
						ie.ifElseBlockOrder.add(new Integer(1));
				} else if (parser.checkTaskOrFunctionName(temp) != 0) {
					String taskCallText = "";
					ArrayList<String> taskCallElements = new ArrayList();
					for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						taskCallText += temp + " ";
						taskCallElements.add(temp);
					}
					ie.addVariable(new TaskCall(taskCallText, taskCallElements));
					ie.ifElseBlockOrder.add(new Integer(2));
				} else if (ie.findVariableInParentBlockHierarchy(temp) != null
						|| !ie.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
					for (statementText = ""; !temp.equals(";")
							&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						statementText += temp + " ";
					}
					ie.addAssignment(new AssignmentStatement(statementText, ie, parser));
					ie.ifElseBlockOrder.add(new Integer(0));
				} else {
					// add piece identification error here
				}
			}
		} else {
			IfElse ie = new IfElse(current, "");
			current.addSubBlock(ie);
			ie.setIfElseType(2);
			if (temp.equals("$#")) {
				parser.checkForNewBlock(ie, temp);
				temp = parser.getNextPiece();
			}
			if (parser.pieceIsKeyword(temp)) {
				parser.checkForNewBlock(ie, temp);
				if (!temp.equals("$#"))
					ie.ifElseBlockOrder.add(new Integer(1));
			} else if (parser.checkTaskOrFunctionName(temp) != 0) {
				String taskCallText = "";
				ArrayList<String> taskCallElements = new ArrayList();
				for (; !temp.equals(";") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					taskCallText += temp + " ";
					taskCallElements.add(temp);
				}
				ie.addVariable(new TaskCall(taskCallText, taskCallElements));
				ie.ifElseBlockOrder.add(new Integer(2));
			} else if (ie.findVariableInParentBlockHierarchy(temp) != null
					|| !ie.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
				for (statementText = ""; !temp.equals(";")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					statementText += temp + " ";
				}
				ie.addAssignment(new AssignmentStatement(statementText, ie, parser));
				ie.ifElseBlockOrder.add(new Integer(0));
			} else {
				// add piece identification error here
			}
		}

		return temp;
	}

	public void setIfElseType(int type) {
		this.ifElseType = type;
	}

	public ConditionStatement getConditionStatement() {
		return condition;
	}

	public int getIfElseType() {
		return this.ifElseType;
	}
/*
	@Override
	public String toString() {
		String temp = "";
		int i = 0;
		int subBlockCount = 0;
		int assignmentStatementCount = 0;
		int varsCount = 0;
		if (this.ifElseType == 0) {
			temp += "if(";
			temp += this.condition.toString();
			temp += ") begin \\\\LINE: " + LineNumber + ", Vars in Condition: " + condition.conditionVars.toString()
					+ "\n";
			for (i = 0, subBlockCount = 0, assignmentStatementCount = 0, varsCount = 0; i < this.ifElseBlockOrder
					.size(); i++) {
				if (ifElseBlockOrder.get(i) == 1) {
					temp += this.subBlocks.get(subBlockCount).toString();
					subBlockCount++;
				} else if (!assignments.isEmpty()) {
					temp += this.assignments.get(assignmentStatementCount);
					assignmentStatementCount++;
				} else if (!vars.isEmpty()) {
					temp += this.vars.get(varsCount);
					varsCount++;
				}
			}
			temp += "end //ends if()\n";
		} else if (this.ifElseType == 1) {
			temp += "else if(";
			temp += this.condition.toString();
			temp += ") begin " + "//Vars in Condition: " + condition.conditionVars.toString() + "\n";
			for (i = 0, subBlockCount = 0, assignmentStatementCount = 0, varsCount = 0; i < this.ifElseBlockOrder
					.size(); i++) {
				if (ifElseBlockOrder.get(i) == 1) {
					temp += this.subBlocks.get(subBlockCount).toString();
					subBlockCount++;
				} else if (!assignments.isEmpty()) {
					temp += this.assignments.get(assignmentStatementCount);
					assignmentStatementCount++;
				} else if (!vars.isEmpty()) {
					temp += this.vars.get(varsCount);
					varsCount++;
				}
			}
			temp += "end //ends else if()\n";
		} else {
			temp += "else begin\n";
			for (i = 0, subBlockCount = 0, assignmentStatementCount = 0, varsCount = 0; i < this.ifElseBlockOrder
					.size(); i++) {
				if (ifElseBlockOrder.get(i) == 1) {
					temp += this.subBlocks.get(subBlockCount).toString();
					subBlockCount++;
				} else if (!assignments.isEmpty()) {
					temp += this.assignments.get(assignmentStatementCount);
					assignmentStatementCount++;
				} else if (!vars.isEmpty()) {
					temp += this.vars.get(varsCount);
					varsCount++;
				}
			}
			temp += "end //ends else\n";
		}

		return temp;
	} */
}
