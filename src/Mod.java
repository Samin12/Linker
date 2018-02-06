import java.util.ArrayList;

/**
 * Created by Samin on 2/6/18.
 */
public class Mod {
    public String[] definitions;
    public String[] uses;
    public String[] programText;

    public int moduleNumber;
    public int moduleLength;

    public Mod(String[] definitions,String[] uses,String[] programText){
        this.definitions=definitions;
        this.uses=uses;
        this.programText=programText;
    }
}
