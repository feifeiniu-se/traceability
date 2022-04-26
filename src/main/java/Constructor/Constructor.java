package Constructor;

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

            handleRenameFile(fileList);
            handleDriveFile(fileList);
            handleModifyFile(fileList);
            handleAddFile(fileList);
            handleDeleteFile(fileList);

        }
    }

    private void handleModifyFile(HashMap<String, DiffFile> fileList) {
        List<Map.Entry<String, DiffFile>> modifyFile = fileList.entrySet().stream().filter(map -> "MODIFY".equals(map.getValue().getType())).collect(Collectors.toList());
        if(!modifyFile.isEmpty()){
            //todo
        }

    }

    private void handleDeleteFile(HashMap<String, DiffFile> fileList) {
        List<Map.Entry<String, DiffFile>> deleteFile = fileList.entrySet().stream().filter(map -> "DELETE".equals(map.getValue().getType())).collect(Collectors.toList());
        //todo
        if(!deleteFile.isEmpty()){

        }
    }

    private void handleAddFile(HashMap<String, DiffFile> fileList) {
        List<Map.Entry<String, DiffFile>> addFile = fileList.entrySet().stream().filter(map -> "ADD".equals(map.getValue().getType())).collect(Collectors.toList());
        //todo
        if(!addFile.isEmpty()){
            //firstly, package
            for(Map.Entry<String, DiffFile> map: fileList.entrySet()){
                String fileContent = map.getValue().getContent();
                String filePath = map.getValue().getPath();
                packageVisitor(filePath, fileContent, codeBlocks, codeChange, mappings);

            }

        }
    }



    private void handleDriveFile(HashMap<String, DiffFile> fileList) {
        List<Map.Entry<String, DiffFile>> deriveFile = fileList.entrySet().stream().filter(map -> "DERIVE".equals(map.getValue().getType())).collect(Collectors.toList());
        //todo
        if(!deriveFile.isEmpty()){

        }
    }

    private void handleRenameFile(HashMap<String, DiffFile> fileList) {
        List<Map.Entry<String, DiffFile>> renameFile = fileList.entrySet().stream().filter(map -> "RENAME".equals(map.getValue().getType())).collect(Collectors.toList());
        //todo
        if(!renameFile.isEmpty()){

        }
    }



    private void handlingFirst(HashMap<String, DiffFile> fileList, List<Refactoring> firstLevel, CommitCodeChange commitTime, Set<DiffFile> filePairs) {
//        "Rename Package",
//                "Move Package",
//                "Split Package",
//                "Merge Package"
//        for(Refactoring r: firstLevel){
//            System.out.println(r);
//        }
        //todo
        System.out.println("1: " + commitTime.getCommitID());
    }
    private void handlingSecond(HashMap<String, DiffFile> fileList, List<Refactoring> secondLevel, CommitCodeChange commitTime, Set<DiffFile> filePairs){
        //todo
        System.out.println("2: " + commitTime.getCommitID());
    }
    private void handlingThird(HashMap<String, DiffFile> fileList, List<Refactoring> thirdLevel, CommitCodeChange commitTime, Set<DiffFile> filePairs){
        //todo
        System.out.println("3: " + commitTime.getCommitID());

    }


    //TODO 首先获取所有的commitList
    //TODO 然后遍历所有的commitList 进行处理
    //    TODO 对于每一个commit，先package->class层面的refactoring，确定文件rename， derive的对应关系
    //TODO 根据refactoring确定文件的对应关系
//    TODO 然后处理剩下的refactoring类型
//    TODO 遍历所有的文件，先derive, rename, modify, add, delete，在确定

}
