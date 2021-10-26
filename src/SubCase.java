/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;

/**
 *

 */
public class SubCase extends Block {
	private ConditionStatement condition;
	/*
	 * "caseBlockOrder" preserves the structureal order of statements and
	 * subBlocks in and always block. 0 - indicates an assignment statement, 1-
	 * indicates the presence of another subBlock
	 */
	private ArrayList<Integer> subCaseBlockOrder;

	SubCase(Block comesFrom, String name) {
		BlockType = "";
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		subCaseBlockOrder = new ArrayList();

		parent = comesFrom;
		LineNumber = Parser.currentLineNumber;
	}

	public static String parseSubCase(Block current, Parser parser, String firstTemp) {
		SubCase sub = new SubCase(current, "");
		current.addSubBlock(sub);
		String temp = firstTemp;
		String statementText = "";
		int subCaseConditionStatementBreak = 0;
		// temp = parser.getNextPiece(); // temp should get a "(" back
		// This loop parses the condition statement at the beginning of the case
		// block
		for (subCaseConditionStatementBreak = 0, statementText = ""; subCaseConditionStatementBreak != -1
				&& !temp.equals("##END_OF_MODULE_CODE");) {
			if (temp.equals("$#")) {
				parser.updateLineNumber();
				temp = parser.getNextPiece();
			} else if (temp.equals(":")) {
				if (subCaseConditionStatementBreak == 0) {
					subCaseConditionStatementBreak = -1;
				} else {
					statementText += temp + " ";
					temp = parser.getNextPiece();
				}
			} else if (temp.equals("[")) {
				subCaseConditionStatementBreak++;
				statementText += temp + " ";
				temp = parser.getNextPiece();
			} else if (temp.equals("]")) {
				subCaseConditionStatementBreak--;
				statementText += temp + " ";
				temp = parser.getNextPiece();
			} else {
				statementText += temp + " ";
				temp = parser.getNextPiece();
			}
		}
		sub.condition = new ConditionStatement(statementText, sub, parser);

		temp = parser.getNextPiece();
		// if there will be multiple sublocks or assginments
		if (temp.equals("begin")) {
			for (temp = parser.getNextPiece(); !temp.equals("end")
					&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
				if (parser.pieceIsKeyword(temp)) {
					if (!temp.equals("always")) {
						parser.checkForNewBlock(sub, temp);
						if (!temp.equals("$#"))
							sub.subCaseBlockOrder.add(new Integer(1));
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
					sub.addVariable(new TaskCall(taskCallText, taskCallElements));
					sub.subCaseBlockOrder.add(new Integer(2));
				} else if (sub.findVariableInParentBlockHierarchy(temp) != null
						|| !sub.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
					for (statementText = ""; !temp.equals(";")
							&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
						statementText += temp + " ";
					}
					sub.addAssignment(new AssignmentStatement(statementText, sub, parser));
					sub.subCaseBlockOrder.add(new Integer(0));
				} else {
					parser.addInstanceOfError8UndeclaredSignal(temp);
					parser.stopParsing();
				}
			}
		} else {
			if (temp.equals("$#")) {
				parser.checkForNewBlock(sub, temp);
				temp = parser.getNextPiece();
			}
			if (parser.pieceIsKeyword(temp)) {
				if (!temp.equals("always")) {
					parser.checkForNewBlock(sub, temp);
					if (!temp.equals("$#"))
						sub.subCaseBlockOrder.add(new Integer(1));
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
				sub.addVariable(new TaskCall(taskCallText, taskCallElements));
				sub.subCaseBlockOrder.add(new Integer(2));
			} else if (sub.findVariableInParentBlockHierarchy(temp) != null
					|| !sub.findVectorNameInParentBlockHierarchy(temp).isEmpty()) {
				for (statementText = ""; !temp.equals(";")
						&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
					statementText += temp + " ";
				}
				sub.addAssignment(new AssignmentStatement(statementText, sub, parser));
				sub.subCaseBlockOrder.add(new Integer(0));
			} else {
				parser.addInstanceOfError8UndeclaredSignal(temp);
				parser.stopParsing();
			}
		}

		return temp;
	}

	@Override
	public String toString() {
		String temp = "";
		int i = 0;
		int subBlockCount = 0;
		int assignmentStatementCount = 0;
		int varsCount = 0;
		temp += condition.toString() + ": begin \\\\LINE: " + LineNumber + ", Vars in Condition: "
				+ condition.conditionVars.toString() + "\n";

		for (i = 0, subBlockCount = 0, assignmentStatementCount = 0, varsCount = 0; i < this.subCaseBlockOrder
				.size(); i++) {
			if (subCaseBlockOrder.get(i) == 1) {
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

		temp += "end\n";
		return temp;
	}
}
