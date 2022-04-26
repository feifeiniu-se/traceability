package Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CodeBlockTime {
    String signature;
    List<String> filePath = new ArrayList<>();
    CommitCodeChange time;
    CodeBlockTime pre;
    CodeBlockTime post;
    String refactorType;
    List<CodeBlockTime> deriver = new ArrayList<>();//父
    List<CodeBlockTime> derivee = new ArrayList<>();//子
    CodeBlock parentCodeBlock;
    CodeBlock owner;


}
