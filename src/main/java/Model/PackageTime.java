package Model;
import Constructor.Enums.Operator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PackageTime extends CodeBlockTime{
    List<CodeBlock> classes = new ArrayList<>();

    public PackageTime(String name, CommitCodeChange commitTime, Operator type, CodeBlock own){// add new package
        this.name = name;
        time = commitTime;
        refactorType = type;
        owner = own;
        own.addHistory(this);
        commitTime.addCodeChange(this);
    }

    public PackageTime deepCopy() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PackageTime res = objectMapper.readValue(objectMapper.writeValueAsString(this), PackageTime.class);
        return res;
    }

//    public PackageTime(String name, CommitCodeChange commitTime, Operator type, CodeBlockTime oldCodeBlockTime, CodeBlock own){
//        this.name = name;
//        time = commitTime;
//        refactorType = type;
//        if(type.equals(Operator.Split_Package)){
//            deriver.add(oldCodeBlockTime);
//            oldCodeBlockTime.getDerivee().add(this);
//        }else if(type.equals(Operator.Merge_Package)){
//            derivee.add(oldCodeBlockTime);
//            oldCodeBlockTime.getDeriver().add(this);
//        }
//        owner = own;
//        own.addHistory(this);
//        commitTime.addCodeChange(this);
//    }
//
//    public PackageTime(PackageTime pkg, CommitCodeChange cmt, Operator tp) {//move class
//        name = pkg.getName();
//        time = cmt;
//        refactorType = tp;
//        deriver = pkg.getDeriver();
//        derivee = pkg.getDerivee();
//        parentCodeBlock = pkg.getParentCodeBlock();
//        owner = pkg.getOwner();
//        classes = pkg.getClasses();
//        owner.addHistory(this);
//        cmt.addCodeChange(this);
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
}
