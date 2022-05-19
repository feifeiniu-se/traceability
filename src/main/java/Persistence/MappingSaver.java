package Persistence;

import Util.sqlite.SqliteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public void save(Map<String, String> mapping) {
        try {
            PreparedStatement preparedStatement = helper.getPreparedStatement("insert into Mapping values(?,?);");
            for (String key : mapping.keySet()) {
                String value = mapping.get(key);
                preparedStatement.setString(1,key);
                preparedStatement.setString(2,value);
                preparedStatement.addBatch();
            }
            helper.executePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }

    // todo: query
//    public void query() {
//        Map<String,String> mapping = null;
//        try {
//            mapping = helper.executeQuery("select * from Mapping", (resultSet, index) -> resultSet.getString("commitId"));
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println(mapping);
//    }
}
