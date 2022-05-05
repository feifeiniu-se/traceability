package Model;

import java.util.ArrayList;
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
        if(this.getLastHistory()==null){//如果history是空
            history.add(cbt);
            cbt.setPre(null);
            cbt.setOwner(this);
        }else{//如果不是空
            this.getLastHistory().setPost(cbt);
            cbt.setPre(this.getLastHistory());
            history.add(cbt);
            cbt.setOwner(this);
        }
        //获取最后一个history，更新pre post
    }

    public CodeBlockTime getLastHistory() {
        return history.isEmpty()?null:history.get(history.size()-1);
    }


}
