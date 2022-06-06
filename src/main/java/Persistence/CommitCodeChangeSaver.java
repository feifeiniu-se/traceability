package Persistence;

import Model.CommitCodeChange;
import Util.sqlite.SqliteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CommitCodeChangeSaver {
    private static final Logger logger = LoggerFactory.getLogger(CommitCodeChangeSaver.class);
    private SqliteHelper helper;

    public CommitCodeChangeSaver(String dbFilePath) {
        try {
            helper = new SqliteHelper(dbFilePath);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }

    public void close(){
        helper.destroyed();
    }

    public void save(List<CommitCodeChange> commitCodeChanges) {
        try {
            PreparedStatement preparedStatement = helper.getPreparedStatement("insert or replace into CommitCodeChange values(?,?,?);");
            // 为了减少数据库连接的I/O开销，一般会把多条数据插入放在一条SQL语句中一次执行。
            // 开始事务后，进行大量操作的语句都保存在内存中，当提交时才全部写入数据库，此时，数据库文件也就只用打开一次。
            for (CommitCodeChange commitCodeChange : commitCodeChanges) {
                preparedStatement.setString(1, commitCodeChange.getCommitID());
                CommitCodeChange preCommit = commitCodeChange.getPreCommit();
                if (preCommit != null) {
                    preparedStatement.setString(2, preCommit.getCommitID());
                } else {
                    preparedStatement.setString(2, null);
                }
                CommitCodeChange postCommit = commitCodeChange.getPostCommit();
                if (postCommit != null) {
                    preparedStatement.setString(3, postCommit.getCommitID());
                } else {
                    preparedStatement.setString(3, null);
                }
                preparedStatement.addBatch();
            }
            helper.executePreparedStatement(preparedStatement);
            helper.destroyed();  // 手动关闭连接
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }

    public void query() {
        List<String> commitCodeChangeIds = null;
        try {
            commitCodeChangeIds = helper.executeQuery("select commitId from CommitCodeChange", (resultSet, index) -> resultSet.getString("commitId"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(commitCodeChangeIds);
    }
}
