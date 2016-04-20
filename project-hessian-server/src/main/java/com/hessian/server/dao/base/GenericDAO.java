package com.hessian.server.dao.base;

import java.util.List;
import java.util.Map;

import com.hessian.server.dao.entity.base.BaseEntity;


/**
 * DAO曾基础类
 * 
 * @author Liubao
 * @2015年8月5日
 * 
 */
public interface GenericDAO<E extends BaseEntity> {

    List<E> selectAll();
    
    long selectAllCount();
    
    List<E> selectAll(boolean ignoreFlag);
    
    long selectAllCount(boolean ignoreFlag);
    
    List<E> selectByPageQuery(Map<String,Object> param);
    
    long selectByPageQueryCount(Map<String,Object> param);
    
    List<E> selectByPageQuery(Map<String,Object> param,boolean ignoreFlag);
    
    long selectByPageQueryCount(Map<String,Object> param,boolean ignoreFlag);
    
    List<E> selectByPageQuery(Map<String,Object> param,Integer pageIndex ,Integer pageSize);
    
    List<E> selectByPageQuery(Map<String,Object> param,Integer pageIndex ,Integer pageSize,boolean ignoreFlag);

    E findById(Integer id);
    
    E findById(Integer id, boolean ignoreFlag);

    E findByCode(String code);

    E findByCode(String code, boolean ignoreFlag);

    List<E> findByCode(List<String> codeList);
    
    List<E> findByCode(List<String> codeList, boolean ignoreFlag);
    
    List<E> findById(List<Integer> idList);
    
    List<E> findById(List<Integer> idList, boolean ignoreFlag);

    int insert(E entity);

    void insert(List<E> entities);

    int update(E entity);

//    int updateWithMap(Map<String,Object> paramMap);
    
    int updateWithMap(Map<String,Object> paramMap,Map<String, Object> conditMap);
    
    int update(List<E> entities);

    int deleteById(Integer id);

    int deleteByCode(String code);

    int delete(E entity);

    int delete(List<E> entities);

    int deleteByIdList(List<Integer> idList);

    int deleteByCodeList(List<String> codeList);

    int deletePhysicalByIdList(List<Integer> idList);

    int deletePhysicalByCodeList(List<String> codeList);

}
