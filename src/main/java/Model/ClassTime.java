package Model;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassTime extends CodeBlockTime {
    List<CodeBlock> classes = new ArrayList<>();
    List<CodeBlock> methods = new ArrayList<>();
    List<CodeBlock> attributes = new ArrayList<>();


//    public ClassTime(String sig, String path, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent){
//        if(!own.getHistory().isEmpty()){
//            ClassTime pre = (ClassTime) own.getLastHistory();
//            deriver = pre.getDeriver();
//            derivee = pre.getDerivee();
//        }
//        signature = sig;
//
//        time = cmt;
//        refactorType = tp;
//        owner = own;
//        parentCodeBlock = parent;
//        own.addHistory(this);
//        cmt.addCodeChange(this);
//    }

    public ClassTime(String name, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent) {//add new classBlock
        this.name = name;
        time = cmt;
        refactorType = tp;
        parentCodeBlock = parent;
        owner = own;
        own.addHistory(this);
        cmt.addCodeChange(this);
        if (parent.getType().equals(CodeBlockType.Class)) {//if the parent is a class
            ClassTime parentTime = (ClassTime) parent.getLastHistory().clone();
            parentTime.setTime(cmt);
            parentTime.setRefactorType(tp);
            parentTime.getClasses().add(own);
            parent.addHistory(parentTime);
            cmt.addCodeChange(parentTime);
        } else if (parent.getType().equals(CodeBlockType.Package)) {// if the parent is a package
            PackageTime parentTime = (PackageTime)parent.getLastHistory().clone();
            parentTime.setTime(cmt);
            parentTime.setRefactorType(tp);
            parentTime.getClasses().add(own);
            parent.addHistory(parentTime);
            cmt.addCodeChange(parentTime);
        }else{
            System.out.println("Wrong: classTime");
        }
    }

//    public ClassTime(String newClassName, CommitCodeChange cmt, Operator tp, CodeBlock newClassBlock) {//merge_Class
//        signature = newClassName;
//        time = cmt;
//        refactorType = tp;
//        owner = newClassBlock;
//        newClassBlock.addHistory(this);
//        cmt.addCodeChange(this);
//    }
//
//    public ClassTime(ClassTime classTime, CommitCodeChange cmt, Operator tp) {//merge class 复制旧的class的内容
//        signature = classTime.getSignature();
//        time = cmt;
//        refactorType = tp;
//        deriver = classTime.getDeriver();
//        parentCodeBlock = classTime.getParentCodeBlock();
//        owner = classTime.getOwner();
//        owner.addHistory(this);
//        cmt.addCodeChange(this);
//    }

    @Override
    public List<CodeBlock> getClasses(){return classes;}

    @Override
    public List<CodeBlock> getMethods(){return methods;}

    @Override
    public List<CodeBlock> getAttributes(){return attributes;}

    public List<String> getFilePath() {
        return null;
    }

    @Override
    public String getSignature() {
        return this.getParentCodeBlock().getLastHistory().getSignature()+"."+this.getName();
    }

    @Override
    public List<CodeBlock> getPackages() {
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

    @Override
    public Object clone() {
        ClassTime classTime = null;
        classTime = (ClassTime) super.clone();
        classTime.setClasses(new ArrayList<>(classes));
        classTime.setMethods(new ArrayList<>(methods));
        classTime.setAttributes(new ArrayList<>(attributes));
        return classTime;
    }

}
