package Project.RefactoringMiner;

import lombok.Data;

import java.util.List;

@Data
public class SideLocation {
    private String filePath;
    private int startLine;
    private int endLine;
    private int startColumn;
    private int endColumn;
    private String codeElementType;
    private String description;
    private String codeElement;

    private List<String> codeLines;  // [startLine, endLine] 对应的源代码
}
