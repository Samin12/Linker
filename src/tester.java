import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;




public class tester{
    public static void main(String[] args) throws Exception{

        File file = new File("/Users/Samin/Downloads/untitled/input1.txt");
        int relocationConstant=0;


        HashMap<String, Integer> myHashMapDefinitions = new HashMap<String, Integer>();
        HashMap<String, Integer> myHashMapModule = new HashMap<String, Integer>();
        HashMap<String, Integer> myHashMapUses = new HashMap<String, Integer>();


        //readFile("/Users/Samin/Downloads/untitled/input1.txt");
       // String fileContent = readFile("/Users/Samin/Downloads/untitled/input1.txt");
        List<String> fileContentArrayList = Arrays.asList( readFile("/Users/Samin/Downloads/untitled/input1.txt").split("\\n"));

        Integer numberOfModules = Integer.parseInt(fileContentArrayList.get(0));

        System.out.println(numberOfModules);

        String[] D = fileContentArrayList.get(1).split(" ");
        String[] U = fileContentArrayList.get(2).split(" ");
        String[] P = fileContentArrayList.get(3).split(" ");

       // System.out.println(Arrays.toString(D));
       // System.out.println(Arrays.toString(U));
        //System.out.println(Arrays.toString(P));

        //ArrayList<Mod> modArrayList = new ArrayList<>();


        System.out.println(fileContentArrayList);

        for (int i = 0; i < numberOfModules; i++) {



            String[] definitions =  fileContentArrayList.get(1).split(" ");
            Integer numDefinitions = Integer.parseInt(definitions[0]);
            String symbol = definitions[1];
            //System.out.println(numDefinitions);
            Integer location = Integer.parseInt(definitions[2])+relocationConstant;
            //System.out.println(location);


            if (!myHashMapDefinitions.containsKey(symbol)) {
                myHashMapDefinitions.put(symbol, location);
                myHashMapModule.put(symbol, i);
               // System.out.println(symbol);
            }else {
             //   System.out.println("Error: This variable is multiply defined; first value used.\n");
            }

            //System.out.println(myHashMapDefinitions);




            String[] uses =  fileContentArrayList.get(2).split(" ");
            Integer numberUses = Integer.parseInt(uses[0]);
            System.out.println(numberUses);
            for (int j = 0; j < numberUses; j++) {
               //  String symbolUses = uses[1];
                //Integer locationUses = Integer.parseInt(uses[2]);
               // System.out.println(symbolUses+" "+locationUses);

            }


        }





    }

    static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}
