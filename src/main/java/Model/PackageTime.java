package Model;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PackageTime extends CodeBlockTime{
    List<String> classes = new ArrayList<>();


    public PackageTime(String sig, String path, CommitCodeChange commitTime, String type, CodeBlock own){
        signature = sig;
        filePath.add(path);
        time = commitTime;
        refactorType = type;
        owner = own;
    }
}
