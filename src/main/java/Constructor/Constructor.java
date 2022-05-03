package Constructor;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.FileType;
import Constructor.Enums.Operator;
import Constructor.Enums.Status;
import Constructor.Visitors.ClassVisitor;
import Constructor.Visitors.MethodAAttributeVisitor;
import Constructor.Visitors.PackageVisitor;
import Model.ClassTime;
import Model.PackageTime;
import Project.RefactoringMiner.Refactoring;
import Project.RefactoringMiner.Refactorings;
import Project.RefactoringMiner.SideLocation;
import Project.Utils.CommitHashCode;
import Project.Utils.DiffFile;
import com.fasterxml.jackson.databind.JsonSerializable;
import lombok.Data;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Project.Project;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.util.*;
import java.util.stream.Collectors;

import static Constructor.Utils.codeElement2Name;

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

            //遍历此次本次变更，如果没有refactoring 就直接是文件增删，如果有refactoring
            HashMap<String, DiffFile> fileList =  project.getDiffList(hashCode);
            if(fileList==null){continue;}//如果本次没有java文件改变，就跳过本次循环

            Set<DiffFile> filePairs = new HashSet<>();//用来存储待处理的所有文件对
            //本次有文件改变，先判断是否有refactoring
            Refactorings refact = project.getRefactorings().get(hashCode.getHashCode());
            if (refact != null) {//如果refactoring不是空，则分别获取first，second，third level
                if (!refact.getRefactorings().isEmpty()) {
                    List<Refactoring> firstLevel = refact.filter("first");
                    if (!firstLevel.isEmpty()) {
                        System.out.println("before filter: "+fileList.size());
                        handlingFirst(fileList, firstLevel, commitTime, filePairs);
                    }

                    List<Refactoring> secondLevel = refact.filter("second");
                    if (!secondLevel.isEmpty()) {
                        handlingSecond(fileList, secondLevel, commitTime, filePairs);
                    }
                    List<Refactoring> thirdLevel = refact.filter("third");
                    if (!thirdLevel.isEmpty()) {
                        handlingThird(fileList, thirdLevel, commitTime, filePairs);
                    }
                    System.out.println("H");
                }
            }
            //处理完重构， 需要对所有的文件进行遍历
            //先处理rename的，然后derive，然后add, 然后modify，最后delete
            //需要先把value重复的去掉
            HashSet<DiffFile> s = new HashSet();
            for(Map.Entry<String, DiffFile> d:fileList.entrySet()){
                s.add(d.getValue());
            }
            System.out.println("after filter: "+s.size());

            handleRenameFile(fileList);
            handleDriveFile(fileList);
            handleModifyFile(fileList);
            handleAddFile(fileList);
            handleDeleteFile(fileList);

        }
    }

    private void handleModifyFile(HashMap<String, DiffFile> fileList) {
//        System.out.println(fileList.size());
        Map<String, DiffFile> modifyFile = fileList.entrySet().stream()
                .filter(p -> FileType.MODIFY.toString().equals(p.getValue().getType()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
//        System.out.println(modifyFile.size());
        if(!modifyFile.isEmpty()){
            for(Map.Entry<String, DiffFile> file: modifyFile.entrySet()){

            }
            //todo
        }

    }

    private void handleDeleteFile(HashMap<String, DiffFile> fileList) {
        Map<String, DiffFile> deleteFile = fileList.entrySet().stream()
                .filter(p -> FileType.DELETE.toString().equals(p.getValue().getType()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        //todo
        if(!deleteFile.isEmpty()){
            for(Map.Entry<String, DiffFile> file: deleteFile.entrySet()){

            }

        }
    }

    private void handleAddFile(HashMap<String, DiffFile> fileList) {
        Map<String, DiffFile> addFile = fileList.entrySet().stream()
                .filter(p -> FileType.ADD.equals(p.getValue().getType()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        //todo
        if(!addFile.isEmpty()){
            //firstly, package level
            for(Map.Entry<String, DiffFile> file: addFile.entrySet()){
                String fileContent = file.getValue().getContent();
                String filePath = file.getValue().getPath();
                PackageVisitor pkgVisitor = new PackageVisitor();
                pkgVisitor.packageVisitor(filePath, fileContent, codeBlocks, codeChange, mappings);
            }
            //secondly, class level
            for(Map.Entry<String, DiffFile> file: addFile.entrySet()){
                String fileContent = file.getValue().getContent();
                String filePath = file.getValue().getPath();
                ClassVisitor classVisitor = new ClassVisitor();
                classVisitor.classVisitor(filePath, fileContent, codeBlocks, codeChange, mappings);

            }
            //thirdly, inner class, method, and attribute level
            for(Map.Entry<String, DiffFile> file: addFile.entrySet()){
                String fileContent = file.getValue().getContent();
                String filePath = file.getValue().getPath();
                MethodAAttributeVisitor third = new MethodAAttributeVisitor();//包含inner class, method, attribute
                third.methodAAttributeVisitor(filePath, fileContent, codeBlocks, codeChange, mappings);
            }

        }
    }



    private void handleDriveFile(HashMap<String, DiffFile> fileList) {
        Map<String, DiffFile> deriveFile = fileList.entrySet().stream()
                .filter(p -> FileType.DERIVE.toString().equals(p.getValue().getType()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        //todo
        if(!deriveFile.isEmpty()){
            for(Map.Entry<String, DiffFile> file: deriveFile.entrySet()){
//                System.out.println(file.getValue().getType());
//                System.out.println(file.getValue().getType().equals("ADD"));

            }

        }
    }

    private void handleRenameFile(HashMap<String, DiffFile> fileList) {
        Map<String, DiffFile> renameFile = fileList.entrySet().stream()
                .filter(p -> FileType.RENAME.toString().equals(p.getValue().getType()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        //todo
        if(!renameFile.isEmpty()){
            for(Map.Entry<String, DiffFile> file: renameFile.entrySet()){

            }
        }
    }


//first level 主要处理包，文件重命名
    private void handlingFirst(HashMap<String, DiffFile> fileList, List<Refactoring> firstLevel, CommitCodeChange commitTime, Set<DiffFile> filePairs) {
//        "Rename Package",
//        "Move Package",
//        "Split Package",
//        "Merge Package"
        for(Refactoring r: firstLevel){
            Handler handler = new Handler();
            handler.handle(codeBlocks, mappings, r, fileList, commitTime, Operator.valueOf(r.getType().replace(" ", "_")));
        }

    }

    private void handlingSecond(HashMap<String, DiffFile> fileList, List<Refactoring> secondLevel, CommitCodeChange commitTime, Set<DiffFile> filePairs){
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
            handler.handle(codeBlocks, mappings, r, fileList, commitTime, Operator.valueOf(r.getType().replace(" ", "_")));
        }
    }


    private void handlingThird(HashMap<String, DiffFile> fileList, List<Refactoring> thirdLevel, CommitCodeChange commitTime, Set<DiffFile> filePairs){
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
//        "Rename Attribute",
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
            handler.handle(codeBlocks, mappings, r, fileList, commitTime, Operator.valueOf(r.getType().replace(" ", "_")));
        }
    }


    //TODO 首先获取所有的commitList
    //TODO 然后遍历所有的commitList 进行处理
    //    TODO 对于每一个commit，先package->class层面的refactoring，确定文件rename， derive的对应关系
    //TODO 根据refactoring确定文件的对应关系
//    TODO 然后处理剩下的refactoring类型
//    TODO 遍历所有的文件，先derive, rename, modify, add, delete，在确定

}
