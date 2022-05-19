package Persistence;

import Model.CommitCodeChange;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommitCodeChangeSaverTest {
    private final CommitCodeChangeSaver commitCodeChangeSaver= new CommitCodeChangeSaver("/Users/neowoodley/Postgraduate/ScientificResearch/CaseStudy/sqlite/data/traceability.sqlite3");

    @Test
    public void saveTest1() {
        CommitCodeChange commitCodeChange1 = new CommitCodeChange("commitId-1");
        CommitCodeChange commitCodeChange2 = new CommitCodeChange("commitId-2");
        CommitCodeChange commitCodeChange3 = new CommitCodeChange("commitId-3");
        commitCodeChange1.setPostCommit(commitCodeChange2);
        commitCodeChange2.setPreCommit(commitCodeChange1);
        commitCodeChange2.setPostCommit(commitCodeChange3);
        commitCodeChange3.setPreCommit(commitCodeChange2);
        List<CommitCodeChange> commitCodeChanges = new ArrayList<>(Arrays.asList(commitCodeChange1, commitCodeChange2, commitCodeChange3));

        commitCodeChangeSaver.save(commitCodeChanges);
        commitCodeChangeSaver.query();
    }
}