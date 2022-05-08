package Model;

import Constructor.Enums.Operator;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class PackageTime extends CodeBlockTime implements Cloneable, Serializable {
    List<CodeBlock> classes = new ArrayList<>();
    List<CodeBlock> packages = new ArrayList<>();

    public PackageTime(String name, CommitCodeChange commitTime, Operator type, CodeBlock own) {// add new package
        this.name = name;
        time = commitTime;
        refactorType = type;
        owner = own;
        own.addHistory(this);
        commitTime.addCodeChange(this);
    }

    @Override
    public Object clone() {
        PackageTime packageTime = null;
        packageTime = (PackageTime) super.clone();
        packageTime.setClasses(new ArrayList<>(classes));
        return packageTime;
    }

    @Override
    public List<CodeBlock> getClasses() {
        return classes;
    }


    @Override
    public String getSignature() {
        return this.getName();
    }

    @Override
    public List<CodeBlock> getPackages() {
        return packages;
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

}
