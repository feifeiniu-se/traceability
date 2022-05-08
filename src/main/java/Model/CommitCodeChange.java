package Model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

public class CommitCodeChange {
    String commitID;
    List<CodeBlockTime> codeChange = new ArrayList<>();
    CommitCodeChange preCommit;
    CommitCodeChange postCommit;

    public CommitCodeChange(String id){
        commitID = id;
    }

    public void setPreCommit(CommitCodeChange preCommit) {
        this.preCommit = preCommit;
    }
    public void setPostCommit(CommitCodeChange postCommit){
        this.postCommit = postCommit;
    }
    public String getCommitID(){
        return commitID;
    }
    public void addCodeChange(CodeBlockTime cbt){
        codeChange.add(cbt);
    }
    public CommitCodeChange getPreCommit(){return this.preCommit;}
    public CommitCodeChange getPostCommit(){return this.postCommit;}

    //    public String toString()
}
