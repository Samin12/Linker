/*Mahataz Khandaker */
import java.util.ArrayList;

public class Module {
	ArrayList<String> definitionList = new ArrayList<String>();
	ArrayList<String> useList = new ArrayList<String>();
	ArrayList<String> text = new ArrayList<String>();
	int moduleNumber;
	int moduleLength;
	
	public Module(int moduleLength,int moduleNumber, ArrayList<String> text, ArrayList<String> useList, ArrayList<String> defList){
		this.moduleLength = moduleLength;
		this.moduleNumber = moduleNumber;
		this.text = text;
		this.useList = useList;
		this.definitionList = defList;
	}








	// this method prints out the module number and length
	void prettyPrint(){
		System.out.printf("moduleNumber: %d module length: %d\n ",this.moduleNumber,this.moduleLength);
	}
	//this method prints out all the elements in the use list
	void printUseList(){
		for(int i = 0; i < this.useList.size(); i ++){
			System.out.printf("%s ",this.useList.get(i));
		}
		System.out.println();
	}
	//this method prints out all the elements in the program text
	void printText(){
		for(int i = 0; i < this.text.size(); i ++){
			System.out.printf(" %s \n", this.text.get(i));
		}
		System.out.println();

	}
	//this method prints out all the elements in the definition list
	void printDefList(){
		for(int i = 0; i < this.definitionList.size(); i ++){
			System.out.printf(" %s ", this.definitionList.get(i));
		}
		System.out.println();
	}
	
}
