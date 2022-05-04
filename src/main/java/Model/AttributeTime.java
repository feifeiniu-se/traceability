package Model;
import Constructor.Enums.Operator;
import com.fasterxml.jackson.core.JsonProcessingException;
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

        try {
            ClassTime parentTime = (ClassTime) parent.getLastHistory().deepCopy();
            parentTime.setTime(cmt);
            parentTime.setRefactorType(Operator.Add_Attribute);
            parentTime.getAttributes().add(own);
            parent.addHistory(parentTime);
            cmt.addCodeChange(parentTime);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

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
    List<String> getParameters() {
        return null;
    }

    @Override
    public CodeBlockTime deepCopy() throws JsonProcessingException {
        return null;
    }
}
