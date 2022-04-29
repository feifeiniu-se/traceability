package Constructor;

import Constructor.Enums.Operator;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Project.RefactoringMiner.Refactoring;
import Project.Utils.DiffFile;

import java.util.HashMap;
import java.util.List;

public class Handler {
    public void handle(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, HashMap<String, DiffFile> fileList, CommitCodeChange commitTime, Operator operator){
        operator.apply(codeBlocks, mappings, r, fileList, commitTime);
    }
}
