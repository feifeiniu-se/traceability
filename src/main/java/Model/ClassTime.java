package Model;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            try{
                ClassTime parentTime = (ClassTime) parent.getLastHistory().deepCopy();
                parentTime.setTime(cmt);
                parentTime.setRefactorType(Operator.Add_Class);
                parentTime.getClasses().add(own);
                parent.addHistory(parentTime);
                cmt.addCodeChange(parentTime);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else if (parent.getType().equals(CodeBlockType.Package)) {// if the parent is a package
            try{
                PackageTime parentTime = (PackageTime) parent.getLastHistory().deepCopy();
                parentTime.setTime(cmt);
                parentTime.setRefactorType(Operator.Add_Class);
                parentTime.getClasses().add(own);
                parent.addHistory(parentTime);
                cmt.addCodeChange(parentTime);
            }catch (JsonProcessingException e){
                e.printStackTrace();
            }
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

    public List<String> getFilePath() {
        return null;
    }

    @Override
    List<CodeBlock> getPackages() {
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
        ObjectMapper objectMapper = new ObjectMapper();
        ClassTime res = objectMapper.readValue(objectMapper.writeValueAsString(this), ClassTime.class);
        return res;
    }
}
