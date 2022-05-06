package Constructor;

import Constructor.Enums.Operator;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Project.RefactoringMiner.Refactoring;

import java.util.HashMap;
import java.util.List;

public class Handler {
    public void handle(List<CodeBlock> codeBlocks, HashMap<String, CodeBlock> mappings, Refactoring r, CommitCodeChange commitTime, Operator operator, String name){
        operator.apply(codeBlocks, mappings, r, commitTime, name);
    }

}
