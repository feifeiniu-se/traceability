package Constructor.Visitors;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Model.PackageTime;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.List;


public class PackageVisitor {
    private String filePath;
    private List<CodeBlock> codeBlocks;
    private List<CommitCodeChange> codeChange;
    private HashMap<String, CodeBlock> mappings;
    private CommitCodeChange commitCodeChange;

    public void packageVisitor(String filePath, String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings) {
        this.filePath = filePath;
        this.codeBlocks = codeBlocks;
        this.codeChange = codeChange;
        this.mappings = mappings;
        this.commitCodeChange = codeChange.get(codeChange.size()-1); //获得当前commit的内容
        JavaParser javaParser=new JavaParser();
        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();
        Visitor visitor = new Visitor();
        visitor.visit(cu, null);// 遍历完文件的AST树，初步获得信息
    }
    private class Visitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(PackageDeclaration md, Void arg){
            super.visit(md, arg);
            String signature = md.getNameAsString();
            if(!mappings.containsKey(signature)){

                //如果当前package是新的 就进行创建
                CodeBlock codeBlock = new CodeBlock(codeBlocks.size()+1, CodeBlockType.Package);
                mappings.put(signature, codeBlock);//更新mapping， codeblocks， commitcodechange
                PackageTime packageTime = new PackageTime(signature, filePath, commitCodeChange, Operator.ADD_Package, codeBlock);
                codeBlocks.add(codeBlock);
            }else{
                CodeBlock codeBlock = mappings.get(signature);
                //todo 如果包含的话 就更新filepathList
                //如果是在同一个commit中，那就直接添加filelist，如果是不同的commit中，那就增加codeBlockTime
                if(codeBlock.getLastHistory().getTime().getCommitID().equals(commitCodeChange.getCommitID())){
                    codeBlock.getLastHistory().getFilePath().add(filePath);
                }else{
                    PackageTime pkg = new PackageTime(signature, filePath, commitCodeChange, Operator.ADD_Class, codeBlock);
                }
            }

//            if (codeBlocks.containsKey(id)){
//                codeBlock = codeBlocks.get(id);
//                if(codeBlock.getHistory().containsKey(currentTime)){
//                    packageTime = (PackageTime) codeBlock.getHistory().get(currentTime);
//                }else{
//                    packageTime = new PackageTime();
//                    packageTime.setTime(currentTime);
//                }
//            }else{
//                codeBlock = new CodeBlock();
//                packageTime = new PackageTime();
//                packageTime.setTime(currentTime);
//            }
//            codeBlock.setCodeBlockID(id);
//            codeBlock.setType("Package");
//
//            packageTime.setSignature("package:"+md.getNameAsString());
//            packageTime.setFilePath(filePath);
//            packageTime.setOwner("root");
//            codeBlock.addHistory(packageTime);
//            codeBlocks.put(id, codeBlock);
        }
    }
}
