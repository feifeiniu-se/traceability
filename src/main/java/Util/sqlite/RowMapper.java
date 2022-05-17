package Util.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集行数据处理类
 *
 * @param <T>
 */
public interface RowMapper<T> {
    T mapRow(ResultSet resultSet, int index) throws SQLException;
}
