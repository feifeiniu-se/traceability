package Model;
import Constructor.Enums.Operator;
import lombok.Data;

@Data
public class AttributeTime extends CodeBlockTime{
    CodeBlock declareType;//返回值类型
    public AttributeTime(String sig, String path, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent){
        signature = sig;
        filePath.add(path);
        time = cmt;
        refactorType = tp;
        owner = own;
        parentCodeBlock = parent;


    }

}
