package Model;

import Constructor.Enums.Operator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassTime extends CodeBlockTime{
    List<CodeBlock> classes = new ArrayList<>();
    List<CodeBlock> methods = new ArrayList<>();
    List<CodeBlock> attributes = new ArrayList<>();

    public ClassTime(String sig, String path, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent){
        signature = sig;
        filePath.add(path);
        time = cmt;
        refactorType = tp;
        owner = own;
        parentCodeBlock = parent;
    }

}
