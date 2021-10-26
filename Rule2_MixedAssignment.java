import java.util.ArrayList;

public class Rule2_MixedAssignment {
	public static ArrayList<Display> getDisplayMessage(Parser parser) {
		
		ArrayList<Display> Msg_List = new ArrayList();
		ArrayList<AssignmentStatement> Statement_List;
		ArrayList<Block> blocks = parser.getBlockList();
		String msg = "All Good!";
		Block Blk;
		AssignmentStatement Statement;
		int found = 0;
		

		// Go through the list of blocks
		for (int i = 0; i < blocks.size(); i++) {
			// get the current block
			Blk = blocks.get(i);
			found = 0;
			// see if the current block is an always block
			if (Blk.getClass() == Always.class) {
				// get the list of assignment statements in the always block
				Statement_List = Blk.getAllAssignmentStatements();

				// go through the statements
				for (int j = 0; j < Statement_List.size(); j++) {
					Statement = Statement_List.get(j);

					// see if current statement contains an nonblocking line of
					// code
					if (Statement.assignmentText.contains("< =") && found == 0) {
						// go though the statements again
						
					    String Nonblocking = Statement.assignmentText.replaceAll("< =", "<=");					
						for (int k = 0; k < Statement_List.size(); k++) {
							Statement = Statement_List.get(k);
							
							String Blocking = Statement.assignmentText;
							// Look for an equals sign to see if it is an
							// blocking line
							if (Statement.assignmentText.contains("=") && found == 0) {
								int index = Statement.assignmentText.indexOf("=");

								// verify that it is a blocking line by making
								// sure there is not a non blocking symbol at
								// the start
								if (Statement.assignmentText.charAt(index - 2) != '<' && found == 0) {
									
									LintEngine.count++;
									// create the error message for the line
									msg = LintEngine.count + ") WARNING!\nMixed Assignments Statements found"
										 + " in a same always block(on line no. " + Blk.LineNumber + " )\n"
										 + "Non-Blocking Statement:\t \" " + Nonblocking + "\"\t on line no. -> " + 
										 Statement_List.get(j).LineNumber + "\nBlocking Statement:\t \" "
										 + Blocking +"\"\t on line no. -> " + Statement_List.get(k).LineNumber + "\n";
			

							// put the error message into the error list
							//		Display Warning = new Display();
							//		Warning.setDisplayMsg(msg);
							//		Warning.setDisplayMsg("15");
							//		Warning.addLineNumber(Statement_List.get(j).LineNumber);
							//		Warning.addLineNumber(Statement_List.get(k).LineNumber);
							//		Msg_List.add(Warning);
									System.out.println(msg);

									// label the block as already been flagged
									// so that multiply errors arent listed
									found = 1;
								}
							}
						}
					}
				}
			}
		}

		if (msg.equals("All Good!")) {
			System.out.println("No Mixed Blocking and Non-blocking assignments in the same always block!\n");
			Display Warning = new Display("No Mixed Blocking and Non-blocking assignments in the same always block!");
			Msg_List.add(Warning);
		} 
		return Msg_List;
	}
}
