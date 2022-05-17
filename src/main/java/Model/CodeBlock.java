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
        System.out.println("oldName: "+oldName);
        System.out.println("newName:" + newName);
        assert mappings.containsKey(oldName);
        mappings.put(newName, this);

        if (!(this.getLastHistory().getPackages() ==null)) {
            for (CodeBlock pkg : this.getLastHistory().getPackages()) {
                pkg.updateMappings(mappings, pkg.getLastHistory().getSignature(), pkg.getLastHistory().getSignature().replace(oldName, newName));
            }
        }
        if (!(this.getLastHistory().getClasses()==null)) {
            for (CodeBlock classBlock : this.getLastHistory().getClasses()) {
                classBlock.updateMappings(mappings, classBlock.getLastHistory().getSignature(), classBlock.getLastHistory().getSignature().replace(oldName, newName));
            }
        }
        if(!(this.getLastHistory().getMethods()==null)){
            for(CodeBlock methodBlock: this.getLastHistory().getMethods()){
                System.out.println("methodName: "+methodBlock.getLastHistory().getName());
                System.out.println("methodType: "+methodBlock.getLastHistory().getRefactorType());
                System.out.println("methodSig: "+methodBlock.getLastHistory().getSignature());
                System.out.println("refactType: "+this.getLastHistory().getRefactorType());
                System.out.println("clasName:" + this.getLastHistory().getName());
                methodBlock.updateMappings(mappings, methodBlock.getLastHistory().getSignature(), methodBlock.getLastHistory().getSignature().replace(oldName, newName));
            }
        }
        if(!(this.getLastHistory().getAttributes()==null)){
            for(CodeBlock attriBlock: this.getLastHistory().getAttributes()){
                attriBlock.updateMappings(mappings, attriBlock.getLastHistory().getSignature(), attriBlock.getLastHistory().getSignature().replace(oldName, newName));
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
