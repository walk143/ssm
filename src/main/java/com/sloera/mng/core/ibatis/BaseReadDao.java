package com.sloera.mng.core.ibatis;

import com.alibaba.fastjson.JSONObject;
import com.sloera.mng.core.db.Record;
import com.sloera.mng.core.db.RecordBuilder;
import com.sloera.mng.core.page.Page;
import com.sloera.mng.core.utils.db.DbUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseReadDao<E> {
    static final Object[] object = new Object[0];
    @Autowired
    @Qualifier("slaveSqlSessionTemplate")
    public SqlSessionTemplate slaveSqlSessionTemplate;

    public BaseReadDao() {
    }

    private SqlSession getSession(SqlSessionTemplate var1, boolean var2) {
        return (var1 == null ? this.slaveSqlSessionTemplate : var1.getSqlSessionFactory().openSession(ExecutorType.SIMPLE, var2));
//        return (SqlSession) (var1.getSqlSessionFactory().openSession(ExecutorType.BATCH, var2));
    }
    //未传入数据库链接
    public <E>List<E> selectList(String var1, Object var2){
        SqlSession var3 = this.getSession(null, false);
        return var3.selectList(var1, var2);
    }
    public <E>List<E> selectList(SqlSessionTemplate var1, String var2, Object var3){
        SqlSession var4 = this.getSession(var1, false);
        return var4.selectList(var2, var3);
    }
    public <E> List<E> selectList(String var1) {
        SqlSession var2 = this.getSession(null, false);
        return var2.selectList(var1);
    }
    //只传入一个sql
    public <E> List<E> selectList(SqlSessionTemplate var1, String var2) {
        SqlSession var3 = this.getSession(null, false);
        return var3.selectList(var2);
    }

    public <E> E selectOne(String var1) {
        SqlSession var2 = this.getSession(null, false);
        return var2.selectOne(var1);
    }

    public <E> E selectOne(String var1, Object var2) {
        SqlSession var3 = this.getSession(null, false);
        return var3.selectOne(var1, var2);
    }

    public <E> E selectOne(SqlSessionTemplate var1, String var2, Object var3) {
        SqlSession var4 = this.getSession(var1, false);
        return var4.selectOne(var2, var3);
    }

    public <E> E selectOne(SqlSessionTemplate var1, String var2) {
        SqlSession var3 = this.getSession(var1, false);
        return var3.selectOne(var2);
    }

    public Map<?, ?> selectMap(String var1, String var2) {
        SqlSession var3 = this.getSession(null, false);
        return var3.selectMap(var1, var2);
    }

    public Map<?, ?> selectMap(SqlSessionTemplate var1, String var2, String var3) {
        SqlSession var4 = this.getSession(null, false);
        return var4.selectMap(var2, var3);
    }

    public Map<?, ?> selectMap(String var1, Object var2, String var3) {
        SqlSession var4 = this.getSession(null, false);
        return var4.selectMap(var1, var2, var3);
    }

    public Map<?, ?> selectMap(SqlSessionTemplate var1, String var2, Object var3, String var4) {
        SqlSession var5 = this.getSession(var1, false);
        return var5.selectMap(var2, var3, var4);
    }

    /*
     * @Description 分页
     * @param page: 页
     * @param rows: 行
     * @param sql: 字段
     * @param sqlExceptSelect: 条件
     * @param param: 参数
     * @Return com.sloera.mng.core.page.Page<com.sloera.mng.core.db.Record>
     * @Author SloeraN
     * @Date 2020/1/5 17:27
     */
    public Page<Record> paginate(int page, int rows, String sql, String sqlExceptSelect, Object... param) throws Exception {
        Connection connection = null;
        Page<Record> page1;
        try {
            connection = this.slaveSqlSessionTemplate.getConnection();
            page1 = this.getPaginateList(connection, page, rows, sql, sqlExceptSelect, param);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        return page1;
    }

    private Page<Record> getPaginateList(Connection connection, int page, int rows, String sql, String sqlExceptSelect, Object... param) throws Exception {
        if (page >= 1 && rows >= 1) {
            long size;
            boolean flag = false;
            String sizeSql = "select count(1) " + DbUtils.replaceFormatSqlOrderBy(sqlExceptSelect);
            size = this.getPaginateSize(connection, sizeSql, param);
            if (size == 0L) {
                return new Page(new ArrayList(0), page, rows, 0, 0L);
            } else {
                //获取总页数
                int pages = (int) (size / (long) rows);
                if (size % (long) rows != 0L) {
                    pages++;
                }
                JSONObject jsonObject = DbUtils.getDbPaginate(connection, page, rows, sql.toUpperCase(), sqlExceptSelect);
                String sqlPagi = jsonObject.getString("sql");
                List list = this.getPaginateList(connection, sqlPagi, param);
                return new Page(list, page, rows, pages, (long) ((int) size));

            }
        } else {
            return new Page(new ArrayList(0), page, rows, 0, 0L);
        }
    }

    private List<Record> getPaginateList(Connection connection, String sql, Object... param) throws SQLException {
        List<Record> list;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i + 1, param[i]);
            }
            resultSet = preparedStatement.executeQuery();
            //结果转换
            list = RecordBuilder.build(resultSet);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (null != resultSet) {
                    resultSet.close();
                }
                if (null != preparedStatement) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private long getPaginateSize(Connection connection, String sql, Object... param) throws SQLException {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (null != param && param.length > 0) {
                //参数从1开始设置
                for (int i = 0; i < param.length; ++i) {
                    preparedStatement.setObject(i + 1, param[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getLong(1) : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
