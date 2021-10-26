/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author cloud9
 */
public class Task extends Block {
	private ArrayList<String> taskText;

	Task(String type, Block comesFrom, String name) {
		BlockType = type;
		BlockName = name;
		vars = new ArrayList();
		subBlocks = new ArrayList();
		assignments = new ArrayList();
		parent = comesFrom;
		taskText = new ArrayList();
		LineNumber = Parser.currentLineNumber;
	}

	public static void parseTask(Block current, Parser parser) {
		String temp = parser.getNextPiece(); // should be equal to the task name
		String lastType = "DEFAULT_NET_TYPE";
		String lastAttribute = "";
		Task task = new Task("task", current, temp);
		current.addSubBlock(task);

		for (; !temp.equals("endtask") && !temp.equals("##END_OF_MODULE_CODE"); temp = parser.getNextPiece()) {
			task.taskText.add(temp);
		}
	}

	@Override
	public String toString() {
		String exit = "task ";
		for (int i = 0; i < taskText.size(); i++) {
			exit += taskText.get(i) + " ";
			if (taskText.get(i).equals("begin") || taskText.get(i).equals("end") || taskText.get(i).equals(";")) {
				exit += "\n";
			}
		}
		return exit + "endtask\n";
	}
}
