/*Mahataz Khandaker*/
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class Linker1 {
	
	
	/*This method reads in the input file, creates linked lists for the definition list,
	 * use list, and program text. Then it stores these lists in a Module object and adds the object to 
	 * the linked list of module objects.
	 * Then, it looks for addresses that need to be resolved and resolves them.
	 * Last, it looks for addresses that need to be relocated and relocates them.
	 */
	
	public static void main(String[] args){
		HashMap <String,Integer> symbols = new HashMap<String,Integer>();
		//moduleNumber is a hashmap that maps the symbols to the module they are defined in
		HashMap<String,Integer> moduleNumber = new HashMap<String, Integer>();
		ArrayList<String> allUses = new ArrayList<String>();
		ArrayList<Module> modules = new ArrayList<Module>();
		
		//boolean [] symbolUsed = new boolean[symbols.size()];
		try{
			File file = new File("/Users/Samin/Downloads/untitled/input1.txt");
			Scanner input = new Scanner(file);
			int numModules = input.nextInt();
			int [] moduleLengths = new int[numModules];
			input.nextLine();
			int index = 0;
			
			int moduleNum = 0;
			while (input.hasNext()){
				ArrayList<String> defList = new ArrayList<String>();
				ArrayList<String> useList = new ArrayList<String>();
				ArrayList<String> addresses = new ArrayList<String>();
				boolean multiplyDefined = false;
				int relativeAddress = 0;
				int relIndex = 0;
				ArrayList<Integer> relAddress = new ArrayList<Integer>(); 
				String defSymbol = "";
				int numDefs = input.nextInt();
				if(numDefs >= 1){
					for(int i = 0; i < numDefs; i ++){
						defSymbol = input.next();
						defList.add(defSymbol);
						String location = input.next();
						//relativeAddress = Integer.valueOf(location);//will use relative address later for error checking
						relAddress.add(Integer.valueOf(location));//will use later for error checking
						relIndex += 1;
						int absoluteAddress = Integer.valueOf(location);
						if(moduleNum != 0){
							//checks for error 2
							if(symbols.containsKey(defSymbol)){
								System.err.println("Error, the symbol " + defSymbol +  " was multiply defined, first value used");
										
								absoluteAddress = symbols.get(defSymbol);
						//		relativeAddress = symbols.get(defSymbol);
								
								multiplyDefined = true;
							}
							else{
								for(int k = 0; k < moduleNum; k ++){
									absoluteAddress += moduleLengths[k];
								}
							}
							symbols.put(defSymbol,absoluteAddress);
						}
						else{
							symbols.put(defSymbol,absoluteAddress);
						}
						defList.add(location);
						moduleNumber.put(defSymbol,moduleNum);
						
					}
				}
				//scans the use list
				int numUses = input.nextInt();
				if(numUses >= 1){
					for(int i = 0; i < numUses; i ++){
						String symbol = input.next();
						useList.add(symbol);
						String location = input.next();
						useList.add(location);
						allUses.add(symbol);
					}
				}
				//scans the program text
				int numAddresses = input.nextInt();
				//checks for error 4
				if(numDefs > 0){
					if(relAddress.get(relIndex-1) > numAddresses-1 && !multiplyDefined){
						System.err.printf("Error: the defined address of %s is outside module %d, zero (relative) will be used \n ", defSymbol, moduleNum);
						relativeAddress = 0;
						for(int i = 0; i < moduleNum; i ++){
							relativeAddress += moduleLengths[i];
						}
						symbols.put(defSymbol, relativeAddress);
					}
				}
				
				/**
				if(relativeAddress > numAddresses-1 && !multiplyDefined){
					System.err.printf("Error: the defined address of %s is outside module %d, zero (relative) will be used \n ", defSymbol, moduleNum);
					relativeAddress = 0;
					for(int i = 0; i < moduleNum; i ++){
						relativeAddress += moduleLengths[i];
					}
					symbols.put(defSymbol, relativeAddress);
				}
				**/
				moduleLengths[index] = numAddresses;
				index ++;
				for(int k = 0; k < numAddresses; k ++){
					String address = input.next();
					addresses.add(address);	
				}
				moduleNum += 1;
				modules.add(new Module(numAddresses, moduleNum, addresses, useList, defList));	
				
			}//closes while

			
			
			//Pass 2: Resolve external references
			boolean [] symbolUsed = new boolean[symbols.size()]; 
			for(Module m: modules){
				for(int i = 0; i < m.useList.size(); i+=2){
					String symbol = m.useList.get(i);
					int location = Integer.valueOf(m.useList.get(i+1));					
					String address = m.text.get(location);
					int referenceType = Character.getNumericValue((address.charAt(address.length()-1)));
					//checks for error 3 
					if(!(symbols.keySet().contains(symbol))){
						System.err.printf("Warning: the symbol %s is not defined. It has been given the value 0.\n", symbol);
						symbols.put(symbol, 0);
					}
					//checks for error 5
					if(referenceType == 1){
						System.err.println("Error: immediate address (" + address +") on use list; treated as External\n");
						referenceType = 4;
					}
								
					if(referenceType == 4 || referenceType == 3){
						boolean done = false;
						String middle = address.substring(1, 4);
						while (!done){
							String newAddress = "";
							int numDigits = String.valueOf(symbols.get(symbol)).length();
							newAddress += address.charAt(0);
							if(numDigits < 3){
								int difference = 3 - numDigits;
								for(int j = 0; j < difference; j ++){
									newAddress += "0";
								}
								newAddress += Integer.toString(symbols.get(symbol));
							}else{
								newAddress += Integer.toString(symbols.get(symbol));
							}
							m.text.set(location, newAddress);
							if (middle.equals("777")){
								done = true;
								break;
							}
							int num = Integer.valueOf(middle);
							String nextAddress = m.text.get(num);
							middle = nextAddress.substring(1,4);
							address = nextAddress;
							location = num;//changes location to next address location
							
						}
					}
					
				}
				
			}//ends for
			
			//Pass 3: Relocates relative addresses
			for(Module m : modules){
				for(int i = 0; i < m.text.size(); i ++){
					String address = m.text.get(i);
					if (address.length() == 5){//if address last digit is 1,2, or 3
						int referenceType = Character.getNumericValue((address.charAt(address.length()-1)));
						String newAddress = address.substring(0,address.length()-1);
						//checks for error 6
						if(referenceType == 4){
							System.err.println("Error: E type address from module # " + m.moduleNumber + " not on use chain; treated as I type.\n");
							referenceType = 1;
						}
						if (referenceType == 3){
							if(m.moduleNumber!= 0){
								int resolve = Integer.valueOf(newAddress);
								for(int j = 1; j < m.moduleNumber; j ++){
									resolve += moduleLengths[j-1];
								}
								m.text.set(i, Integer.toString(resolve));
								
							}
							else{
								m.text.set(i, newAddress);
							}
						}		
						else{//if reference type is 1 or 2
							m.text.set(i, newAddress);
						}
						
					}			
				}
			}
			
			System.out.println("Symbol table");
			for (String s: symbols.keySet()){
				System.out.printf("%s = %d\n", s, symbols.get(s));
			}
			
			System.out.println();
			System.out.println("Memory Map");
			int counter = 0;
			for(Module m: modules){	
				for(int i = 0; i < m.text.size(); i ++){
					System.out.printf("%d: %s\n", counter,m.text.get(i));
					counter ++;
				}
				
			}
			//checks for error 1
			for(String s: symbols.keySet()){
				if(allUses.contains(s) == false){
					System.err.println("Warning: the symbol "+ s + " was defined in module # " + moduleNumber.get(s) + " but never used");
				}
			}
			
			/**
			 for(int l = 0; l < moduleLengths.length; l ++){
				System.out.printf("%d ",moduleLengths[l]);
				}
			
			//print loops for debugging
			for(Module m: modules){
				m.printUseList();
			}
			
			
			for(Module M: modules){
				M.prettyPrint();
				System.out.println(modules.size());
				//M.printUseList();
			}
			
			//checks for error 1
					
			
			for(int i = 0; i < symbolUsed.length; i ++){
				if(symbolUsed[i] == false){
					System.err.println("Warning: "+ symbols.get(i) + " was defined but never used.");
				}
					
			}
			
			**/
		}//closes try

		catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}	
	}
	
	
	
	
}	
