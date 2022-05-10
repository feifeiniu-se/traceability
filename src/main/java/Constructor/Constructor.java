package Constructor;

import Constructor.Enums.FileType;
import Constructor.Enums.Operator;
import Constructor.Visitors.ClassVisitor;
import Constructor.Visitors.MethodAndAttributeVisitor;
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
import java.util.stream.Collectors;

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
            System.out.println(codeBlocks.size());
            System.out.println(mappings.size());
            System.out.println("Commit: "+hashCode.getHashCode());
            //add a new commitTime for each commit, for the code change during this commit
            CommitCodeChange commitTime = new CommitCodeChange(hashCode.getHashCode());
            if(codeChange.size()>0){
                commitTime.setPreCommit(codeChange.get(codeChange.size()-1));
                codeChange.get(codeChange.size()-1).setPostCommit(commitTime);
            }else{
                commitTime.setPreCommit(null);
            }
            codeChange.add(commitTime);

            //go through all the files and refactorings
            HashMap<String, DiffFile> fileList1 =  project.getDiffList(hashCode);
            if(fileList1==null){continue;}//no file changes during this commit
            Map<String, DiffFile> fileList = fileList1.entrySet().stream()
                    .filter(p -> FileType.ADD.equals(p.getValue().getType())|| FileType.MODIFY.equals(p.getValue().getType()))
                    .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

            //if refactoring is not null, separate them into three levels: package, class, method&attribute
            Refactorings refact = project.getRefactorings().get(hashCode.getHashCode());

            //packageLevel: firstly refactorings, then javaParser visitor
            if (refact != null && commitTime.getPreCommit() != null) {
                if (!refact.getRefactorings().isEmpty()) {
                    List<Refactoring> packageLevelRefactorings = refact.filter("package");
                    if (!packageLevelRefactorings.isEmpty()) {
                        for(Refactoring r: packageLevelRefactorings){
                            Operator.valueOf(r.getType().replace(" ", "_")).apply(codeBlocks, mappings, r, commitTime, null);
                        }
                    }
                }
            }
            if(!fileList.isEmpty()) {
                for (Map.Entry<String, DiffFile> file : fileList.entrySet()) {
                    String fileContent = file.getValue().getContent();
                    PackageVisitor pkgVisitor = new PackageVisitor();
                    pkgVisitor.packageVisitor(fileContent, codeBlocks, codeChange, mappings);
                }
            }

            //classLevel; firstly refactorings, then javaparser visitor
            if (refact != null && commitTime.getPreCommit() != null) {
                if (!refact.getRefactorings().isEmpty()) {
                    // class level
                    List<Refactoring> classLevelRefactorings = refact.filter("class");
                    if (!classLevelRefactorings.isEmpty()) {
                        for(Refactoring r: classLevelRefactorings){
                            Operator.valueOf(r.getType().replace(" ", "_")).apply(codeBlocks, mappings, r, commitTime, null);
                        }
                    }
                }
            }
            if(!fileList.isEmpty()) {
                for (Map.Entry<String, DiffFile> file : fileList.entrySet()) {
                    String fileContent = file.getValue().getContent();
                    ClassVisitor classVisitor = new ClassVisitor();
                    classVisitor.classVisitor(fileContent, codeBlocks, codeChange, mappings);
                }
            }

            //method and attribute level: firstly refactoring, then javaparser visitor
            if (refact != null && commitTime.getPreCommit() != null) {
                if (!refact.getRefactorings().isEmpty()) {
                    //method & attribute
                    List<Refactoring> methodAndAttributeLevelRefactorings = refact.filter("methodAndAttribute");
                    if (!methodAndAttributeLevelRefactorings.isEmpty()) {
                        for(Refactoring r: methodAndAttributeLevelRefactorings){
                            Operator.valueOf(r.getType().replace(" ", "_")).apply(codeBlocks, mappings, r, commitTime, null);
                        }
                    }
                    //parameters & return type
                    List<Refactoring> parameterLevelRefactorings = refact.filter("parameter");
                    if (!parameterLevelRefactorings.isEmpty()) {
                        for(Refactoring r: parameterLevelRefactorings){
                            Operator.valueOf(r.getType().replace(" ", "_")).apply(codeBlocks, mappings, r, commitTime, null);
                        }
                    }

                }
            }
            if(!fileList.isEmpty()){
                for(Map.Entry<String, DiffFile> file: fileList.entrySet()){
                    String fileContent = file.getValue().getContent();
                    MethodAndAttributeVisitor methodAndAttributeVisitor = new MethodAndAttributeVisitor();//including inner class, method, attribute
                    methodAndAttributeVisitor.methodAAttributeVisitor(fileContent, codeBlocks, codeChange, mappings);
                }
            }
        }
    }

