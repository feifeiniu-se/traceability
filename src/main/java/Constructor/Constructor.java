package Constructor;

import Project.RefactoringMiner.Refactoring;
import Project.RefactoringMiner.Refactorings;
import Project.Utils.CommitHashCode;
import Project.Utils.DiffFile;
import lombok.Data;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Project.Project;
import Project.RefactoringMiner.Commits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Constructor {
    Project project;
    List<CodeBlock> codeBlocks = new ArrayList<>();
    List<CommitCodeChange> codeChange = new ArrayList<>();
    HashMap<String, CodeBlock> mapping = new HashMap<>();// mapping between signature and codeBlockID

    public Constructor(Project p) {
        project = p;
    }


    public void start(){
        List<CommitHashCode> commitList = project.getCommitList();
        for(CommitHashCode hashCode: commitList){
            HashMap<String, DiffFile> fileList =  project.getDiffList(hashCode);
            if(fileList==null){continue;}
            else{
                Refactorings refact = project.getRefactorings().get(hashCode.getHashCode());
                if(refact != null){//if the refactorings is not null,
                    if(!refact.getRefactorings().isEmpty()){
                        List<Refactoring> firstLevel = refact.filter("first");
                        System.out.println("H");
                    }
                }

            }

        }
    }


    //TODO 首先获取所有的commitList
    //TODO 然后遍历所有的commitList 进行处理
    //    TODO 对于每一个commit，先package->class层面的refactoring，确定文件rename， derive的对应关系
    //TODO 根据refactoring确定文件的对应关系
//    TODO 然后处理剩下的refactoring类型
//    TODO 遍历所有的文件，先derive, rename, modify, add, delete，在确定

}
