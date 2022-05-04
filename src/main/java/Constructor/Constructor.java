package Constructor;

import Constructor.Enums.Operator;
import Constructor.Visitors.ClassVisitor;
import Constructor.Visitors.MethodAAttributeVisitor;
import Constructor.Visitors.PackageVisitor;
import Project.RefactoringMiner.Refactoring;
import Project.RefactoringMiner.Refactorings;
import Project.Utils.CommitHashCode;
import Project.Utils.DiffFile;
import lombok.Data;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Project.Project;

import java.util.*;


@Data
public class Constructor {
    Project project;
    List<CodeBlock> codeBlocks = new ArrayList<>();
    List<CommitCodeChange> codeChange = new ArrayList<>();
    HashMap<String, CodeBlock> mappings = new HashMap<>();// mapping between signature and codeBlockID

    public Constructor(Project p) {
        project = p;
    }


    public void start(){
        List<CommitHashCode> commitList = project.getCommitList();
        for(CommitHashCode hashCode: commitList){
            //对每一次commit新建一个commitTime 用于存储本次的变更，如果没有变更，则为空，并且更新pre， post
            CommitCodeChange commitTime = new CommitCodeChange(hashCode.getHashCode());
            if(codeChange.size()>0){
                commitTime.setPreCommit(codeChange.get(codeChange.size()-1));
                codeChange.get(codeChange.size()-1).setPostCommit(commitTime);
            }else{
                commitTime.setPreCommit(null);
            }
            codeChange.add(commitTime);

            HashMap<String, DiffFile> fileList =  project.getDiffList(hashCode);
            if(fileList==null){continue;}//no file changes during this commit

            //firstly, go through all refactorings; and then all changed files
            Refactorings refact = project.getRefactorings().get(hashCode.getHashCode());
            if (refact != null) {//if refactoring is not null, separate them into three levels: package, class, method&attribute
                if (!refact.getRefactorings().isEmpty()) {
                    //package level
                    List<Refactoring> firstLevel = refact.filter("first");
                    if (!firstLevel.isEmpty()) {
                        handlingPackage(firstLevel, commitTime);
                    }
                    // class level
                    List<Refactoring> secondLevel = refact.filter("second");
                    if (!secondLevel.isEmpty()) {
                        handlingMethod(secondLevel, commitTime);
                    }
                    //method & attribute level
                    List<Refactoring> thirdLevel = refact.filter("third");
                    if (!thirdLevel.isEmpty()) {
                        handlingMethodAndAttribute(thirdLevel, commitTime);
                    }
                }
            }
            //after refactorings, go through all the changed files
            handlingFiles(fileList);

        }
    }

    private void handlingFiles(HashMap<String, DiffFile> fileList) {
        //todo
        if(!fileList.isEmpty()){
            //firstly, package level
            for(Map.Entry<String, DiffFile> file: fileList.entrySet()){
                String fileContent = file.getValue().getContent();
                PackageVisitor pkgVisitor = new PackageVisitor();
                pkgVisitor.packageVisitor(fileContent, codeBlocks, codeChange, mappings);
            }
            //secondly, class level
            for(Map.Entry<String, DiffFile> file: fileList.entrySet()){
                String fileContent = file.getValue().getContent();
                String filePath = file.getValue().getPath();
                ClassVisitor classVisitor = new ClassVisitor();
                classVisitor.classVisitor(fileContent, codeBlocks, codeChange, mappings);

            }
            //thirdly, inner class, method, and attribute level
            for(Map.Entry<String, DiffFile> file: fileList.entrySet()){
                String fileContent = file.getValue().getContent();
                String filePath = file.getValue().getPath();
                MethodAAttributeVisitor third = new MethodAAttributeVisitor();//包含inner class, method, attribute
                third.methodAAttributeVisitor(fileContent, codeBlocks, codeChange, mappings);
            }

        }
    }


//package level
    private void handlingPackage(List<Refactoring> firstLevel, CommitCodeChange commitTime) {
//        "Rename Package",
//        "Move Package",
//        "Split Package",
//        "Merge Package"
        for(Refactoring r: firstLevel){
            Handler handler = new Handler();
            handler.handle(codeBlocks, mappings, r, commitTime, Operator.valueOf(r.getType().replace(" ", "_")), null);
        }

    }

    //class level
    private void handlingMethod(List<Refactoring> secondLevel, CommitCodeChange commitTime){
//        "Move Class",//done
//        "Rename Class",//done
//        "Move and Rename Class",//done
//        "Merge Class"//done
//        "Extract Superclass",/done
//        "Extract Interface",//done
//        "Extract Class",//done
//        "Extract Subclass",//done
//        "Change Type Declaration Kind",//todo have to check if neccessary
//        "Collapse Hierarchy",//done

        for(Refactoring r: secondLevel){
            Handler handler = new Handler();
            handler.handle(codeBlocks, mappings, r, commitTime, Operator.valueOf(r.getType().replace(" ", "_")), null);
        }
    }


    //method & attribute level
    private void handlingMethodAndAttribute(List<Refactoring> thirdLevel, CommitCodeChange commitTime){
//        "Extract Method",
//        "Inline Method",
//        "Rename Method",//done
//        "Move Method",
//        "Pull Up Method",
//        "Push Down Method",
//        "Extract and Move Method",
//        "Move and Rename Method",
//        "Move and Inline Method",
//        "Merge Parameter",
//        "Split Parameter",
//        "Add Parameter",
//        "Remove Parameter",
//        "Reorder Parameter",
//        "Change Parameter Type",
//        "Parameterize Attribute",
//        "Parameterize Variable",
//        "Change Return Type",
//        "Rename Attribute",//done
//        "Move and Rename Attribute",
//        "Merge Attribute",
//        "Split Attribute",
//        "Change Attribute Type",
//        "Extract Attribute",
//        "Encapsulate Attribute",
//        "Replace Attribute with Variable",
//        "Inline Attribute"
//        "Pull Up Attribute",
//        "Push Down Attribute",

        for(Refactoring r: thirdLevel){
            Handler handler = new Handler();
            handler.handle(codeBlocks, mappings, r, commitTime, Operator.valueOf(r.getType().replace(" ", "_")), null);
        }
    }


    //TODO 首先获取所有的commitList
    //TODO 然后遍历所有的commitList
        //todo for each commit, fitstly go through all the refactorings
        //todo then go through all the files
        

}
