package Model;

import java.util.List;
import lombok.Data;

@Data
public class CodeBlock {
    String codeBlockID;
    String type;//package, class, method, attribute
    String status = null;//null delete 用来记录被删除的block
    List<CodeBlockTime> history;


    public void add(CodeBlockTime cbt){
        //TODO add
        history.get(history.size()-1).setPost(cbt);
        cbt.setPre(history.get(history.size()-1));
        history.add(cbt);
        //获取最后一个history，更新pre post
    }

    public CodeBlockTime getLastHistory(){
        return history.get(history.size()-1);
    }

}
