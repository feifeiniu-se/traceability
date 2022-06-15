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


import java.util.*;

import static Constructor.Utils.toRoot;

public class MethodAndAttributeVisitor {
    public List<String> commonTypes = new ArrayList<>(Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "String"));
    List<CodeBlock> codeBlocks;
    HashMap<String, CodeBlock> mappings;
    CommitCodeChange commitTime;

    public void methodAAttributeVisitor(String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings) {
        this.codeBlocks = codeBlocks;
        this.mappings = mappings;
        this.commitTime = codeChange.get(codeChange.size() - 1);
//        System.out.println(fileContent);
        JavaParser javaParser = new JavaParser();
        Optional<CompilationUnit> tmp = javaParser.parse(fileContent).getResult();
        if(!tmp.isPresent()){
            return;
        }
        CompilationUnit cu = tmp.get();
        String pkgName;
        if (cu.getPackageDeclaration().isPresent()) {
            pkgName = cu.getPackageDeclaration().get().getNameAsString();
        } else {
            pkgName = "default.package";
        }

        for (TypeDeclaration type : cu.getTypes()) {
            // first give all this java doc member
            String className = type.getNameAsString();
            String signature_class = pkgName + "." + className;//todo 查看signature内容到底是啥，会不会一直增加
//            System.out.println(signature_class);
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
                    String attributeName = toRoot(member.asFieldDeclaration().getElementType().toString()) + "_" + member.asFieldDeclaration().getVariable(i).getNameAsString();
                    if (attributeName.contains(".")) {
                        attributeName = attributeName.substring(attributeName.lastIndexOf(".") + 1);
                    }
                    String signature_attribute = signature + ":" + attributeName;
                    String returenType = member.asFieldDeclaration().getElementType().toString();
//                    if (!commonTypes.contains(returenType)) {
////                        System.out.println("Uncommon types: "+returenType);//todo
//                    }

                    if (!mappings.containsKey(signature_attribute)) {
                        CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Attribute);
                        mappings.put(signature_attribute, codeBlock);
//                        if(signature_attribute.contains("GenericMode")){
//                            System.out.println(signature_attribute);
//                        }
                        codeBlocks.add(codeBlock);
                        AttributeTime attriTime = new AttributeTime(attributeName, commitTime, Operator.Add_Attribute, codeBlock, classBlock);
                    }
                }
            }
            if (member.isMethodDeclaration() || member.isConstructorDeclaration()) {//method
                String methodName = null;
                String parameters = "";
                if (member.isMethodDeclaration()) {
                    //之前写的 现在重新写了
////                    String returnType = member.asMethodDeclaration().getType().toString().contains(".")?member.asMethodDeclaration().getType().asClassOrInterfaceType().getNameAsString():member.asMethodDeclaration().getType().toString();
//                    String returnType = member.asMethodDeclaration().getTypeAsString();
//                    returnType = returnType.substring(returnType.lastIndexOf(".") + 1);
//                    methodName = returnType + "_" + member.asMethodDeclaration().getSignature();
//                    parameters = member.asMethodDeclaration().getParameters().toString();
//                    //if the type of parameter is classorInterfaceType from another project(like ReplicatorInput(org.jgroups.Channel, Serializable)), modify it.
//                    if (methodName.contains(".")) {
//                        for (int i = 0; i < member.asMethodDeclaration().getParameters().size(); i++) {
////                            if (member.asMethodDeclaration().getParameters().get(i).getType().isClassOrInterfaceType()) {
////                                methodName = methodName.replace(member.asMethodDeclaration().getParameters().get(i).getType().toString(), member.asMethodDeclaration().getParameters().get(i).getType().asClassOrInterfaceType().getNameAsString());
////                            }
//                            String param = member.asMethodDeclaration().getParameters().get(i).getType().toString();
//                            if(param.contains(".")){
//                                methodName = methodName.replace(param, param.substring(param.lastIndexOf(".")+1));
//                            }
//                        }
//                    }
                    //新版本
                    methodName = toRoot(member.asMethodDeclaration().getTypeAsString()) + "_" + toRoot(member.asMethodDeclaration().getNameAsString());
                    for(int i=0; i<member.asMethodDeclaration().getParameters().size(); i++){
                        parameters = parameters + ", " + toRoot(member.asMethodDeclaration().getParameter(i).getType().toString());
                        if(signature.contains("org.apache.derby.vti.XmlVTI")){

                            System.out.println(member.asMethodDeclaration().getParameter(i).getType().toString());
                        }
                    }
                    parameters = parameters.length()>0?parameters.substring(2):parameters;
                    methodName = methodName + "(" + parameters + ")";


                } else if (member.isConstructorDeclaration()) {
//                    methodName = member.asConstructorDeclaration().getSignature().toString();
//                    parameters = member.asConstructorDeclaration().getParameters().toString();
//                    //if the type of parameter is classorInterfaceType from another project(like ReplicatorInput(org.jgroups.Channel, Serializable)), modify it.
//                    if (methodName.contains(".")) {
//                        for (int i = 0; i < member.asConstructorDeclaration().getParameters().size(); i++) {
////                            if (member.asConstructorDeclaration().getParameters().get(i).getType().isClassOrInterfaceType()) {
////                                methodName = methodName.replace(member.asConstructorDeclaration().getParameters().get(i).getType().toString(), member.asConstructorDeclaration().getParameters().get(i).getType().asClassOrInterfaceType().getNameAsString());
////                            }
//                            String param = member.asConstructorDeclaration().getParameters().get(i).getType().toString();
//                            if(param.contains(".")){
//                                String paramN = param.substring(param.lastIndexOf(".")+1);
//                                methodName = methodName.replace(param, paramN);
//                            }
//                        }
//                    }
                    methodName = toRoot(member.asConstructorDeclaration().getNameAsString());
                    for(int i=0; i<member.asConstructorDeclaration().getParameters().size(); i++){
                        if(signature.contains("org.apache.derbyPreBuild.PropertySetter")){

                            System.out.println(member.asConstructorDeclaration().getParameter(i).getType().toString());
                        }
                        parameters = parameters + ", " + toRoot(member.asConstructorDeclaration().getParameter(i).getTypeAsString());
                    }
                    parameters = parameters.length()>0?parameters.substring(2):parameters;
                    methodName = methodName + "(" + parameters + ")";

                } else {
                    System.out.println("Something wrong happend");
                }
                //add method block
                String signature_method = signature + ":" + methodName;
                if (!mappings.containsKey(signature_method)) {
                    CodeBlock codeBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Method);
                    mappings.put(signature_method, codeBlock);
                    codeBlocks.add(codeBlock);
                    if (signature_method.contains("org.apache.derbyPreBuild.PropertySetter")) {
                        System.out.println(signature_method);
                    }
                    MethodTime methodTime = new MethodTime(methodName, commitTime, Operator.Add_Method, codeBlock, classBlock, parameters);
                }
            }
            //todo 记得把classVisitor中其他的类型加进来

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
//                    if (signature_inner_class.contains("org.apache.derby.iapi.security.SecurityUtil")) {
//                        System.out.println(signature_inner_class);
//                    }
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
            } else if (member.isAnnotationDeclaration()) {
                assert 1 == 2;
            } else if (member.isEnumDeclaration()) {
//                assert 1 == 2;
            }
        }
    }

}
