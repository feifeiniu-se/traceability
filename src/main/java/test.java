import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class test {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream content = new FileInputStream("C:\\Users\\Feifei\\code\\TraceabilityModel\\src\\main\\java\\test.txt");


        JavaParser javaParser = new JavaParser();
        Optional<CompilationUnit> x = javaParser.parse(content).getResult();
        CompilationUnit cu = x.get();
        Visitor visitor = new Visitor();
        visitor.visit(cu, null);


//        String pkgName = cu.getPackageDeclaration().get().getNameAsString();


        for (TypeDeclaration type : cu.getTypes()) {
            // first give all this java doc member
            String className = type.getNameAsString();
            System.out.println(className + " type");

            List<BodyDeclaration> members = type.getMembers();
            // check all member content： class, method, attribute
            classVisit(members, className);
        }

        System.out.println("OK2");
    }

    public static class Visitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(PackageDeclaration md, Void arg){
            super.visit(md, arg);
            String name = md.getNameAsString();
            System.out.println(name + " package");
        }

        @Override
        //class or interface
        public void visit(ClassOrInterfaceDeclaration md, Void arg) {
            super.visit(md, arg);
            if(md.isNestedType()){
                String name = md.getNameAsString();
                System.out.println(name + " nested name");
                return;
            }

            String name = md.getNameAsString();
            System.out.println(name + " visit");
//            System.out.println("OK");
        }
        public void visit(EnumDeclaration md, Void arg){
            super.visit(md, arg);
            String name = md.getNameAsString();
            if(md.isNestedType()){
                System.out.println("nested Enum");
            }
        }



    }

    public static void classVisit(List<BodyDeclaration> members, String signature) {
        for (BodyDeclaration member : members) {
            // if member state equal ClassOrInterfaceDeclaration, and you can identify it which is inner class
            if (member.isClassOrInterfaceDeclaration()) {
//                System.out.println("Inner class: "+member.asClassOrInterfaceDeclaration().getMethods().toString());
                String innerClassName = member.asClassOrInterfaceDeclaration().getNameAsString();
                String signature_inner_class = signature + "." + innerClassName;
                System.out.println(signature_inner_class + " inner");

                NodeList<BodyDeclaration<?>> innerMember = member.asClassOrInterfaceDeclaration().getMembers();
                List<BodyDeclaration> innerMembers = new ArrayList<>();
                for (int i = 0; i < innerMember.size(); i++) {
                    innerMembers.add(innerMember.get(i));
                }
                classVisit(innerMembers, signature_inner_class);
            }
            if(member.isEnumDeclaration()){
                String innerClassName = member.asEnumDeclaration().getNameAsString();
                System.out.println(signature+ "." + innerClassName + " enum");
            }
        }
    }

    public static void checkWeakReferences(WeakReference<?>... references){

    }
    public static String parse(String x) {
        String[] tmp = x.split(" : ");
        assert tmp.length == 2;
        String tmp2 = tmp[0].substring(tmp[0].indexOf(" ") + 1);

        System.out.println(x.lastIndexOf(":"));
        return tmp[1] + "_" + tmp2;
    }

    public static String cutString(String str, String start, String end) {
        Integer s = str.indexOf(start);
        Integer e = str.indexOf(end);
        return str.substring(s + 1, e);
    }

    public static String codeElement2Name(String codeElement) {
        String x = codeElement.substring(codeElement.lastIndexOf(".") - 1);
        return codeElement.replace(x, x.replace(".", ":"));
    }
}
