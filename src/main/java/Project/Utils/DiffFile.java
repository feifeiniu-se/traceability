package Project.Utils;
import Constructor.Enums.FileType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class DiffFile {
    FileType type;// add, delete, rename, modify, derive
    String path;
    String content;
    String oldPath;
    String oldContent;
    List<String> preFiles;
    List<String> postFiles;

    public DiffFile(FileType type, String path, String content){
        this.type = type;
        this.path = path;
        this.content = content;
        this.oldPath = null;
        this.oldContent = null;
        preFiles = new ArrayList<>();
        postFiles = new ArrayList<>();
    }
    public DiffFile(FileType type, String path, String content, String oldPath, String oldContent){
        this.type = type;
        this.path = path;
        this.content = content;
        this.oldPath = oldPath;
        this.oldContent = oldContent;
        preFiles = new ArrayList<>();
        postFiles = new ArrayList<>();
    }

}
