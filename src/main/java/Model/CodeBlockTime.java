package Model;

import Constructor.Enums.Operator;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class CodeBlockTime implements Cloneable, Serializable {
    String name;
    CommitCodeChange time;
    CodeBlockTime pre = null;
    CodeBlockTime post = null;
    Operator refactorType;
    List<CodeBlockTime> deriver = new ArrayList<>();//父
    List<CodeBlockTime> derivee = new ArrayList<>();//子
    CodeBlock parentCodeBlock;
    CodeBlock owner;

    public abstract String getSignature();
    public abstract List<CodeBlock> getPackages();
    public abstract List<CodeBlock> getClasses();
    abstract List<CodeBlock> getMethods();
    abstract List<CodeBlock> getAttributes();
    abstract List<CodeBlock> getParameterRetureType();
    abstract String getParameters();

    @Override
    public Object clone() {
        CodeBlockTime codeBlockTime = null;
        try {
            codeBlockTime = (CodeBlockTime) super.clone();
            codeBlockTime.setDeriver(new ArrayList<>(deriver));
            codeBlockTime.setDerivee(new ArrayList<>(derivee));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return codeBlockTime;
    }


}
