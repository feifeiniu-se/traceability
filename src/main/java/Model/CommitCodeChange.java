package Model;

import java.util.List;
import lombok.Data;

public class CommitCodeChange {
    String commitID;
    List<CodeBlockTime> codeChange;
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

    //    public String toString()
}
