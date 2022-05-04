package Model;

import Constructor.Enums.Operator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class CodeBlockTime {
    String name;
    CommitCodeChange time;
    CodeBlockTime pre = null;
    CodeBlockTime post = null;
    Operator refactorType;
    List<CodeBlockTime> deriver = new ArrayList<>();//父
    List<CodeBlockTime> derivee = new ArrayList<>();//子
    CodeBlock parentCodeBlock;
    CodeBlock owner;

    abstract List<String> getFilePath();
    abstract List<CodeBlock> getPackages();
    abstract List<CodeBlock> getClasses();
    abstract List<CodeBlock> getMethods();
    abstract List<CodeBlock> getAttributes();
    abstract List<CodeBlock> getParameterRetureType();
    abstract List<String> getParameters();
    public abstract CodeBlockTime deepCopy() throws JsonProcessingException;


}
