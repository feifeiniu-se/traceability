//package Constructor;
//
//import Model.CodeBlock;
//import Model.CommitCodeChange;
//import com.github.javaparser.JavaParser;
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.PackageDeclaration;
//import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
//import com.github.javaparser.ast.body.ConstructorDeclaration;
//import com.github.javaparser.ast.body.FieldDeclaration;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
//
//public class JavaParserVisitor {
//    private static final Logger logger = LoggerFactory.getLogger(JavaParserVisitor.class);
//
//
//    public void fileParser(String fileName, String fileContent, String hashCode){
//
//        JavaParser javaParser=new JavaParser();
//        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();
//        Visitor visitor = new Visitor();
//        visitor.visit(cu, null);// 遍历完文件的AST树，初步获得信息
//    }
//
//    private class Visitor extends VoidVisitorAdapter<Void> {
//        // package
////        @Override
////        public void visit(PackageDeclaration md, Void arg){
////            super.visit(md, arg);
//////            System.out.println(0);
////            String id = sha1Hex("package:"+md.getNameAsString());//TODO waiting for further modify type:name
////            packages.add(id);
////
////            CodeBlock codeBlock;
////            PackageTime packageTime;
////            if (codeBlocks.containsKey(id)){
////                codeBlock = codeBlocks.get(id);
////                if(codeBlock.getHistory().containsKey(currentTime)){
////                    packageTime = (PackageTime) codeBlock.getHistory().get(currentTime);
////                }else{
////                    packageTime = new PackageTime();
////                    packageTime.setTime(currentTime);
////                }
////            }else{
////                codeBlock = new CodeBlock();
////                packageTime = new PackageTime();
////                packageTime.setTime(currentTime);
////            }
////            codeBlock.setCodeBlockID(id);
////            codeBlock.setType("Package");
////
////            packageTime.setSignature("package:"+md.getNameAsString());
////            packageTime.setFilePath(filePath);
////            packageTime.setOwner("root");
////            codeBlock.addHistory(packageTime);
////            codeBlocks.put(id, codeBlock);
////        }
//
//        //class or interface
//        @Override
//        public void visit(ClassOrInterfaceDeclaration md, Void arg) {
//            super.visit(md, arg);
////            System.out.println(1);
//            String id = sha1Hex("class:"+filePath+":"+md.getNameAsString());//TODO waiting for further modify
//
//            CodeBlock codeBlock;
//            ClassTime classTime;
//            if (codeBlocks.containsKey(id)){
//                codeBlock = codeBlocks.get(id);
//                if(codeBlock.getHistory().containsKey(currentTime)){
//                    classTime = (ClassTime) codeBlock.getHistory().get(currentTime);
//                }else{
//                    classTime = new ClassTime();
//                    classTime.setTime(currentTime);
//                }
//            }else{
//                codeBlock = new CodeBlock();
//                classTime = new ClassTime();
//                classTime.setTime(currentTime);
//            }
//
//            codeBlock.setCodeBlockID(id);
//            codeBlock.setType("Class");
//
//            classTime.setSignature("class:"+filePath+":"+md.getNameAsString());//TODO
//            classTime.setFilePath(filePath);
//            classTime.setStartLine(md.getRange().get().begin.line);
//            classTime.setEndLine(md.getRange().get().end.line);
//
//
//            //TODO 判断是否还有漏掉的其他类型
//            //计算class内部包含的方法 属性以及内部类
//            //由于package class method attribute都与class有关联，而且class是最后visit的，所以只需要在这里进行处理
//            List<FieldDeclaration> fields = md.getFields();
//            for(FieldDeclaration f: fields){
//                String fID = sha1Hex("attribute:"+filePath+":"+f.getModifiers().toString()+" "+f.getElementType().toString()+" "+f.getVariables().toString());
//                if(codeBlocks.containsKey(fID)){//如果已经存在就互相更新值，如果不存在就报错
////                    System.out.println(codeBlocks.get(fID).getHistory().get(currentTime).getOwner());
//                    codeBlocks.get(fID).getHistory().get(currentTime).setOwner(id);// 更新owner
//                    classTime.addSubNodes(fID);//更新subnode
////                    System.out.println(codeBlocks.get(fID).getHistory().get(currentTime).getOwner());
//
//                }else{
//                    System.out.println("Error, attribute not found");
////                    System.out.println("attribute:"+filePath+":"+f.toString().split("=")[0]);
//                    //DONE
//                }
//            }
//
//            //计算 methodDeclaration
//            List<MethodDeclaration> tempMethod = md.getMethods();
//            for(MethodDeclaration m: tempMethod){
//                String mID = sha1Hex("method:"+filePath+":"+m.getDeclarationAsString());
//                if(codeBlocks.containsKey(mID)){
//                    codeBlocks.get(mID).getHistory().get(currentTime).setOwner(id);//update owner of method as class
//                    classTime.addSubNodes(mID);
//                }else{
//                    System.out.println("Error, method not found1");
//                }
//            }
//
//            //计算ConstructorDeclaration
//            List<ConstructorDeclaration> tempConstructor = md.getConstructors();
//            for(ConstructorDeclaration c: tempConstructor){
//                String cID = sha1Hex("method:"+filePath+":"+c.getDeclarationAsString());
//                if(codeBlocks.containsKey(cID)){
//                    codeBlocks.get(cID).getHistory().get(currentTime).setOwner(id);//update owner of method as class
//                    classTime.addSubNodes(cID);
//                }else{
//                    System.out.println("Error, method not found2");
//                }
//            }
//
//            //计算package 目前只算有一个package
//            //TODO
//            classTime.setOwner(packages.get(0));
//            for(String pID: packages){
//                try {
//                    Method addx = codeBlocks.get(pID).getHistory().get(currentTime).getClass().getMethod("addClasses", String.class);
//                    addx.invoke(codeBlocks.get(pID).getHistory().get(currentTime), id);
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            codeBlock.addHistory(classTime);
//            codeBlocks.put(id, codeBlock);
//
//        }
//
//        //method
//        @Override
//        public void visit(MethodDeclaration md, Void arg) {
//            super.visit(md, arg);
////            System.out.println(2);
//            String id = sha1Hex("method:"+filePath+":"+md.getDeclarationAsString());//TODO
//
////            System.out.println(md.getDeclarationAsString());//函数第一行
////            System.out.println(md.getParameters());//
////            System.out.println(md.getType());//返回值类型
////            System.out.println(md.getChildNodes());//修饰符 方法名 参数 throw类型 返回值类型 方法体
//
//
//            CodeBlock codeBlock;
//            MethodTime methodTime;
//            if(codeBlocks.containsKey(id)){
//                codeBlock = codeBlocks.get(id);
//                if(codeBlock.getHistory().containsKey(currentTime)){
//                    methodTime = (MethodTime) codeBlock.getHistory().get(currentTime);
//                }else{
//                    methodTime = new MethodTime();
//                    methodTime.setTime(currentTime);
//                }
//            }else{
//                codeBlock = new CodeBlock();
//                methodTime = new MethodTime();
//                methodTime.setTime(currentTime);
//            }
//
//            codeBlock.setCodeBlockID(id);
//            codeBlock.setType("Method");
//
//            methodTime.setSignature("method:"+filePath+":"+md.getDeclarationAsString());//TODO
//            methodTime.setFilePath(filePath);
//            methodTime.setStartLine(md.getRange().get().begin.line);
//            methodTime.setEndLine(md.getRange().get().end.line);
//            if(md.getParameters().size()>0){
//                List<String> parameterList = new ArrayList<>();
//                md.getParameters().forEach(item -> parameterList.add(item.getType()+" "+item.getName()));
//                methodTime.setParameters(parameterList);
//            }//TODO parameter list
//            codeBlock.addHistory(methodTime);
//            codeBlocks.put(codeBlock.getCodeBlockID(), codeBlock);
//        }
//
//        //method
//        @Override
//        public void visit(ConstructorDeclaration md, Void arg) {
//            super.visit(md, arg);
////            System.out.println(3);
//            String id = sha1Hex("method:"+filePath+":"+md.getDeclarationAsString());//TODO
//
//            CodeBlock codeBlock;
//            MethodTime methodTime;
//            if(codeBlocks.containsKey(id)){
//                codeBlock = codeBlocks.get(id);
//                if(codeBlock.getHistory().containsKey(currentTime)){
//                    methodTime = (MethodTime) codeBlock.getHistory().get(currentTime);
//                }else{
//                    methodTime = new MethodTime();
//                    methodTime.setTime(currentTime);
//                }
//            }else{
//                codeBlock = new CodeBlock();
//                methodTime = new MethodTime();
//                methodTime.setTime(currentTime);
//            }
//
//            codeBlock.setCodeBlockID(id);
//            codeBlock.setType("Method");
//
//            methodTime.setSignature("method:"+filePath+":"+md.getDeclarationAsString());//TODO
//            methodTime.setFilePath(filePath);
//            methodTime.setStartLine(md.getRange().get().begin.line);
//            methodTime.setEndLine(md.getRange().get().end.line);
//            if(md.getParameters().size()>0){
//                List<String> parameterList = new ArrayList<>();
//                md.getParameters().forEach(item -> parameterList.add(item.getType()+" "+item.getName()));
//                methodTime.setParameters(parameterList);
//            }//TODO parameter list
//            codeBlock.addHistory(methodTime);
//            codeBlocks.put(codeBlock.getCodeBlockID(), codeBlock);
//        }
//
//        //attribute
//        @Override
//        public void visit(FieldDeclaration md, Void arg){
//            super.visit(md, arg);
////            System.out.println(4);
//            String id = sha1Hex("attribute:"+filePath+":"+md.getModifiers().toString()+" "+md.getElementType().toString()+" "+md.getVariables().toString()); //TODO public String x
////            System.out.println("attribute:"+filePath+":"+md.toString().split("=")[0]);
////            System.out.println("attribute:"+filePath+":"+md.getModifiers().toString()+" "+md.getElementType().toString()+" "+md.getVariables().toString());
//
//            CodeBlock codeBlock;
//            AttributeTime attributeTime;
//            if(codeBlocks.containsKey(id)){
//                codeBlock = codeBlocks.get(id);
//                if(codeBlock.getHistory().containsKey(currentTime)){
//                    attributeTime = (AttributeTime) codeBlock.getHistory().get(currentTime);
//                }else{
//                    attributeTime = new AttributeTime();
//                    attributeTime.setTime(currentTime);
//                }
//            }else{
//                codeBlock = new CodeBlock();
//                attributeTime = new AttributeTime();
//                attributeTime.setTime(currentTime);
//            }
//            codeBlock.setCodeBlockID(id);
//            codeBlock.setType("Attribute");
//            attributeTime.setSignature("attribute:"+filePath+":"+md.getModifiers().toString()+" "+md.getElementType().toString()+" "+md.getVariables().toString());
////            attributeTime.setSignature("attribute:"+filePath+":"+md.toString().split("=")[0]);
//            attributeTime.setFilePath(filePath);
//            attributeTime.setStartLine(md.getRange().get().begin.line);
//            attributeTime.setEndLine(md.getRange().get().end.line);
//            attributeTime.setDeclareClass(md.getElementType().toString());
////            System.out.println(md.getElementType());
////            System.out.println(md.getCommonType());//TODO 两种输出一样 暂时不确定二者间的区别
//            codeBlock.addHistory(attributeTime);
//            codeBlocks.put(codeBlock.getCodeBlockID(), codeBlock);
//        }
//
//    }
//
////
////    public static void main(String[] args){
////        //获取一个java文件中所有的类型
////        String address = "C:\\Users\\Feifei\\dataset\\projects\\hornetq";
////        String startHash = "1a9aa377d578e1ca8bb7aa4cf99ab53d9183801e";
////        Map<String, String> fileList = InitialVersion.getInitialFileList(address, startHash);
////        fileParser(fileList.keySet().toArray()[0].toString(), fileList.values().toArray()[0].toString());
////
////
////    }
//
//}
