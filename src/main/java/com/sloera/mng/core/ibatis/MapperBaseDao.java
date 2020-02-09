package com.sloera.mng.core.ibatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public abstract class MapperBaseDao<E> extends BaseWriteDao<E> implements BaseDaoInterface {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MapperBaseDao() {
    }

    ;

    public void executeSQL(String var1) throws Exception {
        this.jdbcTemplate.execute(var1);
    }

    public int updateSQL(String var1, Object var2) throws Exception {
        return this.jdbcTemplate.update(var1, new Object[]{var2});
    }

    public int updateSQL(String var1, Object[] var2) throws Exception {
        return this.jdbcTemplate.update(var1, var2);
    }

    public List<Map<String, Object>> findSQL(String var1) throws Exception {
        return this.jdbcTemplate.queryForList(var1);
    }

    public List<Map<String, Object>> findSQL(String var1, Object var2) throws Exception {
        return this.jdbcTemplate.queryForList(var1, new Object[]{var2});
    }

    public List<Map<String, Object>> findSQL(String var1, Object[] var2) throws Exception {
        return this.jdbcTemplate.queryForList(var1, var2);
    }

    public Object findObjectSQL(String var1, Object[] var2, Class var3) {
        return this.jdbcTemplate.queryForObject(var1, var2, var3);
    }

    public Map<String, Object> findMapSQL(String var1, Object[] var2) {
        return this.jdbcTemplate.queryForMap(var1, var2);
    }

}
