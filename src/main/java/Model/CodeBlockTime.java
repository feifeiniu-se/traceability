package Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CodeBlockTime {
    String signature;
    String filePath = null;
    String time;
    CodeBlockTime pre;
    CodeBlockTime post;
    String refactorType;
    List<CodeBlockTime> deriver = new ArrayList<>();//父
    List<CodeBlockTime> derivee = new ArrayList<>();//子
    CodeBlock parentCodeBlock;
    CodeBlock owner;


}
