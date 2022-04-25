package Project.Utils;
import lombok.Data;

import java.util.HashMap;


@Data
public class DiffFile {
    String type;// add, delete, rename, modify, derive
    String path;
    String content;
    String oldPath;
    String oldContent;

    public DiffFile(String type, String path, String content){
        this.type = type;
        this.path = path;
        this.content = content;
        this.oldPath = null;
        this.oldContent = null;
    }
    public DiffFile(String type, String path, String content, String oldPath, String oldContent){
        this.type = type;
        this.path = path;
        this.content = content;
        this.oldPath = oldPath;
        this.oldContent = oldContent;
    }

}
