package Model;

import java.util.List;
import lombok.Data;
@Data
public class CommitCodeChange {
    String commitID;
    List<CodeBlockTime> codeChange;
    CommitCodeChange preCommit;
    CommitCodeChange postCommit;
}
