import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Model.PackageTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args){
//        Map<String, CodeBlock> test = new HashMap<>();
////        System.out.println(test.isEmpty());
//        CodeBlock a = new CodeBlock(1, CodeBlockType.Class);
//        CodeBlock b = new CodeBlock(2, CodeBlockType.Method);
//        test.put("tsst", b);
//        test.put("haha", a);

//        PackageTime p = new PackageTime("test", "test", new CommitCodeChange("0"), Operator.ADD_Package, new CodeBlock(1, CodeBlockType.Package));
//        PackageTime p2 = new PackageTime(p.getSignature(), p.getFilePath().get(0), p.getTime(), p.getRefactorType(), p.getOwner());
//        p2.getFilePath().add("haha");
//        p.getFilePath().add("7");
//        List<CodeBlock> classes = new ArrayList<>();
//        classes.add(new CodeBlock(2, CodeBlockType.Class));
//        p.setClasses(classes);
//        p2.setClasses(new ArrayList<>(p.getClasses()));
//        p2.getClasses().add(new CodeBlock(4, CodeBlockType.Class));
//        System.out.println(p2.getFilePath());
        String x = cutString("Split Package org.jboss.messaging.util.newprioritylinkedlist to [org.jboss.messaging.newcore, org.jboss.messaging.newcore.impl]", "[", "]");
        System.out.println(x);



//        System.out.println(a.toString().get);
//        test.put("ni", test.get("haha"));
//        Collection<CodeBlock> values = test.values();
//        System.out.println(values.size());
//        System.out.println(test.get("hg"));
//        System.out.println(test.values().);
    }

    public static String cutString(String str, String start, String end){
        Integer s = str.indexOf(start);
        Integer e = str.indexOf(end);
        return str.substring(s+1, e);
    }
}
