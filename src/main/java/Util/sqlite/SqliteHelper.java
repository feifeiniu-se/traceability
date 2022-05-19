package Util.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteHelper {
    private static final Logger logger = LoggerFactory.getLogger(SqliteHelper.class);

    private Connection connection;
    private Statement statement;  // Statement时Java执行数据库操作的一个重要接口，用于在已经建立数据库连接的基础上，向数据库发送要执行的SQL语句，执行静态SQL语句并返回它生成的结果
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;  // ResultSet用于访问从关系型数据库检索的查询结果，使用其提供的导航和获取方法，我们可以一个一个迭代和访问数据库记录，也可用于更新数据
    private String dbFilePath;

    public SqliteHelper(String dbFilePath) throws SQLException, ClassNotFoundException {
        this.dbFilePath = dbFilePath;
        connection = getConnection(dbFilePath);
    }

    private Connection getConnection(String dbFilePath) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null) {
            logger.info("Create new connection.");
            connection = getConnection(dbFilePath);
        }
        logger.info("Connection already exists.");
        return connection;
    }

    /**
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private Statement getStatement() throws SQLException, ClassNotFoundException {
        if (statement == null) {
            statement = getConnection().createStatement();
        }
        return statement;
    }

    public PreparedStatement getPreparedStatement(String preparedStatementStr) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        preparedStatement = connection.prepareStatement(preparedStatementStr);
        return preparedStatement;
    }

    /**
     * 执行select查询，返回结果列表
     *
     * @param sql
     * @param resultSetExtractor
     * @param <T>
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public <T> T executeQuery(String sql,ResultSetExtractor<T> resultSetExtractor) throws SQLException, ClassNotFoundException {
        try {
            resultSet = getStatement().executeQuery(sql);
            T result = resultSetExtractor.extractData(resultSet);
            return result;
        }finally {
//            destroyed();
        }
    }

    public <T> List<T> executeQuery(String sql, RowMapper<T> mapper) throws SQLException, ClassNotFoundException {
        List<T> results = new ArrayList<>();
        try {
            resultSet = getStatement().executeQuery(sql);
            while (resultSet.next()) {
                results.add(mapper.mapRow(resultSet, resultSet.getRow()));
            }
        }finally {
//            destroyed();
        }
        return results;
    }

    /**
     * 由客户端构造PreparedStatement，在这里提交并执行
     * @param preparedStatement
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void executePreparedStatement(PreparedStatement preparedStatement) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        preparedStatement.executeBatch();
        connection.setAutoCommit(true);
//        destroyed();
    }



    /**
     * 执行多个SQL更新语句
     *
     * @param sqls
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void executeUpdate(String... sqls) throws SQLException, ClassNotFoundException {
        try {
            for (String sql : sqls) {
                getStatement().executeUpdate(sql);
            }
        }finally {
//            destroyed();
        }
    }

    /**
     * 执行数据库更新SQL List
     *
     * @param sqls
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void executeUpdate(List<String> sqls) throws SQLException, ClassNotFoundException {
        try {
            for (String sql : sqls) {
                getStatement().executeUpdate(sql);
            }
        }finally {
//            destroyed();
        }
    }

    /**
     * 数据库资源关闭和释放
     */
    public void destroyed() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException e) {
            logger.error("Sqlite数据库关闭时异常: ",e);
        }
    }

//    public static void main(String[] args) {
//        Class.forName("org.sqlite.JDBC");
//        String db = ""
//    }
}
