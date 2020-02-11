package com.sloera.mng.core.utils.db;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DbUtils {
    private static Logger logger = LogManager.getLogger(DbUtils.class);
    //private static final MessageFormat formatLimitString = new MessageFormat("SELECT GLOBAL_TABLE.* FROM ( SELECT ROW_NUMBER() OVER( {0}) AS __MYSEQ__,TEMP_TABLE.* FROM  ( {1} ) TEMP_TABLE) GLOBAL_TABLE WHERE GLOBAL_TABLE.__MYSEQ__>{2}");

    public DbUtils() {
    }

    public static String replaceFormatSqlOrderBy(String sql) {
        //`\s`匹配任何空白字符，包括空格、制表符、换页符等。与 [ \f\n\r\t\v] 等效。
        //`+`一次或多次匹配前面的字符或子表达式。例如，"zo+"与"zo"和"zoo"匹配，但与"z"不匹配。+ 等效于 {1,}。
        sql = sql.replaceAll("(\\s)+", " ");
        int index = sql.toLowerCase().lastIndexOf("order by");
        //如果order by在最后，count(1)后排序不影响效率。否则，去除内部所有排序。
        if (index > sql.toLowerCase().lastIndexOf(")")) {
            String sql1 = sql.substring(0, index);
            String sql2 = sql.substring(index);
            //`?`零次或一次匹配前面的字符或子表达式。例如，"do(es)?"匹配"do"或"does"中的"do"。? 等效于 {0,1}。
            //    删除所有内部排序
            sql2 = sql2.replaceAll("[oO][rR][dD][eE][rR] [bB][yY] [一-龥a-zA-Z0-9_.]+((\\s)+(([dD][eE][sS][cC])|([aA][sS][cC])))?(( )*,( )*[一-龥a-zA-Z0-9_.]+(( )+(([dD][eE][sS][cC])|([aA][sS][cC])))?)*", "");
            return sql1 + sql2;
        } else {//order by 在最后
            return sql;
        }
    }

    /*
     * @Description 获取数据库分页查询语句
     * @param connection: 数据库连接
     * @param page: 页数
     * @param rows: 行数
     * @param select: select语句
     * @param sqlExceptSelect: from后语句
     * @Return com.alibaba.fastjson.JSONObject
     * @Author SloeraN
     * @Date 2020/1/5 20:07
     */
    public static JSONObject getDbPaginate(Connection connection, int page, int rows, String select, String sqlExceptSelect) throws SQLException {
        String dataType = "Oracle";
        StringBuilder stringBuilder = new StringBuilder(255);
        DatabaseMetaData metaData = connection.getMetaData();
        if (null != metaData) {
            String name = metaData.getDatabaseProductName();
            if (StringUtils.isNotBlank(name)) {
                if (name.startsWith("Oracle")) {
                    dataType = "Oracle";
                    forOraclePaginateBetween(stringBuilder, page, rows, select, sqlExceptSelect);
                } else if (name.startsWith("MySQL")) {
                    dataType = "MySQL";
                    forMySQLPaginate(stringBuilder, page, rows, select, sqlExceptSelect);
                }
            } else {
                logger.warn("无匹配数据库类型");
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", dataType);
        jsonObject.put("sql", stringBuilder.toString());
        logger.info("当前连接的数据库：" + dataType);
        return jsonObject;
    }

    private static void forMySQLPaginate(StringBuilder stringBuilder, int page, int rows, String select, String sqlExceptSelect) {
        int offset = (page - 1) * rows;
        stringBuilder.append(select).append(" ");
        stringBuilder.append(sqlExceptSelect);
        stringBuilder.append(" limit ").append(offset).append(", ").append(rows);
    }

    /*
     * @Description oracle通过两次rownum分页
     * @param stringBuilder: 构建后的sql
     * @param page: 第几页
     * @param rows: 行数
     * @param select: select语句
     * @param sqlExceptSelect: from后语句
     * @Return void
     * @Author SloeraN
     * @Date 2020/1/5 20:21
     */
    private static void forOraclePaginate(StringBuilder stringBuilder, int page, int rows, String select, String sqlExceptSelect) {
        int start = (page - 1) * rows + 1;
        int end = page * rows;
        stringBuilder.append("select * from (");
        stringBuilder.append(" select row_.*, rownum rownum_ from ( ");
        stringBuilder.append(select).append(",count(1) over() as totalRow_ ").append(sqlExceptSelect);
        stringBuilder.append(" ) row_ where rownum <= ").append(end).append(") table_alias");
        stringBuilder.append(" where table_alias.rownum_ >= ").append(start);
    }

    /*
     * @Description oracle通过rownum between分页
     * @param stringBuilder: 构建后的sql
     * @param page: 第几页
     * @param rows: 行数
     * @param select: select语句
     * @param sqlExceptSelect: from后语句
     * @Return void
     * @Author SloeraN
     * @Date 2020/1/5 20:21
     */
    private static void forOraclePaginateBetween(StringBuilder stringBuilder, int page, int rows, String select, String sqlExceptSelect) {
        int start = (page - 1) * rows + 1;
        int end = page * rows;
        //stringBuilder.append("select * from (");
        stringBuilder.append(" select row_.*, rownum rownum_ from ( ");
        stringBuilder.append(select).append(" ").append(sqlExceptSelect);
        stringBuilder.append(" ) row_ where rownum_ between ").append(start).append(" and ").append(end);
    }
}
