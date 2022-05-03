package Model;

import Constructor.Enums.Operator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassTime extends CodeBlockTime{
    List<CodeBlock> classes = new ArrayList<>();
    List<CodeBlock> methods = new ArrayList<>();
    List<CodeBlock> attributes = new ArrayList<>();


    public ClassTime(String sig, String path, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent){
        if(!own.getHistory().isEmpty()){
            ClassTime pre = (ClassTime) own.getLastHistory();
            filePath = new ArrayList<>(pre.getFilePath());
            deriver = pre.getDeriver();
            derivee = pre.getDerivee();
        }
        signature = sig;

        filePath.add(path);
        time = cmt;
        refactorType = tp;
        owner = own;
        parentCodeBlock = parent;
        own.addHistory(this);
        cmt.addCodeChange(this);
    }

    public ClassTime(String sig, CommitCodeChange cmt, Operator tp, CodeBlock own, CodeBlock parent) {
        if(!own.getHistory().isEmpty()){
            ClassTime pre = (ClassTime) own.getLastHistory();
            filePath = new ArrayList<>(pre.getFilePath());
            deriver = pre.getDeriver();
            derivee = pre.getDerivee();
        }
        signature = sig;
        time = cmt;
        refactorType = tp;
        parentCodeBlock = parent;
        owner = own;
        own.addHistory(this);
        cmt.addCodeChange(this);
    }

    public ClassTime(String newClassName, CommitCodeChange cmt, Operator tp, CodeBlock newClassBlock) {//merge_Class
        signature = newClassName;
        time = cmt;
        refactorType = tp;
        owner = newClassBlock;
        newClassBlock.addHistory(this);
        cmt.addCodeChange(this);
    }

    public ClassTime(ClassTime classTime, CommitCodeChange cmt, Operator tp) {//merge class 复制旧的class的内容
        signature = classTime.getSignature();
        time = cmt;
        refactorType = tp;
        deriver = classTime.getDeriver();
        parentCodeBlock = classTime.getParentCodeBlock();
        owner = classTime.getOwner();
        owner.addHistory(this);
        cmt.addCodeChange(this);
    }
}
