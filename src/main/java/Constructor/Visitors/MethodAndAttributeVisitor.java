package Constructor.Visitors;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MethodAndAttributeVisitor {
    public List<String> commonTypes = new ArrayList<>(Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "String"));
    List<CodeBlock> codeBlocks;
    HashMap<String, CodeBlock> mappings;
    CommitCodeChange commitTime;

    public void methodAAttributeVisitor(String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings) {
        this.codeBlocks = codeBlocks;
        this.mappings = mappings;
        this.commitTime = codeChange.get(codeChange.size() - 1);

        JavaParser javaParser = new JavaParser();
        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();
        String pkgName = cu.getPackageDeclaration().get().getNameAsString();

        for (TypeDeclaration type : cu.getTypes()) {
            // first give all this java doc member
            String className = type.getNameAsString();
            String signature_class = pkgName + "." + className;//todo 查看signature内容到底是啥，会不会一直增加
            assert mappings.containsKey(signature_class);
            CodeBlock classBlock = mappings.get(signature_class);
            List<BodyDeclaration> members = type.getMembers();
            // check all member content： class, method, attribute
            classVisit(members, signature_class, classBlock);
        }
    }

    public void classVisit(List<BodyDeclaration> members, String signature, CodeBlock classBlock) {
        for (BodyDeclaration member : members) {
            //分别判断属性 方法 内部类（假设内部类不会影响属性的类型）todo
            if (member.isFieldDeclaration()) {
                assert (member.asFieldDeclaration().getVariables().size() > 0);//todo 有时可能是不止已有一个属性，需要进一步的处理
                for (int i = 0; i < member.asFieldDeclaration().getVariables().size(); i++) {
                    String attributeName = member.asFieldDeclaration().getElementType().toString() + "_" + member.asFieldDeclaration().getVariable(i).getNameAsString();
                    String signature_attribute = signature + ":" + attributeName;
                    String returenType = member.asFieldDeclaration().getElementType().toString();
                    if (!commonTypes.contains(returenType)) {
//                        System.out.println("Uncommon types: "+returenType);//todo
                    }
                    if (!mappings.containsKey(signature_attribute)) {
                        CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Attribute);
                        mappings.put(signature_attribute, codeBlock);
                        codeBlocks.add(codeBlock);
                        AttributeTime attriTime = new AttributeTime(attributeName, commitTime, Operator.Add_Attribute, codeBlock, classBlock);
                    }
                }
            }
            if (member.isMethodDeclaration() || member.isConstructorDeclaration()) {//method
                String methodName = null;
                String parameters = null;
                if (member.isMethodDeclaration()) {
//
                    String returnType = member.asMethodDeclaration().getType().toString().contains(".")?member.asMethodDeclaration().getType().asClassOrInterfaceType().getNameAsString():member.asMethodDeclaration().getType().toString();

                    methodName = returnType + "_" + member.asMethodDeclaration().getSignature();
                    parameters = member.asMethodDeclaration().getParameters().toString();
                    //if the type of parameter is classorInterfaceType from another project(like ReplicatorInput(org.jgroups.Channel, Serializable)), modify it.
                    if (methodName.contains(".")) {
                        for (int i = 0; i < member.asMethodDeclaration().getParameters().size(); i++) {
                            if (member.asMethodDeclaration().getParameters().get(i).getType().isClassOrInterfaceType()) {
                                methodName = methodName.replace(member.asMethodDeclaration().getParameters().get(i).getType().toString(), member.asMethodDeclaration().getParameters().get(i).getType().asClassOrInterfaceType().getNameAsString());
                            }
                        }
                    }

                } else if (member.isConstructorDeclaration()) {
                    methodName = member.asConstructorDeclaration().getSignature().toString();
                    parameters = member.asConstructorDeclaration().getParameters().toString();
                    //if the type of parameter is classorInterfaceType from another project(like ReplicatorInput(org.jgroups.Channel, Serializable)), modify it.
                    if (methodName.contains(".")) {
                        for (int i = 0; i < member.asConstructorDeclaration().getParameters().size(); i++) {
                            if (member.asConstructorDeclaration().getParameters().get(i).getType().isClassOrInterfaceType()) {
                                methodName = methodName.replace(member.asConstructorDeclaration().getParameters().get(i).getType().toString(), member.asConstructorDeclaration().getParameters().get(i).getType().asClassOrInterfaceType().getNameAsString());
                            }
                        }
                    }

                } else {
                    System.out.println("Something wrong happend");
                }
                //add method block
                String signature_method = signature + ":" + methodName;
                if (!mappings.containsKey(signature_method)) {
                    CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Method);
                    mappings.put(signature_method, codeBlock);
                    codeBlocks.add(codeBlock);
                    MethodTime methodTime = new MethodTime(methodName, commitTime, Operator.Add_Method, codeBlock, classBlock, parameters);
                }
            }

            // if member state equal ClassOrInterfaceDeclaration, and you can identify it which is inner class
            if (member.isClassOrInterfaceDeclaration()) {
//                System.out.println("Inner class: "+member.asClassOrInterfaceDeclaration().getMethods().toString());
                String innerClassName = member.asClassOrInterfaceDeclaration().getNameAsString();
                String signature_inner_class = signature + "." + innerClassName;
                CodeBlock innerClassBlock;
                if (!mappings.containsKey(signature_inner_class)) {
                    innerClassBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                    ClassTime classTime = new ClassTime(innerClassName, commitTime, Operator.Add_Class, innerClassBlock, classBlock);
                    mappings.put(signature_inner_class, innerClassBlock);
                    codeBlocks.add(innerClassBlock);
                } else {
                    innerClassBlock = mappings.get(signature_inner_class);
                }
                NodeList<BodyDeclaration<?>> innerMember = member.asClassOrInterfaceDeclaration().getMembers();
                List<BodyDeclaration> innerMembers = new ArrayList<>();
                for (int i = 0; i < innerMember.size(); i++) {
                    innerMembers.add(innerMember.get(i));
                }
                classVisit(innerMembers, signature_inner_class, innerClassBlock);
            }
        }
    }

}
