package Model;

import lombok.Data;

import java.util.List;

@Data
public class MethodTime extends CodeBlockTime{

    List<CodeBlock> callers;
    List<CodeBlock> callees;
    List<String> parameters;
    List<CodeBlock> parameterType;
    //TODO 目前写成parameter List<String>形式 之后再改吧 留意java常用类型
    // byte short int long float double char boolean
}
