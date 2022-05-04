package Model;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MethodTime extends CodeBlockTime{

    List<CodeBlock> callers = new ArrayList<>();
    List<CodeBlock> callees = new ArrayList<>();
    String parameters;
    List<CodeBlock> parameterType = new ArrayList<>();

    public MethodTime(String name, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent, String params) {//add method
        this.name = name;
        time = cmt;
        refactorType = tp;
        owner = own;
        parentCodeBlock = parent;
        parameters = params;
        cmt.addCodeChange(this);
        own.addHistory(this);
        try{
            ClassTime parentTime = (ClassTime) parent.getLastHistory().deepCopy();
            parentTime.setTime(cmt);
            parentTime.setRefactorType(Operator.Add_Method);
            parentTime.getMethods().add(own);
            parent.addHistory(parentTime);
            cmt.addCodeChange(parentTime);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


//    public MethodTime(MethodTime methodTimeOld, CommitCodeChange commitTime, Operator tp) {//rename_method
//        name = methodTimeOld.name;
//        time = commitTime;
//        refactorType = tp;
//        owner = methodTimeOld.owner;
//        parentCodeBlock = methodTimeOld.parentCodeBlock;
//    }

    @Override
    List<String> getFilePath() {
        return null;
    }

    @Override
    List<CodeBlock> getPackages() {
        return null;
    }

    @Override
    List<CodeBlock> getClasses() {
        return null;
    }

    @Override
    List<CodeBlock> getMethods() {
        return null;
    }

    @Override
    List<CodeBlock> getAttributes() {
        return null;
    }

    @Override
    List<CodeBlock> getParameterRetureType() {
        return null;
    }

    @Override
    public CodeBlockTime deepCopy() throws JsonProcessingException {
        return null;
    }


    //TODO 目前写成parameter List<String>形式 之后再改吧 留意java常用类型
    // byte short int long float double char boolean
}
