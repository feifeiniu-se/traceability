//package Persistence;
//
//import Util.sqlite.SqliteHelper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TableCreator {
//    private static final Logger logger = LoggerFactory.getLogger(TableCreator.class);
//
//    private SqliteHelper helper;
//
//    public TableCreator(String dbFilePath) {
//        try {
//            helper = new SqliteHelper(dbFilePath);
//        } catch (SQLException | ClassNotFoundException e) {
//            logger.error(e.toString());
//        }
//    }
//
//    public void createTables() {
//        List<String> sqls = new ArrayList<>(
//                // CodeBlockTime
//                "drop table if exists CodeBlockTime;",
//                "create table CodeBlockTime\n" +
//                        "(\n" +
//                        "    id              integer primary key not null, -- CodeBlockTime的id\n" +
//                        "    name            varchar(200),                 -- 名称\n" +
//                        "    commitId        varchar(45),                  -- commit hash值\n" +
//                        "    refactorType    varchar(50),                  -- 重构的类型，eg. Add_Package\n" +
//                        "    parentCodeBlock integer,                      -- 当前CodeBlockTime的父亲，指向某一个CodeBlock的id\n" +
//                        "    owner           integer,                      -- 当前CodeBlockTime的拥有者，指向某一个CodeBlock的id\n" +
//                        "    parameters      varchar(500)                  -- 只有Method类型的CodeBlockTime有这个值\n" +
//                        ");",
//                // CodeBlock
//                "drop table if exists CodeBlock;",
//                "create table CodeBlock\n" +
//                        "(\n" +
//                        "    id   integer primary key not null, -- CodeBlock的id\n" +
//                        "    type varchar(20)                   -- CodeBlock的类型，eg. Package/Class/Method/Attribute\n" +
//                        ");",
//                // CommitCodeChange
//                "drop table if exists CommitCodeChange;\n" +
//                        "create table CommitCodeChange\n" +
//                        "(\n" +
//                        "    commitId   varchar(45) primary key not null, -- commit hash值\n" +
//                        "    preCommit  varchar(45),                      -- 上一个CommitCodeChange\n" +
//                        "    postCommit varchar(45)                       -- 下一个CommitCodeChange\n" +
//                        ");",
//                // CodeBlockTime_link
//                "drop table if exists CodeBlockTime_link;"
//
//        );
//        helper.executeUpdate();
//    }
//}
