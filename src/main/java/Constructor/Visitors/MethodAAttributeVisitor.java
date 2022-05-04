package Constructor.Visitors;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MethodAAttributeVisitor {
    public List<String> commonTypes = new ArrayList<>(Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "String"));

    public void methodAAttributeVisitor(String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings) {
        CommitCodeChange commitCodeChange = codeChange.get(codeChange.size()-1); //获得当前commit的内容
        JavaParser javaParser=new JavaParser();
        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();
        String pkgName = cu.getPackageDeclaration().get().getNameAsString();

        for(TypeDeclaration type : cu.getTypes()) {
            // first give all this java doc member
            String className = type.getNameAsString();
            String signature_class = pkgName + "." + className;//todo 查看signature内容到底是啥，会不会一直增加
            assert mappings.containsKey(signature_class);
            CodeBlock classBlock = mappings.get(signature_class);
            List<BodyDeclaration> members = type.getMembers();
            // check all member content： class, method, attribute
            classVisit(members, signature_class, mappings, codeBlocks, commitCodeChange, classBlock);
        }
    }
    public void classVisit(List<BodyDeclaration> members, String signature, HashMap<String, CodeBlock> mappings, List<CodeBlock> codeBlocks, CommitCodeChange commitCodeChange, CodeBlock classBlock){
        for(BodyDeclaration member : members) {
            //分别判断属性 方法 内部类（假设内部类不会影响属性的类型）todo
            if(member.isFieldDeclaration()){
//                    System.out.println(member.asFieldDeclaration().toString());
//                    assert(member.asFieldDeclaration().getVariables().size()==1);//todo 有时可能是不止已有一个属性，需要进一步的处理
                String attributeName = member.asFieldDeclaration().getElementType().toString()+"_"+member.asFieldDeclaration().getVariable(0).getNameAsString();
                String signature_attribute = signature+":"+attributeName;
                String returenType = member.asFieldDeclaration().getElementType().toString();
                if(!commonTypes.contains(returenType)){
//                        System.out.println("Uncommon types: "+returenType);//todo
                }

                if(!mappings.containsKey(signature_attribute)){
                    CodeBlock codeBlock = new CodeBlock(codeBlocks.size()-1, CodeBlockType.Attribute);
                    mappings.put(signature_attribute, codeBlock);
                    codeBlocks.add(codeBlock);
                    AttributeTime attriTime = new AttributeTime(attributeName, commitCodeChange, Operator.Add_Attribute, codeBlock, classBlock);
                }
            }
            if(member.isMethodDeclaration()||member.isConstructorDeclaration()){//method
                String methodName = null;
                String parameters = null;
                if(member.isMethodDeclaration()){
                    methodName = member.asMethodDeclaration().getType().toString()+"_"+member.asMethodDeclaration().getSignature();
                    parameters = member.asMethodDeclaration().getParameters().toString();
                }else if(member.isConstructorDeclaration()){
                    methodName = member.asConstructorDeclaration().getSignature().toString();
                    parameters = member.asConstructorDeclaration().getParameters().toString();
                }else{
                    System.out.println("Something wrong happend");
                }
                //add method block
                String signature_method = signature+":"+methodName;
                if(!mappings.containsKey(signature_method)){
                    CodeBlock codeBlock = new CodeBlock(codeBlocks.size()-1, CodeBlockType.Method);
                    mappings.put(signature_method, codeBlock);
                    codeBlocks.add(codeBlock);
                    MethodTime methodTime = new MethodTime(methodName, commitCodeChange, Operator.Add_Method, codeBlock, classBlock, parameters);
                }
            }

            // if member state equal ClassOrInterfaceDeclaration, and you can identify it which is inner class
            if(member.isClassOrInterfaceDeclaration()) {
                //todo add inner class
                // get inner class method
                System.out.println("Inner class: "+member.asClassOrInterfaceDeclaration().getMethods().toString());
                String innerClassName = member.asClassOrInterfaceDeclaration().getNameAsString();
                String signature_inner_class = signature+"." + innerClassName;
                CodeBlock innerClassBlock;
                if(!mappings.containsKey(signature_inner_class)){
                    innerClassBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Class);
                    ClassTime classTime = new ClassTime(innerClassName, commitCodeChange, Operator.Add_Class, innerClassBlock, classBlock);
                    mappings.put(signature_inner_class, innerClassBlock);
                    codeBlocks.add(innerClassBlock);
                }else{
                    innerClassBlock = mappings.get(signature_inner_class);
                }
                NodeList<BodyDeclaration<?>> innerMember = member.asClassOrInterfaceDeclaration().getMembers();
                List<BodyDeclaration> innerMembers = new ArrayList<>();
                for(int i=0; i<innerMember.size(); i++){
                    innerMembers.add(innerMember.get(i));
                }
                classVisit(innerMembers, signature_inner_class, mappings, codeBlocks, commitCodeChange, innerClassBlock);
            }
        }
    }

}
