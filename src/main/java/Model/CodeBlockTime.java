package Model;

import lombok.Data;

@Data
public class CodeBlockTime {
    String signature;
    String filePath = null;
    String time;
    CodeBlockTime pre;
    CodeBlockTime post;
    String refactorType;
    CodeBlockTime deriver = null;//父
    CodeBlockTime derivee = null;//子
    CodeBlock parent;

}
