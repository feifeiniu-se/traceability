package Persistence;

import Model.*;
import Util.sqlite.SqliteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CodeBlockSaver {
    private static final Logger logger = LoggerFactory.getLogger(CodeBlockSaver.class);
    private SqliteHelper helper;
    private Map<CodeBlockTime, Integer> codeBlockTime2Id = new HashMap<>();

    public CodeBlockSaver(String dbFilePath) {
        try {
            helper = new SqliteHelper(dbFilePath);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }
    public void close(){
        helper.destroyed();
    }

    public void save(List<CodeBlock> codeBlocks) {
        // 1. 保存 CodeBlock
        saveCodeBlock(codeBlocks);

        for (CodeBlock codeBlock : codeBlocks) {
            List<CodeBlockTime> history = codeBlock.getHistory();
            // 2. 保存 CodeBlockTime
            saveCodeBlockTime(history);
        }

        // 3. 保存 CodeBlockTimeChild & CodeBlockTime_link
        for (CodeBlock codeBlock : codeBlocks) {
            List<CodeBlockTime> history = codeBlock.getHistory();
            saveCodeBlockTimeChild(history);
            saveCodeBlockTime_link(history);
        }
    }

    private void saveCodeBlock(List<CodeBlock> codeBlocks) {
        try {
            PreparedStatement preparedStatement = helper.getPreparedStatement("insert or replace into CodeBlock (id,type) values(?,?);");
            for (CodeBlock codeBlock : codeBlocks) {
                preparedStatement.setInt(1, codeBlock.getCodeBlockID());
                preparedStatement.setString(2, codeBlock.getType().toString());
                preparedStatement.addBatch();
            }
            helper.executePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }

    private void saveCodeBlockTime(List<CodeBlockTime> codeBlockTimes) {
        try {
            PreparedStatement preparedStatement = helper.getPreparedStatement("insert into CodeBlockTime (name,commitId,refactorType,parentCodeBlock,owner,parameters) values(?,?,?,?,?,?);");
            for (CodeBlockTime codeBlockTime : codeBlockTimes) {
                preparedStatement.setString(1, codeBlockTime.getName());
                preparedStatement.setString(2, codeBlockTime.getTime().getCommitID());
                preparedStatement.setString(3, codeBlockTime.getRefactorType().toString());
                CodeBlock parentCodeBlock = codeBlockTime.getParentCodeBlock();
                if (parentCodeBlock != null) {
                    preparedStatement.setInt(4, parentCodeBlock.getCodeBlockID());
                }
                CodeBlock owner = codeBlockTime.getOwner();
                if (owner != null) {
                    preparedStatement.setInt(5, owner.getCodeBlockID());
                }
                if (parentCodeBlock == null && owner == null) {
                    logger.error("parentCodeBlock & owner = null");
                }
                if (codeBlockTime instanceof MethodTime) {
                    preparedStatement.setString(6, ((MethodTime) codeBlockTime).getParameters());
                }
                preparedStatement.executeUpdate();  // 立即执行
                int id = helper.executeQuery("select last_insert_rowid() as id from CodeBlockTime limit 1;", resultSet -> {
                    int id1 = -1;
                    try {
                        id1 = resultSet.getInt("id");
                    } catch (SQLException e) {
                        logger.error(e.toString());
                    }
                    return id1;
                });
                codeBlockTime2Id.put(codeBlockTime, id);
            }
            helper.destroyed();  // 手动关闭连接
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }

    private void saveCodeBlockTimeChild(List<CodeBlockTime> codeBlockTimes) {
        try {
            PreparedStatement preparedStatement = helper.getPreparedStatement("insert into CodeBlockTimeChild (codeBlockTimeId,codeBlockChildId,codeBlockChildType) values(?,?,?);");  // ( 父亲, 孩子, link类型 )
            for (CodeBlockTime codeBlockTime : codeBlockTimes) {
                preparedStatement.setInt(1, codeBlockTime2Id.get(codeBlockTime));
                if (codeBlockTime instanceof AttributeTime) {
                    constructPreparedStatementForAttributeTime(preparedStatement, (AttributeTime) codeBlockTime);
                } else if (codeBlockTime instanceof MethodTime) {
                    constructPreparedStatementForMethodTime(preparedStatement, (MethodTime) codeBlockTime);
                } else if (codeBlockTime instanceof ClassTime) {
                    constructPreparedStatementForClassTime(preparedStatement, (ClassTime) codeBlockTime);
                } else if (codeBlockTime instanceof PackageTime) {
                    constructPreparedStatementForPackageTime(preparedStatement, (PackageTime) codeBlockTime);
                }
            }
            helper.executePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }

    private void constructPreparedStatementForAttributeTime(PreparedStatement preparedStatement, AttributeTime attributeTime) throws SQLException {
        preparedStatement.setInt(1, codeBlockTime2Id.get(attributeTime));
        CodeBlock declareType = attributeTime.getDeclareType();
        if (declareType != null) {
            preparedStatement.setInt(2, declareType.getCodeBlockID());
            preparedStatement.setString(3, "declareType");
        }
        preparedStatement.addBatch();
    }

    private void constructPreparedStatementForMethodTime(PreparedStatement preparedStatement, MethodTime methodTime) throws SQLException {
        int codeBlockTimeId = codeBlockTime2Id.get(methodTime);
        Set<CodeBlock> callers = methodTime.getCallers();
        Set<CodeBlock> callees = methodTime.getCallees();
        Set<CodeBlock> parameterTypes = methodTime.getParameterType();

        for (CodeBlock caller : callers) {
            preparedStatement.setInt(1, codeBlockTimeId);
            preparedStatement.setInt(2, caller.getCodeBlockID());
            preparedStatement.setString(3, "caller");
            preparedStatement.addBatch();
        }

        for (CodeBlock callee : callees) {
            preparedStatement.setInt(1, codeBlockTimeId);
            preparedStatement.setInt(2, callee.getCodeBlockID());
            preparedStatement.setString(3, "callee");
            preparedStatement.addBatch();
        }

        for (CodeBlock parameterType : parameterTypes) {
            preparedStatement.setInt(1, codeBlockTimeId);
            preparedStatement.setInt(2, parameterType.getCodeBlockID());
            preparedStatement.setString(3, "parameterType");
            preparedStatement.addBatch();
        }
    }

    private void constructPreparedStatementForClassTime(PreparedStatement preparedStatement, ClassTime classTime) throws SQLException {
        int codeBlockTimeId = codeBlockTime2Id.get(classTime);
        Set<CodeBlock> classes = classTime.getClasses();
        Set<CodeBlock> methods = classTime.getMethods();
        Set<CodeBlock> attributes = classTime.getAttributes();

        for (CodeBlock class_ : classes) {
            preparedStatement.setInt(1, codeBlockTimeId);
            preparedStatement.setInt(2, class_.getCodeBlockID());
            preparedStatement.setString(3, "class");
            preparedStatement.addBatch();
        }

        for (CodeBlock method : methods) {
            preparedStatement.setInt(1, codeBlockTimeId);
            preparedStatement.setInt(2, method.getCodeBlockID());
            preparedStatement.setString(3, "method");
            preparedStatement.addBatch();
        }

        for (CodeBlock attribute : attributes) {
            preparedStatement.setInt(1, codeBlockTimeId);
            preparedStatement.setInt(2, attribute.getCodeBlockID());
            preparedStatement.setString(3, "attribute");
            preparedStatement.addBatch();
        }
    }

    private void constructPreparedStatementForPackageTime(PreparedStatement preparedStatement, PackageTime packageTime) throws SQLException {
        int codeBlockTimeId = codeBlockTime2Id.get(packageTime);
        Set<CodeBlock> classes = packageTime.getClasses();
        Set<CodeBlock> packages = packageTime.getPackages();

        for (CodeBlock class_ : classes) {
            preparedStatement.setInt(1, codeBlockTimeId);
            preparedStatement.setInt(2, class_.getCodeBlockID());
            preparedStatement.setString(3, "class");
            preparedStatement.addBatch();
        }

        for (CodeBlock package_ : packages) {
            preparedStatement.setInt(1, codeBlockTimeId);
            preparedStatement.setInt(2, package_.getCodeBlockID());
            preparedStatement.setString(3, "package");
            preparedStatement.addBatch();
        }
    }

    private void saveCodeBlockTime_link(List<CodeBlockTime> codeBlockTimes) {
        try {
            PreparedStatement preparedStatement = helper.getPreparedStatement("insert into CodeBlockTime_link (source,target,link_type) values(?,?,?);");  // ( 父亲, 孩子, link类型 )
            for (CodeBlockTime codeBlockTime : codeBlockTimes) {
                CodeBlockTime post = codeBlockTime.getPost();  // 当前CodeBlockTime的孩子
                Set<CodeBlockTime> deriveeSet = codeBlockTime.getDerivee();  // 当前CodeBlockTime所衍生出的集合
                if (post != null) {
                    preparedStatement.setInt(1, codeBlockTime2Id.get(codeBlockTime));
                    preparedStatement.setInt(2, codeBlockTime2Id.get(post));
                    preparedStatement.setInt(3, 0);
                    preparedStatement.addBatch();
                }

                if (!deriveeSet.isEmpty()) {
                    for (CodeBlockTime derivee : deriveeSet) {
                        preparedStatement.setInt(1, codeBlockTime2Id.get(codeBlockTime));
                        preparedStatement.setInt(2, codeBlockTime2Id.get(derivee));
                        preparedStatement.setInt(3, 1);
                        preparedStatement.addBatch();
                    }
                }
            }
            helper.executePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
    }
}