//    private void handlingFiles(HashMap<String, DiffFile> fileList1) {
//        Map<String, DiffFile> fileList = fileList1.entrySet().stream()
//                .filter(p -> FileType.ADD.equals(p.getValue().getType())|| FileType.MODIFY.equals(p.getValue().getType()))
//                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
//
//        if(!fileList.isEmpty()){
//            //firstly, package level
//            for(Map.Entry<String, DiffFile> file: fileList.entrySet()){
//                String fileContent = file.getValue().getContent();
//                PackageVisitor pkgVisitor = new PackageVisitor();
//                pkgVisitor.packageVisitor(fileContent, codeBlocks, codeChange, mappings);
//            }
//            //secondly, class level
//            for(Map.Entry<String, DiffFile> file: fileList.entrySet()){
//                String fileContent = file.getValue().getContent();
//                ClassVisitor classVisitor = new ClassVisitor();
//                classVisitor.classVisitor(fileContent, codeBlocks, codeChange, mappings);
//
//            }
//            //thirdly, inner class, method, and attribute level
//            for(Map.Entry<String, DiffFile> file: fileList.entrySet()){
//                String fileContent = file.getValue().getContent();
//                MethodAAttributeVisitor third = new MethodAAttributeVisitor();//包含inner class, method, attribute
//                third.methodAAttributeVisitor(fileContent, codeBlocks, codeChange, mappings);
//            }
////            System.out.println(mappings.size());
//        }
//    }


//package level
//    private void handlingPackage(List<Refactoring> firstLevel, CommitCodeChange commitTime) {
////        "Rename Package",
////        "Move Package",
////        "Split Package",
////        "Merge Package"
//        for(Refactoring r: firstLevel){
//            Operator.valueOf(r.getType().replace(" ", "_")).apply(codeBlocks, mappings, r, commitTime, null);
//        }
//
//    }

    //class level
//    private void handleMinerRefactoringClassLevel(List<Refactoring> secondLevel, CommitCodeChange commitTime){
////        "Move Class",//
////        "Rename Class",//
////        "Move and Rename Class",//
////        "Merge Class"//
////        "Extract Superclass",/
////        "Extract Interface",//
////        "Extract Class",//
////        "Extract Subclass",//
////        "Change Type Declaration Kind",
////        "Collapse Hierarchy",//
//
//        for(Refactoring r: secondLevel){
//            Operator.valueOf(r.getType().replace(" ", "_")).apply(codeBlocks, mappings, r, commitTime, null);
//            //Handler handler = new Handler();
//            //handler.handle(codeBlocks, mappings, r, commitTime, , null);
//        }
//    }



//    //method & attribute level
//    private void handlingMethodAndAttribute(List<Refactoring> thirdLevel, CommitCodeChange commitTime){
////        "Extract Method",
////        "Inline Method",
////        "Rename Method",//done
////        "Move Method",
////        "Pull Up Method",
////        "Push Down Method",
////        "Extract and Move Method",
////        "Move and Rename Method",
////        "Move and Inline Method",
////        "Merge Parameter",
////        "Split Parameter",
////        "Add Parameter",
////        "Remove Parameter",
////        "Reorder Parameter",
////        "Change Parameter Type",
////        "Parameterize Attribute",
////        "Parameterize Variable",
////        "Change Return Type",
////        "Rename Attribute",//done
////        "Move and Rename Attribute",
////        "Merge Attribute",
////        "Split Attribute",
////        "Change Attribute Type",
////        "Extract Attribute",
////        "Encapsulate Attribute",
////        "Replace Attribute with Variable",
////        "Inline Attribute"
////        "Pull Up Attribute",
////        "Push Down Attribute",
//
//        for(Refactoring r: thirdLevel){
//            Handler handler = new Handler();
//            handler.handle(codeBlocks, mappings, r, commitTime, Operator.valueOf(r.getType().replace(" ", "_")), null);
//        }
//    }
        

}
