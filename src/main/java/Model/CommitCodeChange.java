package Model;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 纵向关系（时间，不同commit之间）
 */
@ToString
public class CommitCodeChange {
    String commitID;  // 对应于commit的hash值
    List<CodeBlockTime> codeChange = new ArrayList<>();
    @ToString.Exclude
    CommitCodeChange postCommit;
    @ToString.Exclude
    CommitCodeChange preCommit;

    public CommitCodeChange(String id) {
        commitID = id;
    }

    public void setPreCommit(CommitCodeChange preCommit) {
        this.preCommit = preCommit;
    }

    public void setPostCommit(CommitCodeChange postCommit) {
        this.postCommit = postCommit;
    }

    public String getCommitID() {
        return commitID;
    }

    public void addCodeChange(CodeBlockTime cbt) {
        codeChange.add(cbt);
    }

    public CommitCodeChange getPreCommit() {
        return this.preCommit;
    }

    public CommitCodeChange getPostCommit() {
        return this.postCommit;
    }
}
