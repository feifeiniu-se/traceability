package Constructor.Visitors;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ClassVisitor {
    private List<CodeBlock> codeBlocks;
    private List<CommitCodeChange> codeChange;
    private HashMap<String, CodeBlock> mappings;
    private CommitCodeChange commitCodeChange;
    private String pkgName;
    private CodeBlock pkgBlock;
    private boolean flag = false;

    public void classVisitor(String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings) {
        this.codeBlocks = codeBlocks;
        this.codeChange = codeChange;
        this.mappings = mappings;
        this.commitCodeChange = codeChange.get(codeChange.size() - 1); //获得当前commit的内容
//        System.out.println(fileContent);
        JavaParser javaParser = new JavaParser();
        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();//done

//        System.out.println(fileContent);
        if(cu.getPackageDeclaration().isPresent()){
            pkgName = cu.getPackageDeclaration().get().getNameAsString();
        }else{
            pkgName = "default.package";
        }
        assert mappings.containsKey(pkgName);
        pkgBlock = mappings.get(pkgName);

        Visitor visitor = new Visitor();
        visitor.visit(cu, null);// 遍历完文件的AST树，初步获得信息

        //前边是获得了第一层类的信息，接下来要遍历一下内部类
        if(flag){
            for (TypeDeclaration type : cu.getTypes()) {
                // first give all this java doc member
                String className = type.getNameAsString();
                String signature_class = pkgName + "." + className;
                assert mappings.containsKey(signature_class);
                CodeBlock classBlock = mappings.get(signature_class);
                List<BodyDeclaration> members = type.getMembers();
                // check all member content： class, method, attribute
                nestedClassVisit(members, signature_class, classBlock);
            }
        }
    }


    private class Visitor extends VoidVisitorAdapter<Void> {

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
            if (!mappings.containsKey(signature)) {
                //creat new classBlock, update package
//                System.out.println(signature);
                CodeBlock classBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                mappings.put(signature, classBlock);
                codeBlocks.add(classBlock);
                ClassTime classTime = new ClassTime(name, commitCodeChange, Operator.Add_Class, classBlock, pkgBlock);//create classTime, add to classBlock, commitTime, update parentBlock
            }
        }

        public void visit(EnumDeclaration md, Void arg){
            super.visit(md, arg);
            if (md.isNestedType() == true) {
                flag = true;
                return;
            }//如果是nested class就返回
            String name = md.getNameAsString();
            String signature = pkgName + "." + name;
            if (!mappings.containsKey(signature)) {
                CodeBlock classBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                mappings.put(signature, classBlock);
                codeBlocks.add(classBlock);
                ClassTime classTime = new ClassTime(name, commitCodeChange, Operator.Add_Class, classBlock, pkgBlock);//create classTime, add to classBlock, commitTime, update parentBlock
            }
        }
        public void visit(AnnotationDeclaration md, Void arg){
            super.visit(md, arg);
            if (md.isNestedType() == true) {
                flag = true;
                return;
            }//如果是nested class就返回
            String name = md.getNameAsString();
            String signature = pkgName + "." + name;
            if (!mappings.containsKey(signature)) {
                CodeBlock classBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                mappings.put(signature, classBlock);
                codeBlocks.add(classBlock);
                ClassTime classTime = new ClassTime(name, commitCodeChange, Operator.Add_Class, classBlock, pkgBlock);//create classTime, add to classBlock, commitTime, update parentBlock
            }
        }
    }
    //为了处理内部类
    public void nestedClassVisit(List<BodyDeclaration> members, String signature, CodeBlock classBlock) {
        for (BodyDeclaration member : members) {
            // if member state equal ClassOrInterfaceDeclaration, and you can identify it which is inner class
            if (member.isClassOrInterfaceDeclaration()) {
//                System.out.println("Inner class: "+member.asClassOrInterfaceDeclaration().getMethods().toString());
                String nestedClassName = member.asClassOrInterfaceDeclaration().getNameAsString();
                String signature_inner_class = signature + "." + nestedClassName;
                CodeBlock innerClassBlock;
                if (!mappings.containsKey(signature_inner_class)) {
                    innerClassBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                    ClassTime classTime = new ClassTime(nestedClassName, commitCodeChange, Operator.Add_Class, innerClassBlock, classBlock);
                    mappings.put(signature_inner_class, innerClassBlock);
//                    System.out.println(signature_inner_class);
                    codeBlocks.add(innerClassBlock);
                } else {
                    innerClassBlock = mappings.get(signature_inner_class);
                }
                NodeList<BodyDeclaration<?>> innerMember = member.asClassOrInterfaceDeclaration().getMembers();
                List<BodyDeclaration> innerMembers = new ArrayList<>();
                for (int i = 0; i < innerMember.size(); i++) {
                    innerMembers.add(innerMember.get(i));
                }
                nestedClassVisit(innerMembers, signature_inner_class, innerClassBlock);
            }
            if(member.isEnumDeclaration()){
                String nestedClassName = member.asEnumDeclaration().getNameAsString();
                String signature_enum = signature + "." + nestedClassName;
//                System.out.println(signature_enum + " enum");
                CodeBlock enumClassBlock;
                if (!mappings.containsKey(signature_enum)) {
                    enumClassBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                    ClassTime classTime = new ClassTime(nestedClassName, commitCodeChange, Operator.Add_Class, enumClassBlock, classBlock);
                    mappings.put(signature_enum, enumClassBlock);
//                    System.out.println(signature_inner_class);
                    codeBlocks.add(enumClassBlock);
                } else {
                    enumClassBlock = mappings.get(signature_enum);
                }
                NodeList<BodyDeclaration<?>> innerMember = member.asEnumDeclaration().getMembers();
                List<BodyDeclaration> innerMembers = new ArrayList<>();
                for (int i = 0; i < innerMember.size(); i++) {
                    innerMembers.add(innerMember.get(i));
                }
                nestedClassVisit(innerMembers, signature_enum, enumClassBlock);
            }
            if(member.isAnnotationDeclaration()){
                assert 1==2;
            }
        }
    }

}
