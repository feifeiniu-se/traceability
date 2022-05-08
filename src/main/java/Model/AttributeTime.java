package Model;
import Constructor.Enums.Operator;
import lombok.Data;

import java.util.List;

@Data
public class AttributeTime extends CodeBlockTime{
    CodeBlock declareType;//返回值类型
    public AttributeTime(String name, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent) {//add attribute
        this.name = name;
        time = cmt;
        refactorType = tp;
        owner = own;
        parentCodeBlock = parent;
        cmt.addCodeChange(this);
        own.addHistory(this);

        ClassTime parentTime = (ClassTime) parent.getLastHistory().clone();
        parentTime.setTime(cmt);
        parentTime.setRefactorType(tp);
        parentTime.getAttributes().add(own);
        parent.addHistory(parentTime);
        cmt.addCodeChange(parentTime);
    }


    @Override
    public String getSignature() {
        return this.getParentCodeBlock().getLastHistory().getSignature()+":"+this.getName();
    }

    @Override
    public List<CodeBlock> getPackages() {
        return null;
    }

    @Override
    public List<CodeBlock> getClasses() {
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
    String getParameters() {
        return null;
    }


//    @Override
//    public CodeBlockTime deepCopy() throws JsonProcessingException {
//        return null;
//    }
}
