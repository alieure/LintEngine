/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author cloud9
 */
public class Function extends Block {
	private ArrayList<String> functionText;

	Function(String type, Block comesFrom, String name) {
		BlockType = type;
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		parent = comesFrom;
		functionText = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	public static void parseFunction(Block current, Parser parser) {
		String temp = parser.getNextPiece(); // should be equal to the function
												// name
		String lastType = "DEFAULT_NET_TYPE";
		String lastAttribute = "";
		Function function = new Function("function", current, temp);
		current.addSubBlock(function);

		for (; !temp.equals("endfunction") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
			function.functionText.add(temp);
		}
	}

	@Override
	public String toString() {
		String exit = "function ";
		for (int i = 0; i < functionText.size(); i++) {
			exit += functionText.get(i) + " ";
			if (functionText.get(i).equals("begin") || functionText.get(i).equals("end")
					|| functionText.get(i).equals(";")) {
				exit += "\n";
			}
		}
		return exit + "endfunction\n";
	}
}
