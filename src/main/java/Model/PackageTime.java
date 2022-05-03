package Model;
import Constructor.Enums.Operator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PackageTime extends CodeBlockTime{
    List<CodeBlock> classes = new ArrayList<>();

    public PackageTime(String sig, String path, CommitCodeChange commitTime, Operator type, CodeBlock own){
        signature = sig;
        filePath.add(path);
        time = commitTime;
        refactorType = type;
        owner = own;
        own.addHistory(this);
        commitTime.addCodeChange(this);
    }
    public PackageTime(String sig, CommitCodeChange commitTime, Operator type, CodeBlock own){
        signature = sig;
        time = commitTime;
        refactorType = type;
        owner = own;
        own.addHistory(this);
        commitTime.addCodeChange(this);
    }
    public PackageTime(String sig, CommitCodeChange commitTime, Operator type, CodeBlockTime oldCodeBlockTime, CodeBlock own){
        signature = sig;
        time = commitTime;
        refactorType = type;
        if(type.equals(Operator.Split_Package)){
            deriver.add(oldCodeBlockTime);
            oldCodeBlockTime.getDerivee().add(this);
        }else if(type.equals(Operator.Merge_Package)){
            derivee.add(oldCodeBlockTime);
            oldCodeBlockTime.getDeriver().add(this);
        }
        owner = own;
        own.addHistory(this);
        commitTime.addCodeChange(this);
    }

    public PackageTime(PackageTime pkg, CommitCodeChange cmt, Operator tp) {//move class
        signature = pkg.getSignature();
        filePath = new ArrayList<>(pkg.getFilePath());
        time = cmt;
        refactorType = tp;
        deriver = pkg.getDeriver();
        derivee = pkg.getDerivee();
        parentCodeBlock = pkg.getParentCodeBlock();
        owner = pkg.getOwner();
        classes = pkg.getClasses();
        owner.addHistory(this);
        cmt.addCodeChange(this);
    }

}
