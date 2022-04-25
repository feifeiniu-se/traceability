import Model.CodeBlock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args){
        Map<String, CodeBlock> test = new HashMap<>();
        CodeBlock a = new CodeBlock();
        CodeBlock b = new CodeBlock();
        test.put("tsst", b);
        test.put("haha", a);
        test.put("ni", test.get("haha"));
        Collection<CodeBlock> values = test.values();
        System.out.println(values.size());
        System.out.println(test.get("hg"));
    }
}
