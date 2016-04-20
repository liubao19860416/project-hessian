package com.hessian.server.service.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hessian.server.dao.base.GenericDAO;
import com.hessian.server.dao.entity.base.BaseEntity;
import com.hessian.server.utils.GrapeDAOUtils;
import com.hessian.utils.DatetimeUtils;
import com.hessian.vo.base.BaseViewObject;

/**
 * 服务层抽象通用基类
 * 
 * @param <V>
 *            BaseViewObject子视图类型
 * @param <E>
 *            BaseEntity子实体类型
 * @param <T>
 *            GenericServiceImpl子类型
 */
@SuppressWarnings("unchecked")
public abstract class GenericServiceImpl<V extends BaseViewObject<V>, E extends BaseEntity, T extends GenericServiceImpl<V, E, T>>
        implements GenericService<V, E> {

    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    private static final Map<Class<?>, Logger> loggersMap = new ConcurrentHashMap<>();
    
    public GenericServiceImpl() {
    }

    private GenericDAO<E> genericDAO;

    private Class<V> viewObjectClass;
    private Class<E> entityClass;
    private Class<T> serviceImplClass;
    
    public GenericServiceImpl(final GenericDAO<E> genericDAO) {
        this.genericDAO = genericDAO;
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments();
        viewObjectClass = (Class<V>) types[0];
        entityClass = (Class<E>) types[1];
        serviceImplClass = (Class<T>) types[2];

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getByPageQuery(Map<String,Object> param,boolean ignoreFlag) {
        List<E> entityList = this.genericDAO.selectByPageQuery(param,ignoreFlag);
        return entityList != null&&!entityList.isEmpty() ? this.transToViewObject(entityList) : new ArrayList<V>();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getByPageQueryCount(Map<String,Object> param,boolean ignoreFlag) {
        return this.genericDAO.selectByPageQueryCount(param,ignoreFlag);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getByPageQuery(Map<String,Object> param) {
        return getByPageQuery(param,false);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getByPageQuery(Map<String,Object> param,Integer pageIndex ,Integer pageSize,boolean ignoreFlag) {
        List<E> entityList = this.genericDAO.selectByPageQuery(param, pageIndex, pageSize, ignoreFlag);
        return entityList != null&&!entityList.isEmpty() ? this.transToViewObject(entityList) : new ArrayList<V>();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getByPageQuery(Map<String,Object> param,Integer pageIndex ,Integer pageSize) {
        return getByPageQuery(param, pageIndex, pageSize, false);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getByPageQueryCount(Map<String,Object> param) {
        return getByPageQueryCount(param,false);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getAll(boolean ignoreFlag) {
        List<E> entityList = this.genericDAO.selectAll(ignoreFlag);
//        return entityList != null&&!entityList.isEmpty() ? this.transToViewObject(entityList) : new ArrayList<V>();
        return entityList != null&&!entityList.isEmpty() ? this.transToViewObject(entityList) : null;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getAll() {
        return getAll(false);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getAllCount(boolean ignoreFlag) {
        return this.genericDAO.selectAllCount(ignoreFlag);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getAllCount() {
        return getAllCount(false);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public V getById(Integer id) {
        E entity = this.genericDAO.findById(id);
        return entity != null ? this.transToViewObject(entity) : null;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getByIdList(List<Integer> idList) {
        List<E> entityList = this.genericDAO.findById(idList);
        return entityList != null&&!entityList.isEmpty() ? this.transToViewObject(entityList) : new ArrayList<V>();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public V getById(Integer id , boolean ignoreFlag) {
        E entity = this.genericDAO.findById(id,ignoreFlag);
        return entity != null ? this.transToViewObject(entity) : null;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getByIdList(List<Integer> idList , boolean ignoreFlag) {
        List<E> entityList = this.genericDAO.findById(idList,ignoreFlag);
        return entityList != null&&!entityList.isEmpty() ? this.transToViewObject(entityList) : new ArrayList<V>();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public V getByCode(String code) {
        E entity = this.genericDAO.findByCode(code);
        return entity != null ? this.transToViewObject(entity) : null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getByCodeList(List<String> codeList) {
        List<E> entityList = this.genericDAO.findByCode(codeList);
        return entityList != null&&!entityList.isEmpty() ? this.transToViewObject(entityList) : new ArrayList<V>();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public V getByCode(String code , boolean ignoreFlag) {
        E entity = this.genericDAO.findByCode(code,ignoreFlag);
        return entity != null ? this.transToViewObject(entity) : null ;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<V> getByCodeList(List<String> codeList , boolean ignoreFlag) {
        List<E> entityList = this.genericDAO.findByCode(codeList,ignoreFlag);
        return entityList != null&&!entityList.isEmpty() ? this.transToViewObject(entityList) : new ArrayList<V>();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean save(V viewObject) {
        if (viewObject == null) {
            throw new IllegalArgumentException("Argument viewObject is empty!!");
        }
        E entity = this.transToEntity(viewObject);
        if (entity.getCreatedDatetime() == null) {
            entity.setCreatedDatetime(DatetimeUtils.currentTimestamp());
        }
        if (entity.getUpdatedDatetime() == null) {
            entity.setUpdatedDatetime(DatetimeUtils.currentTimestamp());
        }
        if (hasCode(entity) || hasId(entity)) {
            return this.genericDAO.update(entity) == 1;
        } else {
            if (!hasCode(entity)) {
                entity.setCode(GrapeDAOUtils.uuid());
            }
            return this.genericDAO.insert(entity) == 1;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(List<V> viewObjects) {
        if (viewObjects == null || viewObjects.size() == 0) {
            throw new IllegalArgumentException("Argument viewObjects is empty!!");
        }
        List<E> entities = this.transToEntity(viewObjects);
        List<E> insertList = new ArrayList<>();
        List<E> updateList = new ArrayList<>();
        for (E e : entities) {
            if (hasCode(e) || hasId(e)) {
                updateList.add(e);
            } else {
                if (!hasCode(e)) {
                    e.setCode(GrapeDAOUtils.uuid());
                }
                insertList.add(e);
            }
        }
        if (insertList.size() > 0) {
            this.genericDAO.insert(entities);
        }
        if (updateList.size() > 0) {
            this.genericDAO.update(updateList);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean insert(V viewObject) {
        if (viewObject == null) {
            throw new IllegalArgumentException("Argument viewObject is empty!!");
        }
        E entity = this.transToEntity(viewObject);
        return this.genericDAO.insert(entity) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void insert(List<V> viewObjects) {
        if (viewObjects == null || viewObjects.size() == 0) {
            throw new IllegalArgumentException("Argument viewObjects is empty!!");
        }
        List<E> entities = this.transToEntity(viewObjects);
        this.genericDAO.insert(entities);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteById(Integer id) {
        return this.genericDAO.deleteById(id);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteByIdList(List<Integer> idList) {
        return this.genericDAO.deleteByIdList(idList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteByCode(String code) {
        return this.genericDAO.deleteByCode(code);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteByCodeList(List<String> codeList) {
        return this.genericDAO.deleteByCodeList(codeList);
    }

    protected boolean hasCode(E entity) {
        String code = entity.getCode();
        return code != null && !"".equals(code);
    }

    protected boolean hasId(E entity) {
        Integer id =entity.getId();
        return id != null && id > 0;
    }

    protected Logger getLogger() {
        return getLogger(this.serviceImplClass);
    }

    private static Logger getLogger(Class<?> clazz) {
        Logger logger = loggersMap.get(clazz);
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            loggersMap.put(clazz, logger);
        }
        return logger;
    }

    protected V newViewObject() {
        V obj = null;
        try {
            obj = this.viewObjectClass.newInstance();
        } catch (Exception ex) {
            logger.error("GenericServiceImpl.newViewObject exception", ex);
        }
        return obj;
    }

    protected E newEntityObject() {
        E obj = null;
        try {
            obj = this.entityClass.newInstance();
        } catch (Exception ex) {
            logger.error("GenericServiceImpl.newViewObject exception", ex);
            throw new RuntimeException(ex);
        }
        return obj;
    }

    protected E transToEntity(V object) {
        return transToEntity(object, (Object[]) null);
    }

    protected V userCollectViewObject(E entity) {
        return transToViewObject(entity, (Object[]) null);
    }

    protected List<E> transToEntity(List<V> objects) {
        return transToEntity(objects, (Object[]) null);
    }

    protected List<E> transToEntity(List<V> objects, Object... options) {
        List<E> entities = new ArrayList<>();
        if (objects != null) {
            for (V obj : objects) {
                entities.add(transToEntity(obj, options));
            }
        }
        return entities;
    }

    protected List<V> transToViewObject(List<E> entities) {
        return transToViewObject(entities, (Object[]) null);
    }

    protected List<V> transToViewObject(List<E> entities, Object... options) {
        List<V> viewObjects = new ArrayList<V>();
        if (entities != null) {
            E entity=newEntityObject();
            for (Object object : entities) {
                if(object instanceof BaseEntity){
                    entity= (E)object;
                }else if(object instanceof Map){
                    GrapeDAOUtils.copyProperties(entity, object);
                }
                viewObjects.add(transToViewObject(entity, options));
            }
        }
        return viewObjects;
    }

    protected E transToEntity(V viewObject, Object... options) {
        if (viewObject == null) {
            return null;
        }
        E entity = this.newEntityObject();
        GrapeDAOUtils.copyProperties(entity, viewObject);
        return entity;
    }

    protected V transToViewObject(E entity, Object... options) {
        if (entity == null) {
            return null;
        }
        V viewObject = this.newViewObject();
        GrapeDAOUtils.copyProperties(viewObject, entity);
        return viewObject;
    }

}
