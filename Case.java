/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;

/**
 *

 */
public class Case extends Block {
	private ConditionStatement condition;
	/*
	 * "caseBlockOrder" preserves the structureal order of statements and
	 * subBlocks in and always block. 0 - indicates an assignment statement, 1-
	 * indicates the presence of another subBlock
	 */
	private ArrayList<Integer> caseBlockOrder;

	Case(Block comesFrom, String name) {
		BlockType = "";
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		caseBlockOrder = new ArrayList();

		parent = comesFrom;
		LineNumber = Parser.currentLineNumber;
	}

	public ConditionStatement getCondition() {
		return condition;
	}

	public static String parseCase(Block current, Parser parser) {
		Case cs = new Case(current, "");
		current.addSubBlock(cs);
		String temp = "";
		String statementText = "";
		int caseConditionStatementBreak = 0;
		temp = parser.getNextPiece(); // temp should get a "(" back
		// This loop parses the condition statement at the beginning of the case
		// block
		for (caseConditionStatementBreak = 1, temp = parser
				.getNextPiece(), statementText = ""; caseConditionStatementBreak != -1
						&& !temp.equals("##END_OF_MODULE_CODE");) {
			if (temp.equals("$#")) {
				parser.updateLineNumber();
			} else if (temp.equals("(")) {
				caseConditionStatementBreak++;
				statementText += temp + " ";
				temp = parser.getNextPiece();
			} else if (temp.equals(")")) {
				caseConditionStatementBreak--;
				if (caseConditionStatementBreak != 0) {
					statementText += temp + " ";
					temp = parser.getNextPiece();
				} else {
					caseConditionStatementBreak = -1;
				}
			} else {
				statementText += temp + " ";
				temp = parser.getNextPiece();
			}
		}
		cs.condition = new ConditionStatement(statementText, cs, parser);

		for (temp = parser.getNextPiece(); !temp.equals("endcase")
				&& !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
			if (temp.equals("$#")) {
				parser.updateLineNumber();
			}
			// if temp is a legal start to a verilog sub-case value
			else if (Parser.isANumber(temp) == 1 || Module.checkVariableName(temp) || temp.equals("(")
					|| temp.equals("{")) {
				SubCase.parseSubCase(cs, parser, temp);
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
		temp += "case( " + condition.toString() + " ) \\\\LINE: " + LineNumber + ", Vars in Condition: "
				+ condition.conditionVars.toString() + "\n";
		for (i = 0; i < this.subBlocks.size(); i++) {
			temp += this.subBlocks.get(i).toString();
		}
		temp += "endcase\n";
		return temp;
	}
}
