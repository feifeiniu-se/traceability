package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Constructor.Enums.CodeBlockType;
import lombok.Data;

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
        assert mappings.containsKey(oldName);
//        System.out.println(this.getLastHistory().getMethods());
//        System.out.println(oldName+" *** "+newName);
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
                methodBlock.updateMappings(mappings, methodBlock.getLastHistory().getSignature(), methodBlock.getLastHistory().getSignature().replace(oldName, newName));
            }
        }
        if(!(this.getLastHistory().getAttributes()==null)){
            for(CodeBlock attriBlock: this.getLastHistory().getAttributes()){
                attriBlock.updateMappings(mappings, attriBlock.getLastHistory().getSignature(), attriBlock.getLastHistory().getSignature().replace(oldName, newName));
            }
        }

    }


}
