package com.juduowang.common.service;

import org.hibernate.criterion.Criterion;

/**
 * 公共下拉列表查询services类
 * 所有需要用到combo的地方，可以通过此公共services检索出combo的字串
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 */
public interface UniversalComponent {
	/**
	 * 包含的方式，检索出公用组件的值字串<p>
	 * 根据类，检索出该类的json字串,通过置入的需要检索的对象属性<p>
	 * 采取反射的机理，来获取到对象的值<p>
	 * <pre>
	 * 	{className+Store:[{field1:value1},{field2:value2}]}
	 * </pre>
	 * 
	 * @param packageName 包名
	 * @param className 类名
	 * @param includes 包含的属性
	 * @param criterion 扩展criterion，查询条件
	 * @return 返回所需属性的Combo的JSON字串
	 */
	public String getByIncludes(String packageName,String className,String[] includes,Criterion...criterion);
	
	
	/**
	 * 包含的方式，检索出公用组件的值字串<p>
	 * 根据类，检索出该类的json字串,通过置入的需要检索的对象属性<p>
	 * 采取反射的机理，来获取到对象的值<p>
	 * <pre>
	 * 	{className+Store:[{field1:value1},{field2:value2}]}
	 * </pre>
	 * 
	 * @param packageName 包名
	 * @param className 类名
	 * @param includes 包含的属性
	 * @param criterion 扩展criterion，查询条件
	 * @return 返回所需属性的Combo的JSON字串
	 */
	public String getEnglishByIncludes(String packageName,String className,String[] includes, String args, String argsValue, Criterion...criterion);
	
	/**
	 * 根据类，检索出该类的json字串,根据excludes设置排除不需要的属性
	 * <pre>
	 * 	{className+Store:[{field1:value1},{field2:value2}]}
	 * </pre>
	 * @param packageName 包名
	 * @param className 类名
	 * @param excludes 需要排除的属性
	 * @param criterion 扩展criterion，查询条件
	 * @return 返回所需属性的Combo的JSON字串
	 */
	public String getByExcludes(String packageName,String className,String[] excludes,Criterion...criterion);
}	
