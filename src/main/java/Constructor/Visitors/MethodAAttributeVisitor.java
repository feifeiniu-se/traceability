package Constructor.Visitors;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MethodAAttributeVisitor {
    public List<String> commonTypes = new ArrayList<>(Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "String"));

    public void methodAAttributeVisitor(String filePath, String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings) {

        CommitCodeChange commitCodeChange = codeChange.get(codeChange.size()-1); //获得当前commit的内容
        JavaParser javaParser=new JavaParser();
        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();
        String pkgName = cu.getPackageDeclaration().get().getNameAsString();
        for(TypeDeclaration type : cu.getTypes()) {
            // first give all this java doc member
            String className = type.getNameAsString();
            CodeBlock classBlock = mappings.get(pkgName+":"+className);//todo 理论上说应该是能检测到的
            List<BodyDeclaration> members = type.getMembers();
            // check all member content 有可能是类 方法 属性等
            for(BodyDeclaration member : members) {
                //分别判断属性 方法 内部类（假设内部类不会影响属性的类型）todo
                if(member.isFieldDeclaration()){
//                    System.out.println(member.asFieldDeclaration().toString());
//                    assert(member.asFieldDeclaration().getVariables().size()==1);//todo 有时可能是不止已有一个属性，需要进一步的处理
                    String attributeName = member.asFieldDeclaration().getElementType().toString()+"_"+member.asFieldDeclaration().getVariable(0).getNameAsString();
                    String signature = className+":"+attributeName;
                    String returenType = member.asFieldDeclaration().getElementType().toString();
                    if(!commonTypes.contains(returenType)){
//                        System.out.println("Uncommon types: "+returenType);//todo
                    }
                    //如果不存在 就新增
                    if(!mappings.containsKey(signature)){
                        CodeBlock codeBlock = new CodeBlock(codeBlocks.size()-1, CodeBlockType.Attribute);
                        mappings.put(signature, codeBlock);
                        ClassTime classTime = (ClassTime) classBlock.getLastHistory();
                        AttributeTime attriTime = new AttributeTime(signature, filePath, commitCodeChange, Operator.ADD_Attribute, codeBlock, classBlock);
                        if(classTime.getTime().getCommitID().equals(commitCodeChange.getCommitID())){//如果当前commit与class的最后一个time的commit相同，则直接添加，如果不同，就增加classTime
                            classTime.getAttributes().add(codeBlock);
                        }else{
                            //todo test
                            ClassTime classTimeNew = new ClassTime(classTime.getSignature(), filePath, commitCodeChange, Operator.ADD_Attribute, classBlock, mappings.get(pkgName));
                            classTimeNew.setClasses(new ArrayList<>(classTime.getClasses()));
                            classTimeNew.setMethods(new ArrayList<>(classTime.getMethods()));
                            classTimeNew.setAttributes(new ArrayList<>(classTime.getAttributes()));
                            classTimeNew.getAttributes().add(codeBlock);
                            classBlock.addHistory(classTimeNew);
                            codeChange.get(codeChange.size()-1).addCodeChange(classTimeNew);
                        }
                        codeBlock.addHistory(attriTime);
                        codeBlocks.add(codeBlock);
                        codeChange.get(codeChange.size()-1).addCodeChange(classTime);

                    }//todo 如果存在，暂时不管
                    else{
                        System.out.println("method existing, don't know what to do");
                    }
                }
                if(member.isMethodDeclaration()||member.isConstructorDeclaration()){//方法
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
                    //下边就是增加methodBlock
                    String signature = className+":"+methodName;
                    //方法的返回值类型
                    if(!mappings.containsKey(signature)){
                        //如果不存在 就新增
                        CodeBlock codeBlock = new CodeBlock(codeBlocks.size()-1, CodeBlockType.Method);
                        mappings.put(signature, codeBlock);
                        MethodTime methodTime = new MethodTime(signature, filePath, commitCodeChange, Operator.ADD_Method, codeBlock, classBlock, parameters);
                        ClassTime classTime = (ClassTime) classBlock.getLastHistory();
                        if(classTime.getTime().getCommitID().equals(commitCodeChange.getCommitID())){
                            classTime.getMethods().add(codeBlock);
                        }else{
                            ClassTime classTimeNew = new ClassTime(classTime.getSignature(), filePath, commitCodeChange, Operator.ADD_Method, classBlock, mappings.get(pkgName));
                            classTimeNew.setClasses(new ArrayList<>(classTime.getClasses()));
                            classTimeNew.setMethods(new ArrayList<>(classTime.getMethods()));
                            classTimeNew.setAttributes(new ArrayList<>(classTime.getAttributes()));
                            classTimeNew.getMethods().add(codeBlock);
                            classBlock.addHistory(classTimeNew);
                            codeChange.get(codeChange.size()-1).addCodeChange(classTimeNew);
                        }
                        codeBlock.addHistory(methodTime);
                        codeBlocks.add(codeBlock);
                        codeChange.get(codeChange.size()-1).addCodeChange(classTime);
                    }else{
                        System.out.println("Don't know what to do");
                    }
                }


                // if member state equal ClassOrInterfaceDeclaration, and you can identify it which is inner class
                if(member.isClassOrInterfaceDeclaration()) {
                    //todo add inner class
                    // get inner class method
                    for(MethodDeclaration method : member.asClassOrInterfaceDeclaration().getMethods()) {
//                        log.info("Method Name :{}", method.getName());
                    }
//                    VerifyInnerClassAndParse(member.asClassOrInterfaceDeclaration());
                }
            }
        }
    }

}
