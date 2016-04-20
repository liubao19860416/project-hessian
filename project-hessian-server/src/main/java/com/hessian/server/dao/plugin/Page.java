package com.hessian.server.dao.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:Page <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: 分页对象. <br/>
 * Date: 2014-9-4 下午2:02:17 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.7
 * @see
 */
public class Page<T> {

	private int pageNo ;// 页码，默认是第一页
	private int pageSize ;// 每页显示的记录数，默认是20
	private int totalRecord;// 总记录数
	private int totalPage;// 总页数
	private T results;// 对应的当前页记录
	private Map<String, Object> params = new HashMap<String, Object>();// 其他的参数我们把它分装成一个Map对象

	public T getResults() {
		return results;
	}

	public void setResults(T results) {
		this.results = results;
	}

	public Page() {
		this.pageNo = 1 ;
		this.pageSize = 20 ;
	}

	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public Page(int pageNo, int pageSize, Map<String, Object> params) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.params = params;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize
				: totalRecord / pageSize + 1;
		this.setTotalPage(totalPage);
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
