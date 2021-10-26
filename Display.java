
import java.util.ArrayList;
public class Display {
	
	private String DisplayMsg;
	private ArrayList<Integer> LineNumbers;

	Display() {
		DisplayMsg = "";
		LineNumbers = new ArrayList();
	}

	Display(String DisplayMsg_in) {
		DisplayMsg = DisplayMsg_in;
	}

	Display(String DisplayMsg_in, int line) {
		DisplayMsg = DisplayMsg_in;
		LineNumbers = new ArrayList();
		LineNumbers.add(line);
	}

	/*public String setLineNumbers(ArrayList<Integer> NewLns) {
		LineNumbers = NewLns;
		return "Successful";
	}

	public String addLineNumber(int i) {
		LineNumbers.add(i);
		return "Successful";
	}

	public ArrayList<Integer> getLineNumbers() {
		return LineNumbers;
	}*/

	public String getDisplayMsg() {
		return DisplayMsg;
	}
/*
	public String toString() {
		System.out
				.println(":\n" + "Error Message: " + DisplayMsg + "\nError Definition:");
		return "Successful";
	}

	public void setDisplayMsg(String msg) {
		DisplayMsg = msg;
	}

	public boolean compareTo(Display msg) {
		if (msg.getDisplayMsg().equals(DisplayMsg)){
			return true;
		}
		return false;
	}*/
}