package com.sloera.mng.core.ibatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BaseWriteDao<E> extends BaseReadDao<E> {
    @Autowired
    @Qualifier("masterSqlSessionTemplate")
    public SqlSessionTemplate masterSqlSessionTemplate;

    public BaseWriteDao() {
    }

    private SqlSession getSession(SqlSessionTemplate var1, boolean var2) {
        return (SqlSession) (var1 == null ? this.masterSqlSessionTemplate : var1.getSqlSessionFactory().openSession(ExecutorType.SIMPLE, var2));
//        return (SqlSession) (var1.getSqlSessionFactory().openSession(ExecutorType.BATCH, var2));
    }

    public int update(String var1, Object var2) {
        SqlSession var3 = this.getSession((SqlSessionTemplate) null, true);
        return var3.update(var1, var2);
    }

    public int update(String var1) {
        SqlSession var2 = this.getSession((SqlSessionTemplate) null, true);
        return var2.update(var1);
    }

    public int save(String var1, Object var2) {
        SqlSession var3 = this.getSession((SqlSessionTemplate) null, true);
        return var3.insert(var1, var2);
    }

    public int save(String var1) {
        SqlSession var2 = this.getSession((SqlSessionTemplate) null, true);
        return var2.insert(var1);
    }

    public int save(SqlSessionTemplate var1, String var2, Object var3) {
        SqlSession var4 = this.getSession(var1, true);
        return var4.insert(var2, var3);
    }

    public int delete(String var1, Object var2) {
        SqlSession var3 = this.getSession((SqlSessionTemplate) null, true);
        return var3.delete(var1, var2);
    }

    public int delete(String var1) {
        SqlSession var2 = this.getSession((SqlSessionTemplate) null, true);
        return var2.insert(var1);
    }

    public int delete(SqlSessionTemplate var1, String var2, Object var3) {
        SqlSession var4 = this.getSession(var1, true);
        return var4.delete(var2, var3);
    }
}
