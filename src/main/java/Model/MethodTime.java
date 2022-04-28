package Model;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import lombok.Data;

import java.util.List;

@Data
public class MethodTime extends CodeBlockTime{

    List<CodeBlock> callers;
    List<CodeBlock> callees;
    String parameters;
    List<CodeBlock> parameterType;

    public MethodTime(String sig, String path, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent, String params) {
        signature = sig;
        filePath.add(path);
        time = cmt;
        refactorType = tp;
        owner = own;
        parentCodeBlock = parent;
        parameters = params;
    }

    //TODO 目前写成parameter List<String>形式 之后再改吧 留意java常用类型
    // byte short int long float double char boolean
}
