import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Constructor.Utils;
import Model.ClassTime;
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
//        String x = codeElement2Name("org.jboss.messaging.newcore");
        String x1 = "Move And Inline Method public read(is DataInputStream) : void moved from class org.jboss.jms.wireformat.CallbackSupport to class org.jboss.jms.message.JBossMessage & inlined to public read(in DataInputStream) : void";
        String x2 = "protected replicatorID : Serializable";
//        String res1 = parse(x1);
//        String res2 = parse(cutString(x1, "to class ", " & "));
        String[] tmp = x1.substring(0, x1.indexOf(" & ")).split(" ");
        System.out.println(tmp[tmp.length-1]);
//        Utils.codeElement2Name(x);



//        System.out.println(a.toString().get);
//        test.put("ni", test.get("haha"));
//        Collection<CodeBlock> values = test.values();
//        System.out.println(values.size());
//        System.out.println(test.get("hg"));
//        System.out.println(test.values().);
    }
    public static String parse(String x){
        String[] tmp = x.split(" : ");
        assert  tmp.length==2;
        String tmp2 = tmp[0].substring(tmp[0].indexOf(" ")+1);

        System.out.println(x.lastIndexOf(":"));
        return tmp[1]+"_"+tmp2;
    }
    public static String cutString(String str, String start, String end){
        Integer s = str.indexOf(start);
        Integer e = str.indexOf(end);
        return str.substring(s+1, e);
    }

    public static String codeElement2Name(String codeElement) {
        String x = codeElement.substring(codeElement.lastIndexOf(".")-1);
        return codeElement.replace(x, x.replace(".", ":"));
    }
}
