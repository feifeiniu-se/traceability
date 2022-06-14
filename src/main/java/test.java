import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.*;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Constructor.Visitors.ClassVisitor;
import Constructor.Visitors.MethodAndAttributeVisitor;
import Model.ClassTime;
import Model.CodeBlock;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import static Constructor.Utils.*;
import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;


public class test {
    static boolean flag = false;
    static String pkgName;
    public static void main(String[] args) throws FileNotFoundException {
        test1(1);

        FileInputStream content = new FileInputStream("C:\\Users\\Feifei\\code\\TraceabilityModel\\src\\main\\java\\test.txt");
//
//        JavaParser javaParser = new JavaParser();
//        Optional<CompilationUnit> tmp = javaParser.parse(content).getResult();
//        if(!tmp.isPresent()){
//            return;
//        }
//        CompilationUnit cu = tmp.get();
//        if(cu.getPackageDeclaration().isPresent()){
//            pkgName = cu.getPackageDeclaration().get().getNameAsString();
//        }else{
//            pkgName = "default.package";
//        }
//        System.out.println(pkgName);
//        Visitor visitor = new Visitor();
//        visitor.visit(cu, null);// 遍历完文件的AST树，初步获得信息
//
//        //前边是获得了第一层类的信息，接下来要遍历一下内部类
//        if(flag){
//            for (TypeDeclaration type : cu.getTypes()) {
//                // first give all this java doc member
//                String className = type.getNameAsString();
//                String signature_class = pkgName + "." + className;
////                System.out.println(signature_class);
//                List<BodyDeclaration> members = type.getMembers();
//                // check all member content： class, method, attribute
//                nestedClassVisit(members, signature_class, null);
//            }
//        }

//        System.out.println("OK2");
    }

    public static class Visitor extends VoidVisitorAdapter<Void> {

        @Override
        //class or interface
        public void visit(ClassOrInterfaceDeclaration md, Void arg) {
            super.visit(md, arg);
            if (md.isNestedType() == true) {
                flag = true;
                return;
            }//如果是nested class就返回
            String name = md.getNameAsString();
            String signature = pkgName + "." + name;
//            System.out.println(signature);
//            System.out.println("HAHA");
            Optional<String> y = md.getFullyQualifiedName();
//            System.out.println(y);




        }

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            System.out.println(md.getSignature());
            String methodName = "";
            String returnType = md.getTypeAsString();
            returnType = returnType.substring(returnType.lastIndexOf(".")+1);
            String name = md.getNameAsString();
            name = name.substring(name.lastIndexOf(".")+1);
            methodName = returnType+"_"+name;
            String paramaters = "";
            for(int i=0; i<md.getParameters().size(); i++){
                String paramType = md.getParameter(i).getType().toString();
                paramType = paramType.substring(paramType.lastIndexOf(".")+1);
                paramaters = paramaters + ", " + paramType;
            }
            paramaters = paramaters.length()>0?paramaters.substring(2):paramaters;
            methodName = methodName + "(" + paramaters + ")";

            System.out.println(methodName);


//            System.out.println(md.getNameAsString());
//            System.out.println(md.getType()+"_"+md.getSignature());
//            System.out.println("OK");
            if((md.getType()+"_"+md.getSignature()).contains("ignore")){
//                System.out.println(md.getSignature());
//                System.out.println(md.getType());
                String x =  md.getTypeAsString();
                System.out.println(x);
//                System.out.println(md.getType()+"_"+md.getSignature());
            }


//            System.out.println(md.getParentNodeForChildren());
//            System.out.println(md.getParentNode());
//            System.out.println(md.getParentNode().get().getDeclaringClass());

//            md.getParentNode().ifPresent((l)->System.out.println(l));


        }

        public void visit(EnumDeclaration md, Void arg){
            super.visit(md, arg);
            if (md.isNestedType() == true) {
                flag = true;
                return;
            }//如果是nested class就返回
            String name = md.getNameAsString();
            String signature = pkgName + "." + name;
//            System.out.println(signature);
        }
        public void visit(AnnotationDeclaration md, Void arg){
            super.visit(md, arg);
            if (md.isNestedType() == true) {
                flag = true;
                return;
            }//如果是nested class就返回
            String name = md.getNameAsString();
            String signature = pkgName + "." + name;
//            System.out.println(signature);
        }
    }
    //为了处理内部类
    public static void nestedClassVisit(List<BodyDeclaration> members, String signature, CodeBlock classBlock) {
        for (BodyDeclaration member : members) {
            // if member state equal ClassOrInterfaceDeclaration, and you can identify it which is inner class
            if (member.isClassOrInterfaceDeclaration()) {
//                System.out.println("Inner class: "+member.asClassOrInterfaceDeclaration().getMethods().toString());
                String nestedClassName = member.asClassOrInterfaceDeclaration().getNameAsString();
                String signature_inner_class = signature + "." + nestedClassName;
                System.out.println(signature_inner_class);
                NodeList<BodyDeclaration<?>> innerMember = member.asClassOrInterfaceDeclaration().getMembers();
                List<BodyDeclaration> innerMembers = new ArrayList<>();
                for (int i = 0; i < innerMember.size(); i++) {
                    innerMembers.add(innerMember.get(i));
                }
                nestedClassVisit(innerMembers, signature_inner_class, null);
            }
            else if(member.isEnumDeclaration()){
                String nestedClassName = member.asEnumDeclaration().getNameAsString();
                String signature_enum = signature + "." + nestedClassName;
                System.out.println(signature_enum);
                NodeList<BodyDeclaration<?>> innerMember = member.asEnumDeclaration().getMembers();
                List<BodyDeclaration> innerMembers = new ArrayList<>();
                for (int i = 0; i < innerMember.size(); i++) {
                    innerMembers.add(innerMember.get(i));
                }
                nestedClassVisit(innerMembers, signature_enum, null);
            }
            else if(member.isAnnotationDeclaration()){
                String nestedClassName = member.asAnnotationDeclaration().getNameAsString();
                String signature_annotation = signature + "." + nestedClassName;
                System.out.println(signature_annotation);

                NodeList<BodyDeclaration<?>> innerMember = member.asAnnotationDeclaration().getMembers();
                List<BodyDeclaration> innerMembers = new ArrayList<>();
                for (int i = 0; i < innerMember.size(); i++) {
                    innerMembers.add(innerMember.get(i));
                }
                nestedClassVisit(innerMembers, signature_annotation, null);
            }

        }
    }

    public static void test1(int... property){
        System.out.println(property);
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
