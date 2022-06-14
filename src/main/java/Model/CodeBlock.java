package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import Constructor.Enums.CodeBlockType;
import lombok.Data;

/**
 * 横向关系（同一个commit的不同代码块之间）
 * 把项目分为多个不同的CodeBlock，分割粒度为package、class、method、attribute
 */
@Data
public class CodeBlock {
    Integer codeBlockID;
    CodeBlockType type;//package, class, method, attribute
    List<CodeBlockTime> history;

    public CodeBlock(Integer id, CodeBlockType tp) {
        this.codeBlockID = id;
        type = tp;
        history = new ArrayList<>();
    }

    public void addHistory(CodeBlockTime cbt) {
        //done add
        if (this.getLastHistory() == null) {//如果history是空
            history.add(cbt);
            cbt.setPre(null);
            cbt.setOwner(this);
        } else {//如果不是空
            this.getLastHistory().setPost(cbt);
            cbt.setPre(this.getLastHistory());
            history.add(cbt);
            cbt.setOwner(this);
        }
        //获取最后一个history，更新pre post
    }

    public CodeBlockTime getLastHistory() {
        return history.isEmpty() ? null : history.get(history.size() - 1);
    }

    public void updateMappings(HashMap<String, CodeBlock> mappings, String oldName, String newName) {
//        System.out.println("oldName: "+this.getType()+":"+oldName);
//        System.out.println("newName:"+this.getType()+":" + newName);
//        assert mappings.containsKey(oldName);
        mappings.put(newName, this);
//        System.out.println(newName);

        if (!(this.getLastHistory().getPackages() ==null)) {
            for (CodeBlock pkg : this.getLastHistory().getPackages()) {
                String pkgName = pkg.getLastHistory().getName();
                assert pkgName.contains(oldName);
                pkg.updateMappings(mappings, pkgName, pkgName.replace(oldName, newName));
            }
        }
        if (!(this.getLastHistory().getClasses()==null)) {
//            this.getHistory().forEach(e->System.out.println("refactType: "+e.getRefactorType()+"::"+e.getSignature()));
//            this.getLastHistory().getClasses().forEach(e->System.out.println("classes:"+e.getLastHistory().getName()));
            for (CodeBlock classBlock : this.getLastHistory().getClasses()) {
                String className = classBlock.getLastHistory().getName();
                classBlock.updateMappings(mappings, oldName+"."+className, newName+"."+className);
            }
        }
        if(!(this.getLastHistory().getMethods()==null)){
//            this.getLastHistory().getMethods().forEach(e->System.out.println("methods:"+e.getLastHistory().getName()));
//            this.getHistory().forEach(e->System.out.println("refactType: "+e.getRefactorType()));
            for(CodeBlock methodBlock: this.getLastHistory().getMethods()){
                String methodName = methodBlock.getLastHistory().getName();
//                System.out.println("methodName: "+methodBlock.getLastHistory().getName());
//                System.out.println("methodType: "+methodBlock.getLastHistory().getRefactorType());
//                System.out.println("methodSig: "+methodBlock.getLastHistory().getSignature());
//                methodBlock.getHistory().forEach(e->System.out.println("methodRefact: "+e.getRefactorType()+": "+e.getName()));
                methodBlock.updateMappings(mappings, oldName+":"+methodName, newName+":"+methodName);
            }
        }
        if(!(this.getLastHistory().getAttributes()==null)){
//            System.out.println("className:" + this.getLastHistory().getName());
//            this.getHistory().forEach(e->System.out.println("refactType: "+e.getRefactorType()));
//            this.getLastHistory().getAttributes().forEach(e->System.out.println("attribute: "+e.getLastHistory().getSignature()));
            for(CodeBlock attriBlock: this.getLastHistory().getAttributes()){
                String attriName = attriBlock.getLastHistory().getName();
//                System.out.println("attriName: "+attriBlock.getLastHistory().getName());
//                System.out.println("attriType: "+attriBlock.getLastHistory().getRefactorType());
//                System.out.println("attriSig: "+attriBlock.getLastHistory().getSignature());
//                attriBlock.getHistory().forEach(e->System.out.println("AttriRefactoring: "+e.getRefactorType()+" "+"AttriSig: "+e.getSignature()));

                attriBlock.updateMappings(mappings, oldName+":"+attriName, newName+":"+attriName);
            }
        }

    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null||getClass()!=o.getClass()) return false;
        CodeBlock codeBlock = (CodeBlock) o;
        return Objects.equals(codeBlockID, codeBlock.codeBlockID) && Objects.equals(type, codeBlock.type);
    }
    @Override
    public int hashCode(){
        return Objects.hash(codeBlockID, type);
    }

}
