package Persistence;

import Model.CodeBlock;
import Util.sqlite.SqliteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class CodeBlockSaver {
    private static final Logger logger = LoggerFactory.getLogger(CodeBlockSaver.class);
    private SqliteHelper helper;

    public CodeBlockSaver(String dbFilePath) {
        try {
            helper = new SqliteHelper(dbFilePath);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }

    public void save(List<CodeBlock> codeBlocks) {

    }
}
