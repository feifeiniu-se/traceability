package Project.Utils;
import Constructor.Enums.FileType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class DiffFile {
    FileType type;// add, delete, rename, modify, derive
    String path;
    String content;
    String oldPath;
    String oldContent;


    public DiffFile(FileType type, String path, String content){//仅用于项目初始状态 全部都是add
        this.type = type;
        this.path = path;
        this.content = content;
        this.oldPath = null;
        this.oldContent = null;
    }
    public DiffFile(FileType type, String path, String content, String oldPath, String oldContent){
        this.type = type;
        this.path = path;
        this.content = content;
        this.oldPath = oldPath;
        this.oldContent = oldContent;
    }

}
