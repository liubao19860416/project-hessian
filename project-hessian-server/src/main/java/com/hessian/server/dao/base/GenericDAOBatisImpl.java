package com.hessian.server.dao.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hessian.server.conf.Constants;
import com.hessian.server.dao.entity.base.BaseEntity;
import com.hessian.server.utils.GrapeDAOUtils;
import com.hessian.utils.DatetimeUtils;
import com.hessian.utils.MyStringUtils;

/**
 * 接口 GenericDAO batis 抽象实现基类 GenericDAOBatisImpl的实现子类型
 */
@SuppressWarnings(value = { "rawtypes", "unchecked" })
public abstract class GenericDAOBatisImpl<E extends BaseEntity, T extends GenericDAOBatisImpl<E, T>>
        extends SqlSessionDaoSupport implements GenericDAO<E> {

    protected static final String TABLE_NAME_PREFIX = "t_";
    private static final String NAME_SPACE_SUFFIX = "Mapper";

    protected static final String VAR_TABLE_NAME = "table_name";
    protected static final int SUCCESS = 1;
    protected static final int FAIL = 0;

    private static final Pattern P_DAOIMPL_NAME = Pattern.compile("^(.+)DAOImpl$");

    private static final Map<Class<?>, Logger> loggersMap = new ConcurrentHashMap<>();
    private static final Map<Class, String> NAME_SPACE_MAP = new ConcurrentHashMap<>();
    private static final Map<Class, String> TABLE_NAME_MAP = new ConcurrentHashMap<>();

    protected Class<E> entityClass;
    protected Class<T> daoImplClass;

    private final ObjectFactory objectFactory = new DefaultObjectFactory();

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    //获取反省中的参数类型,实体和实现类
    public GenericDAOBatisImpl() {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.entityClass = (Class<E>) types[0];
        this.daoImplClass = (Class<T>) types[1];
    }

    @PostConstruct
    public void init() {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public final List<E> selectAll(boolean ignore) {
        List<E> entityList = new ArrayList<>();
        List<Map> list = this.getSqlSession().selectList(getNameSpace(GenericDAOBatisImpl.class, "selectAll"),
                newParams().put("ignore", ignore)
                .put(VAR_TABLE_NAME, getTableName()));
        if (list != null) {
            for (Map map : list) {
                entityList.add(this.transMapToEntity(map));
            }
        }
        return entityList;
    }
    
    @Override
    public final List<E> selectAll() {
        return selectAll(false);
    }
    
    @Override
    public final long selectAllCount(boolean ignore) {
        Long count = this.selectCount("selectAllCount",
                newParams().put("ignore", ignore).put(VAR_TABLE_NAME, getTableName()));
        return count;
    }
    
    @Override
    public final long selectAllCount() {
        return selectAllCount(false);
    }

    @Override
    public final List<E> selectByPageQuery(Map<String,Object> param,
                Integer pageIndex ,Integer pageSize,boolean ignoreFlag){
        if(param==null){
            param=newParams();
        }
        List<E> entityList = new ArrayList<>();
        param.put(VAR_TABLE_NAME, getTableName());
        param.put("ignore", ignoreFlag);
        
        List<Map> list = this.getSqlSession().selectList(getNameSpace(GenericDAOBatisImpl.class,
                "selectByPageQuery"),param,makeRowBounds(pageIndex, pageSize));
        if (list != null) {
            for (Map map : list) {
                entityList.add(this.transMapToEntity(map));
            }
        }
//        entityList = this.selectList(getNameSpace(GenericDAOBatisImpl.class,"selectByPageQuery"), param,pageIndex, pageSize);
        return entityList;
    }
    
    @Override
    public final List<E> selectByPageQuery(Map<String,Object> param,
                Integer pageIndex ,Integer pageSize){
        return selectByPageQuery(param, pageIndex, pageSize, false);
    }
    
    @Override
    public final List<E> selectByPageQuery(Map<String,Object> param,boolean ignoreFlag){
        if(param==null){
            param=newParams();
        }
        List<E> entityList = new ArrayList<>();
        param.put("ignore", ignoreFlag);
        param.put(VAR_TABLE_NAME, getTableName());
        List<Map> list = this.getSqlSession().
                selectList(getNameSpace(GenericDAOBatisImpl.class, "selectByPageQuery"),param);
        if (list != null) {
            for (Map map : list) {
                entityList.add(this.transMapToEntity(map));
            }
        }
        return entityList;
    }
    
    @Override
    public final long selectByPageQueryCount(Map<String,Object> param,boolean ignoreFlag){
        if(param==null){
            param=newParams();
        }
        param.put("ignore", ignoreFlag);
        param.put(VAR_TABLE_NAME, getTableName());
        return this.selectCount("selectByPageQueryCount",param);
    }
    
    @Override
    public final long selectByPageQueryCount(Map<String,Object> param){
        return selectByPageQueryCount(param, false);
    }
    
    @Override
    public final List<E> selectByPageQuery(Map<String,Object> param){
        return selectByPageQuery(param, false);
    }
    
    @Override
    public final E findById(Integer id) {
        return findById(id, false);
    }

    @Override
    public final E findById(Integer id, boolean ignore) {
        Map<String, Object> params = newParams()
            .put(VAR_TABLE_NAME, getTableName())
            .put("id", id)
            .put("ignore", ignore);
        return genericSelectOne("findById", params);
    }
    
    @Override
    public final List<E> findById(List<Integer> idList, boolean ignore) {
        if (idList == null || idList.isEmpty()) {
            return new ArrayList<E>();
        }
        return this.selectList(GenericDAOBatisImpl.class, "findByIdList",
                newParams()
                .put(VAR_TABLE_NAME, getTableName())
                .put("ignore", ignore)
                .put("idList",idList));
    }
    
    @Override
    public final List<E> findById(List<Integer> idList) {
        return findById(idList, false);
    }

    @Override
    public final E findByCode(String code) {
        return findByCode(code, false);
    }
    
    @Override
    public final E findByCode(String code, boolean ignore) {
        if (code == null || "".equals(code)) {
            throw new IllegalArgumentException("findByCode: argument code is null or empty!!");
        }
        Map<String, Object> params = newParams()
                .put(VAR_TABLE_NAME, getTableName())
                .put("code", code)
                .put("ignore", ignore);
        return genericSelectOne("findByCode", params);
    }
    
    @Override
    public final List<E> findByCode(List<String> codeList) {
        return findByCode(codeList, false);
    }

    @Override
    public final List<E> findByCode(List<String> codeList, boolean ignore) {
        if (codeList == null || codeList.isEmpty()) {
            return new ArrayList<>();
        }
        return this.selectList(GenericDAOBatisImpl.class, "findByCodeList",
                newParams()
                .put(VAR_TABLE_NAME, getTableName())
                .put("ignore", ignore)
                .put("codeList",codeList));
    }

    @Override
    public final int insert(E entity) {
        if (entity == null) {
            return FAIL;
        }
        List<E> entities = new ArrayList<>();
        entities.add(entity);
        insert(entities);
        return SUCCESS;
    }

    @Override
    public final void insert(List<E> entities) {
        genericInsert(GenericDAOBatisImpl.class, entities);
    }

    protected void genericInsert(List<? extends BaseEntity> entities) {
        genericInsert(GenericDAOBatisImpl.class, entities);
    }

    protected void genericInsert(Class<? extends GenericDAOBatisImpl> daoImplclass,
            List<? extends BaseEntity> entities) {
        if (entities == null || entities.size() <= 0) {
            return;
        }
        List<List<?>> valuesList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put(VAR_TABLE_NAME, getTableName());
        try {
            Timestamp currentTimestamp = DatetimeUtils.currentTimestamp();
            for (int i = 0; i < entities.size(); i++) {
                BaseEntity e = entities.get(i);
                if (StringUtils.isBlank(e.getCode())) {
                    e.setCode(GrapeDAOUtils.uuid());
                }
                
                if (e.getCreatedDatetime() == null
                        || "".equals(e.getCreatedDatetime())
                        || DatetimeUtils.isDatetimeZero(e.getCreatedDatetime())) {
                    e.setCreatedDatetime(currentTimestamp);
                }

                if (e.getUpdatedDatetime() == null
                        || "".equals(e.getUpdatedDatetime())
                        || DatetimeUtils.isDatetimeZero(e.getUpdatedDatetime())) {
                    e.setUpdatedDatetime(currentTimestamp);
                }

                Map<String, List<?>> mapFieldsAndValues = GrapeDAOUtils.describe(e);
                //放入属性字段信息
                if (i == 0) {
                    params.put("fields", mapFieldsAndValues.get("fields"));
                }
                //放入值信息
                valuesList.add((List<?>) mapFieldsAndValues.get("values"));
            }

            List<List<?>> tempValuesList = new ArrayList<>();
            for (List<?> list : valuesList) {
                tempValuesList.add(list);
                if (tempValuesList.size() % Constants.BATCH_SIZE == 0) {
                    params.put("values", tempValuesList);
                    getSqlSession().insert(getNameSpace(daoImplclass, "batchInsert"), params);
                    tempValuesList.clear();
                }
            }

            if (tempValuesList.size() > 0) {
                params.put("values", tempValuesList);
                getSqlSession().insert(getNameSpace(daoImplclass, "batchInsert"), params);
            }
        } catch (Exception ex) {
            getLogger().error("insert entities failed with exception: "
                            + entities.get(0).getClass().getName(), ex);
            throw new RuntimeException(ex);
        }
    }

    protected int insert(String operate, E e) {
        Timestamp currentTimeStamp = DatetimeUtils.currentTimestamp();
        e.setCreatedDatetime(currentTimeStamp);
        e.setUpdatedDatetime(currentTimeStamp);
        return getSqlSession().insert(getNameSpace(operate), e);
    }

    protected int insert(String operate, Object param) {
        return getSqlSession().insert(getNameSpace(operate), param);
    }

    /**
     * 根据传入的条件,进行更新,其中田间字段信息为:map.conditions(预定好的)
     */
//    @Override
//    public final int updateWithMap(Map<String, Object> paramMap) {
//        return updateWithMap(paramMap,null);
//    }
    
    @Override
    public final int updateWithMap(Map<String, Object> paramMap,Map<String, Object> conditMap) {
        if (paramMap != null&&!paramMap.isEmpty()) {
            paramMap.put("updatedDatetime", DatetimeUtils.currentTimestamp());
            List<String> fields = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            for (String key : paramMap.keySet()) {
//                if("id".equalsIgnoreCase(key)){
//                    continue;
//                }
                fields.add(key);
                values.add(paramMap.get(key));
            }
            //where条件  
            Map<String, Object> conditionMap =null;
            List<String> fields2 = new ArrayList<>();
            List<Object> values2 = new ArrayList<>();
            if(conditMap!=null&&!conditMap.isEmpty()){
                conditionMap=new HashMap<String, Object>();
                for (String key : conditMap.keySet()) {
                    fields2.add(key);
                    values2.add(conditMap.get(key));
                }
                conditionMap.put("fields", fields2);
                conditionMap.put("values", values2);
            }else{
                throw new IllegalArgumentException("updateWithMap===>>条件参数Map conditMap is null or empty!!"); 
            }
                
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put(VAR_TABLE_NAME, getTableName());
            parameterMap.put("fields", fields);
            parameterMap.put("values", values);
            parameterMap.put("conditions", conditionMap);
//            parameterMap.put("conditionsFields", fields2);
//            parameterMap.put("conditionsValues", values2);
            return getSqlSession().update( getNameSpace(GenericDAOBatisImpl.class, "updateWithMap"),
                    parameterMap);
        }
        return 0;
    }
    
    @Override
    public final int update(E entity) {
        if(entity!=null){
            entity.setUpdatedDatetime(DatetimeUtils.currentTimestamp());
            Map<String, Object> paramMap=new HashMap<String, Object>();
            try {
                //只能更新为非空的数值信息,不能更新为空值 TODO 
                //GrapeDAOUtils.fromObjectToMapNotNull(entity, paramMap);
                //可以更新为空值
                GrapeDAOUtils.fromObjectToMapAllowsNull(entity, paramMap);
                
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Map<String, Object> conditMap=new HashMap<String, Object>();
            //设置默认查询条件
            if(StringUtils.isNotBlank(MyStringUtils.getStringValue(entity.getId()))){
                conditMap.put("id", entity.getId());
                paramMap.remove("id");
            }
            if(StringUtils.isNotBlank(entity.getCode())){
                conditMap.put("code", entity.getCode());
                paramMap.remove("code");
            }
            return updateWithMap(paramMap, conditMap);
            //return getSqlSession().update(getNameSpace("update"), entity);
        }
        return 0;
    }

    protected int update(String operate, E entity) {
        entity.setUpdatedDatetime(DatetimeUtils.currentTimestamp());
        return update(operate, (Object) entity);
    }

    protected int update(String operate, Object param) {
        return getSqlSession().update(getNameSpace(operate), param);
    }

    @Override
    public final int update(List<E> entities) {
        if (entities != null && entities.size() > 0) {
           return getSqlSession().update(getNameSpace("batchUpdate"), entities);
        }
        return 0;
    }

    protected void update(String operate, List<E> entities) {
        update(operate, (Object) entities);
    }

    protected int updateByCode(Map<String, Object> map, String code) {
        if (map != null) {
            map.put("updatedDatetime", DatetimeUtils.currentTimestamp());
            List<String> fields = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            for (String key : map.keySet()) {
                fields.add(key);
                values.add(map.get(key));
            }
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put(VAR_TABLE_NAME, getTableName());
            parameterMap.put("fields", fields);
            parameterMap.put("values", values);
            parameterMap.put("code", code);
            return getSqlSession().update( getNameSpace(GenericDAOBatisImpl.class, "updateByCode"),
                    parameterMap);
        }
        return 0;
    }

    @Override
    public final int deleteById(Integer id) {
        Map<String, Object> params =new Params()
            .put(VAR_TABLE_NAME, getTableName())
            .put("id", id);
        return getSqlSession().update(
                getNameSpace(GenericDAOBatisImpl.class, "deleteById"), params);
    }

    @Override
    public final int deleteByCode(String code) {
        if (code == null || "".equals(code)) {
            throw new IllegalArgumentException(
                    "deleteByCode: argument code is null or empty!!");
        }
        Map<String, Object> params =new Params()
            .put(VAR_TABLE_NAME, getTableName())
            .put("code", code);
        return getSqlSession().update(getNameSpace(GenericDAOBatisImpl.class, "deleteByCode"),
                        params);
    }

    protected String getCodeStr(String code, String operate) {
        return getSqlSession().selectOne(
                getNameSpace(this.daoImplClass, operate), code);
    }

    @Override
    public final int delete(E entity) {
        return deleteById(entity.getId());
    }

    @Override
    public final int delete(List<E> entities) {
        List<Integer> ids = new ArrayList<>();
        if (entities != null && entities.size() > 0) {
            for (E e : entities) {
                ids.add(e.getId());
            }
        }
        if (ids.size() > 0) {
           return this.deleteByIdList(ids);
        }
        return 0;
    }

    public String getNameSpace(String operate) {
        return getNameSpace(daoImplClass, operate);
    }

    public static String getNameSpace(Class daoImplClass, String operate) {
        String nameSpace = NAME_SPACE_MAP.get(daoImplClass);
        if (nameSpace == null || "".equals(nameSpace)) {
            nameSpace = daoImplClass.getName() + NAME_SPACE_SUFFIX;
            NAME_SPACE_MAP.put(daoImplClass, nameSpace);
        }
        return nameSpace + "." + operate;
    }

    public String getTableName() {
        return getTableName(this.daoImplClass);
    }

    public static String getTableName(Class daoImplClass) {
        String tableName = TABLE_NAME_MAP.get(daoImplClass);
        if (tableName != null && !"".equals(tableName)) {
            return tableName;
        }
        StringBuilder sbd = new StringBuilder();
        Matcher m = P_DAOIMPL_NAME.matcher(daoImplClass.getSimpleName());
        if (!m.matches()) {
            throw new IllegalArgumentException(daoImplClass.getSimpleName()+ " is illegal DAOImpl class!!");
        }

        //按照大小写字母,进行表明下环线划分
        for (char c : m.group(1).toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                if (sbd.length() > 0) {
                    sbd.append('_');
                }
                sbd.append(String.valueOf(c).toLowerCase());
            } else {
                sbd.append(c);
            }
        }

        tableName = TABLE_NAME_PREFIX + sbd.toString();
        System.out.println("表名tableName:"+tableName);
        TABLE_NAME_MAP.put(daoImplClass, tableName);
        return tableName;
    }

    protected Logger getLogger() {
        return getLogger(this.daoImplClass);
    }

    private static Logger getLogger(Class<?> clazz) {
        Logger logger = loggersMap.get(clazz);
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            loggersMap.put(clazz, logger);
        }
        return logger;
    }

    protected List<E> selectList(String operate) {
        return selectList(operate, null);
    }

    protected List<E> selectList(String operate, Object param) {
        return selectList(this.daoImplClass, operate, param);
    }

    protected List<E> selectList(Class<?> clzss, String operate, Object param) {
        return getSqlSession().selectList(getNameSpace(clzss, operate), param);
    }

    protected E selectOne(Class<?> clzss, String operate, Object param) {
        return getSqlSession().selectOne(getNameSpace(clzss, operate), param);
    }

    protected List<E> selectList(String operate, int pageIndex, int pageSize) {
        return selectList(operate, null, pageIndex, pageSize);
    }

    protected List<E> selectList(String operate, Object param, int pageIndex,
            int pageSize) {
        return selectList(this.daoImplClass, operate, param, pageIndex,
                pageSize);
    }

    protected List<Map> selectMapList(String operate, Object param,
            int pageIndex, int pageSize) {
        return getSqlSession().selectList(getNameSpace(this.daoImplClass, operate), param,
                makeRowBounds(pageIndex, pageSize));
    }

    protected Map<String, String> selectMap(String operate, Object param) {
        List<Map> list = getSqlSession().selectList(getNameSpace(this.daoImplClass, operate), param);
        Map<String, String> map = new HashMap<>();
        for (Map result : list) {
            map.put(result.get("startDatetime").toString(), result.get("counts").toString());
        }
        return map;
    }

    protected List<Map> selectMapList(String operate, Object param) {
        return getSqlSession().selectList(getNameSpace(this.daoImplClass, operate), param);
    }

    protected List<E> selectList(Class<?> clzss, String operate, Object param, int pageIndex, int pageSize) {
        return getSqlSession().selectList(getNameSpace(clzss, operate), param,makeRowBounds(pageIndex, pageSize));
    }

    protected E selectOne(String operate) {
        return selectOne(operate, null);
    }

    protected E selectOne(String operate, Object param) {
        return (E) getSqlSession().selectOne(getNameSpace(operate), param);
    }

    // 查询记录总条数
    protected long selectCount(String operate, Object param) {
        return selectCount(GenericDAOBatisImpl.class,operate, param);
    }

    protected long selectCount(Class<?> clzss, String operate, Object param) {
        return getSqlSession().selectOne(getNameSpace(clzss, operate), param);
    }

    protected E genericSelectOne(String operate, Object param) {
        GenericResultHandler resultHandler = new GenericResultHandler();
        getSqlSession().select(getNameSpace(GenericDAOBatisImpl.class, operate), param,
                resultHandler);
        return resultHandler.getResult();
    }

    protected E genericSelectList(String operate, Object param) {
        GenericResultHandler resultHandler = new GenericResultHandler();
        getSqlSession().select( getNameSpace(GenericDAOBatisImpl.class, operate), param,resultHandler);
        return resultHandler.getResult();
    }

    protected RowBounds makeRowBounds(int pageIndex, int pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = (pageSize <= 0 || pageSize > Constants.PAGESIZE_MAX) ? Constants.PAGESIZE_DEFAULT
                : pageSize;
        logger.warn("分页参数为:【pageIndex="+pageIndex+",pageSize="+pageSize+"】");
        return new RowBounds( ( pageIndex - 1 ) * pageSize, pageSize );
        //return new RowBounds((pageIndex - 1) * pageSize+1, pageSize);
    }

    /*
     * internal ResultHandler class
     */
    protected class GenericResultHandler implements ResultHandler {
        private E entity;
        @Override
        public void handleResult(ResultContext context) {
            Map<String, Object> map = (Map<String, Object>) context.getResultObject();
            entity = transMapToEntity(map);
        }
        public E getResult() {
            return entity;
        }

    }

    protected E transMapToEntity(Map map) {
        if (map != null) {
            E entity = objectFactory.create(GenericDAOBatisImpl.this.entityClass);
            try {
                GrapeDAOUtils.populate(entity, map);
            } catch (Exception ex) {
                getLogger().error("GenericResultHandler.handleResult exception", ex);
            }
            return entity;
        }
        return null;
    }

    /**
     * 按传入的codeList批量删除商品信息，注意，此方法为物理删除，不可恢复
     *            商品code所组成的集合
     */
    @Override
    public int deletePhysicalByCodeList(List<String> codeList) {
        Map<String, Object> params = new Params()
            .put(VAR_TABLE_NAME, getTableName())
            .put("codeList", codeList);
        return getSqlSession().delete(getNameSpace(GenericDAOBatisImpl.class,
                        "deletePhysicalByCodeList"), params);
    }

    /**
     * 按传入的idList批量删除商品信息，注意，此方法为物理删除，不可恢复
     *            商品code所组成的集合
     */
    @Override
    public int deletePhysicalByIdList(List<Integer> idList) {
        Map<String, Object> params = new Params()
            .put(VAR_TABLE_NAME, getTableName())
            .put("idList", idList);
        return getSqlSession().delete(getNameSpace(GenericDAOBatisImpl.class,
                        "deletePhysicalByIdList"), params);
    }

    /**
     * 按传入的idList批量删除商品信息，逻辑删除，并不真正删除数据
     *            商品code所组成的集合
     */
    @Override
    public int deleteByIdList(List<Integer> idList) {
        Map<String, Object> params =new Params()
            .put(VAR_TABLE_NAME, getTableName())
            .put("idList", idList);
        return getSqlSession().delete( getNameSpace(GenericDAOBatisImpl.class, "deleteByIdList"),
                params);
    }

    /**
     * 按传入的codeList批量删除商品信息，逻辑删除，并不真正删除数据
     *            商品code所组成的集合
     */
    @Override
    public int deleteByCodeList(List<String> codeList) {
        Map<String, Object> params = new Params()
            .put(VAR_TABLE_NAME, getTableName())
            .put("codeList", codeList);
        return getSqlSession().delete(getNameSpace(GenericDAOBatisImpl.class, "deleteByCodeList"),
                params);
    }

    protected Params newParams() {
        return new Params();
    }

    /**
     * convenient class to hold params
     * 
     */
    protected class Params extends HashMap<String, Object> {
        private static final long serialVersionUID = 5944659754462710346L;
        @Override
        public Params put(String key, Object value) {
            super.put(key, value);
            return this;
        }
    }

}
