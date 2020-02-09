package com.sloera.mng.core.ibatis;

import java.util.List;
import java.util.Map;

public interface BaseDaoInterface {
    <E> List<E> selectList(String var1, Object var2);

    <E> List<E> selectList(String var1);

    <E> E selectOne(String var1);

    Map<?, ?> selectMap(String var1, String var2);

    //Page<Record> paginate(DataSource var1, int var2, int var3, String var4, String var5) throws Exception;
    //
    //Page<Record> paginate(DataSource var1, int var2, int var3, String var4, String var5, Object... var6) throws Exception;
    //
    //Page<Record> paginate(int var1, int var2, String var3, String var4, Object... var5) throws Exception;

    int update(String var1, Object var2);

    int update(String var1);

    int save(String var1, Object var2);

    int save(String var1);

    int delete(String var1, Object var2);

    int delete(String var1);

}
