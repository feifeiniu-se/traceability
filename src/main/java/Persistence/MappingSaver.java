package Persistence;

import Model.CommitCodeChange;
import Util.sqlite.SqliteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MappingSaver {
    private static final Logger logger = LoggerFactory.getLogger(CodeBlockSaver.class);
    private SqliteHelper helper;

    public MappingSaver(String dbFilePath) {
        try {
            helper = new SqliteHelper(dbFilePath);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    public void save(Map<String,String> mapping) {


    }
}
