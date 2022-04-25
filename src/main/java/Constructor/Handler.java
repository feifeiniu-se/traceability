package Constructor;

import Model.CodeBlock;
import Project.RefactoringMiner.Refactoring;

import java.util.HashMap;

public class Handler {
    public void handle(HashMap<String, CodeBlock> codeBlocks, Refactoring refactor, String currentTime, String lastHash, Operator operator){
        operator.apply(codeBlocks, refactor, currentTime, lastHash);
    }
}
