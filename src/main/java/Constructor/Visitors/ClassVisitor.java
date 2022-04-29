package Constructor.Visitors;
import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.ClassTime;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Model.PackageTime;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ClassVisitor {
    private String filePath;
    private List<CodeBlock> codeBlocks;
    private List<CommitCodeChange> codeChange;
    private HashMap<String, CodeBlock> mappings;
    private CommitCodeChange commitCodeChange;
    private String pkgName;

    public void classVisitor(String filePath, String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings){
        this.filePath = filePath;
        this.codeBlocks = codeBlocks;
        this.codeChange = codeChange;
        this.mappings = mappings;
        this.commitCodeChange = codeChange.get(codeChange.size()-1); //获得当前commit的内容
        JavaParser javaParser=new JavaParser();
        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();//done 这里会报错

        pkgName = cu.getPackageDeclaration().get().getNameAsString();
        Visitor visitor = new Visitor();
        visitor.visit(cu, null);// 遍历完文件的AST树，初步获得信息
    }

    private class Visitor extends VoidVisitorAdapter<Void> {

        @Override
        //class or interface
        public void visit(ClassOrInterfaceDeclaration md, Void arg) {
            super.visit(md, arg);
            if(md.isInnerClass()==true){
                String name = md.getNameAsString();
                System.out.println("inner class: "+name);
                return;//todo 如何处理inner class
            }//如果是inner class就返回
            String signature = pkgName+":"+md.getNameAsString();
            if(!mappings.containsKey(signature)){
                //如果当前类是新的 就新建
                //更新mapping， codeblocks， commitcodechang,以及添加pkt的classes属性
                CodeBlock codeBlock = new CodeBlock(codeBlocks.size()-1, CodeBlockType.Class);
                mappings.put(signature, codeBlock);
                PackageTime pkgTime = (PackageTime) mappings.get(pkgName).getLastHistory();
                ClassTime classTime = new ClassTime(signature, filePath, commitCodeChange, Operator.ADD_Class, codeBlock, mappings.get(pkgName));
                //更新package中的类名list, 如果是当前的commit，就直接更新，如果是上一个commit，那就新增packageTime
                if(pkgTime.getTime().getCommitID().equals(commitCodeChange.getCommitID())){
                    pkgTime.getClasses().add(codeBlock);
                }else{
                    PackageTime pkgTimeNew = new PackageTime(signature, filePath, commitCodeChange, Operator.ADD_Class, mappings.get(pkgName));
                    pkgTimeNew.setClasses(new ArrayList<>(pkgTime.getClasses()));
                    pkgTimeNew.getClasses().add(codeBlock);
                }
                codeBlock.addHistory(classTime);
                codeBlocks.add(codeBlock);
                codeChange.get(codeChange.size()-1).addCodeChange(classTime);
            }else{
                System.out.println("I don't know what to do...");//todo
            }

        }
    }
}
