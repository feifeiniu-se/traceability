package Model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CodeBlock {
    Integer codeBlockID;
    String type;//package, class, method, attribute
    String status;//null delete 用来记录被删除的block
    List<CodeBlockTime> history;

    public CodeBlock(Integer id) {
        this.codeBlockID = id;
        type = "ADD";
        status = null;
        history = new ArrayList<>();
    }

    public void add(CodeBlockTime cbt) {
        //TODO add
        history.get(history.size() - 1).setPost(cbt);
        cbt.setPre(history.get(history.size() - 1));
        history.add(cbt);
        //获取最后一个history，更新pre post
    }

    public CodeBlockTime getLastHistory() {
        return history.isEmpty()?null:history.get(history.size()-1);
    }

}
