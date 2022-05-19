package Model;

import Constructor.Enums.Operator;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public abstract class CodeBlockTime implements Cloneable, Serializable {
    String name;
    CommitCodeChange time;
    CodeBlockTime pre = null;
    CodeBlockTime post = null;
    Operator refactorType;
    Set<CodeBlockTime> deriver = new HashSet<>();  // 父：两个类合并为一个类
    Set<CodeBlockTime> derivee = new HashSet<>();  // 子
    CodeBlock parentCodeBlock;
    CodeBlock owner;

    public abstract String getSignature();
    public abstract Set<CodeBlock> getPackages();
    public abstract Set<CodeBlock> getClasses();
    public abstract Set<CodeBlock> getMethods();
    public abstract Set<CodeBlock> getAttributes();
    abstract Set<CodeBlock> getParameterRetureType();
    abstract String getParameters();

    @Override
    public Object clone() {
        CodeBlockTime codeBlockTime = null;
        try {
            codeBlockTime = (CodeBlockTime) super.clone();
            codeBlockTime.setDeriver(new HashSet<>(deriver));
            codeBlockTime.setDerivee(new HashSet<>(derivee));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return codeBlockTime;
    }
}
