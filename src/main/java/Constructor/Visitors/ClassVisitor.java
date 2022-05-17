package Constructor.Visitors;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Model.ClassTime;
import Model.CodeBlock;
import Model.CommitCodeChange;
import Model.PackageTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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

    public void classVisitor(String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings) {
        this.codeBlocks = codeBlocks;
        this.codeChange = codeChange;
        this.mappings = mappings;
        this.commitCodeChange = codeChange.get(codeChange.size() - 1); //获得当前commit的内容
        JavaParser javaParser = new JavaParser();
        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();//done
        System.out.println(javaParser.parse(fileContent).getResult());
        pkgName = cu.getPackageDeclaration().get().getNameAsString();
        assert mappings.containsKey(pkgName);
        pkgBlock = mappings.get(pkgName);

        Visitor visitor = new Visitor();
        visitor.visit(cu, null);// 遍历完文件的AST树，初步获得信息
    }

    private class Visitor extends VoidVisitorAdapter<Void> {

        @Override
        //class or interface
        public void visit(ClassOrInterfaceDeclaration md, Void arg) {
            super.visit(md, arg);
            if (md.isInnerClass() == true) {
                String name = md.getNameAsString();
//                System.out.println("inner class: " + name);
                return;//done 如何处理inner class
            }//如果是inner class就返回
            String name = md.getNameAsString();
            String signature = pkgName + "." + name;
            if (!mappings.containsKey(signature)) {
                //creat new classBlock, update package
                CodeBlock classBlock = new CodeBlock(codeBlocks.size() + 1, CodeBlockType.Class);
                mappings.put(signature, classBlock);
                codeBlocks.add(classBlock);
                ClassTime classTime = new ClassTime(name, commitCodeChange, Operator.Add_Class, classBlock, pkgBlock);//create classTime, add to classBlock, commitTime, update parentBlock
            }

        }
    }
}
