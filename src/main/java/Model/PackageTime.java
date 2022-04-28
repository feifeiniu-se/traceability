package Model;
import Constructor.Enums.Operator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PackageTime extends CodeBlockTime{
    List<CodeBlock> classes = new ArrayList<>();

    public PackageTime(String sig, String path, CommitCodeChange commitTime, Operator type, CodeBlock own){
        signature = sig;
        filePath.add(path);
        time = commitTime;
        refactorType = type;
        owner = own;
    }
//    public static List<CodeBlock> getClasses(){
//        return classes;
//    }
}
