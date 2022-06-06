package Persistence;

import Model.CodeBlock;
import Util.sqlite.SqliteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MappingSaver {
    private static final Logger logger = LoggerFactory.getLogger(MappingSaver.class);
    private SqliteHelper helper;

    public MappingSaver(String dbFilePath) {
        try {
            helper = new SqliteHelper(dbFilePath);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
    }
    public void close(){
        helper.destroyed();
    }

    public void save(HashMap<String, CodeBlock> mapping) {
        try {
            PreparedStatement preparedStatement = helper.getPreparedStatement("insert into Mapping values(?,?);");
            for (String key : mapping.keySet()) {
                CodeBlock codeBlock = mapping.get(key);
                preparedStatement.setString(1,key);
                preparedStatement.setInt(2,codeBlock.getCodeBlockID());
                preparedStatement.addBatch();
            }
            helper.executePreparedStatement(preparedStatement);
            helper.destroyed();  // 手动关闭连接
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
