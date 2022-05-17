package Util.sqlite;

import java.sql.ResultSet;

/**
 * 结果集处理类
 * @param <T>
 */
public interface ResultSetExtractor<T> {
    T extractData(ResultSet resultSet);
}
