package Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassTime extends CodeBlockTime{
    List<CodeBlock> classes;
    List<CodeBlock> methods;
    List<CodeBlock> attributes;

}
