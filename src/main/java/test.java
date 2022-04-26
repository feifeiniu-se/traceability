import Model.CodeBlock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args){
        Map<String, CodeBlock> test = new HashMap<>();
//        System.out.println(test.isEmpty());
        CodeBlock a = new CodeBlock(1);
        CodeBlock b = new CodeBlock(2);
        test.put("tsst", b);
        test.put("haha", a);

//        test.put("ni", test.get("haha"));
//        Collection<CodeBlock> values = test.values();
//        System.out.println(values.size());
//        System.out.println(test.get("hg"));
//        System.out.println(test.values().);
    }
}
