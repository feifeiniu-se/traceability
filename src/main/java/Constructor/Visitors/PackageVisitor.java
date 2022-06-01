package Constructor.Visitors;

import Constructor.Enums.CodeBlockType;
import Constructor.Enums.Operator;
import Constructor.Handler;
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

    private List<CodeBlock> codeBlocks;
    private HashMap<String, CodeBlock> mappings;
    private CommitCodeChange commitTime;

    public void packageVisitor( String fileContent, List<CodeBlock> codeBlocks, List<CommitCodeChange> codeChange, HashMap<String, CodeBlock> mappings) {
        this.codeBlocks = codeBlocks;
        this.mappings = mappings;
        this.commitTime = codeChange.get(codeChange.size()-1); //获得当前commit的内容
        JavaParser javaParser=new JavaParser();
//        System.out.println(fileContent);
        CompilationUnit cu = javaParser.parse(fileContent).getResult().get();
        if(cu.getPackageDeclaration().isPresent()){
            Visitor visitor = new Visitor();
            visitor.visit(cu, null);// 遍历完文件的AST树，初步获得信息
        }else{
            String pkgName = "default.package";
            //如果文件中不包含包信息 就设置为默认包 default.package
            if(!mappings.containsKey(pkgName)){
                Operator.Add_Package.apply(codeBlocks, mappings, null, commitTime, pkgName);
            }

        }

    }
    private class Visitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(PackageDeclaration md, Void arg){
            super.visit(md, arg);
            String name = md.getNameAsString();
            if(!mappings.containsKey(name)){
                //if mappings don't contain package, then create
                Operator.Add_Package.apply(codeBlocks, mappings, null, commitTime, name);
            }
        }
    }
}
