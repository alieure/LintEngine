
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class LintEngine {
	public static int count = 0;
	public static int value = 0;
	public static void main(String[] args) {
	Testing test = new Testing(args);
	}
}

class Testing {
	Scanner sc;

	Testing(String[] args) {
		sc = new Scanner(System.in);
		Parser ParserOutput;
		ArrayList<String> VerilogCode;
		ReadVerilogFile file = new ReadVerilogFile();
		file.setFile(args[0]);
		file.newFile();
		VerilogCode = file.getCode();
		ParserOutput = new Parser(VerilogCode);
		StartLinting Engine = new StartLinting();
		Engine.Start(ParserOutput);
	}
}


///////////////////////////////////////////////////////////////////////////////////////


class ReadVerilogFile {

	private File file;
	private ArrayList<String> code;
	private String FileName;
	
	ReadVerilogFile() {
		FileName = "";
		code = new ArrayList();
	}

	public void setFile(String fileName) {
		file = new File(fileName);
	}
	
	private void getFile() {
		try {
			FileName = file.getName();
			System.out.println("Testing Verilog File:\t\""+FileName+"\"\n");
		} catch (Exception e) {
			System.out.println("File Read Error");
		}
	}
	private void readFile() {
		Scanner inputFile;

		try {
			inputFile = new Scanner(file);
			try {
				while (inputFile.hasNext()) {
					code.add(inputFile.nextLine());
				}
			}
			
			catch (Exception e) {
				System.out.println("File Has Failed to transfer to ArrayList\n" + "Location: FileReader.readFile()");
			}
		}
		
		catch (Exception e) {
			System.out.println("File Has Failed to Read\n" + "Location: FileReader.readFile()");
		}
	}
	
	public void newFile() {
		getFile();
		readFile();
	}
	
	public ArrayList<String> getCode() {
		return code;
	}
	
}
////////////////////////////////////////////////////////////////////////////////////////


class StartLinting {
	
	ArrayList<Display> Parser_error;	
	ArrayList<Display> Lint_rule;

	private void EmptyList() {
		Lint_rule = new ArrayList();
		Parser_error = new ArrayList();
	}
	
	public ArrayList<Display> Start(Parser parser) {
	//System.out.println("\nVariables are:\n"+parser.getVariableList()+"\n");
		EmptyList();
		Parser_error = (parser.getParserErrorList());
		if(Parser_error.isEmpty()){
			Lint_rule.addAll(Rule1_PortMismatch.getDisplayMessage(parser));
			Lint_rule.addAll(Rule2_MixedAssignment.getDisplayMessage(parser));
			Lint_rule.addAll(Rule3_SequentialLogic.getDisplayMessage(parser));
			Lint_rule.addAll(Rule4_CombinationalLogic.getDisplayMessage(parser));
		}
		
		//System.out.println(Lint_rule.size());
		
		System.out.println("Total Warnings found:\t"+ LintEngine.count +"\n");
		
		for (Iterator iterator = Lint_rule.iterator(); iterator.hasNext();) {
			Display display = (Display) iterator.next();
			if(display.getDisplayMsg().startsWith("No")){
				LintEngine.value++;
			}
		}
		
		if (LintEngine.value == 4){
			System.out.println("RTL Code is error free!");
			LintEngine.value = 0;
		}
		
		return Lint_rule;
	}
}

////////////////////////////////////////////////////////////////////////////////////