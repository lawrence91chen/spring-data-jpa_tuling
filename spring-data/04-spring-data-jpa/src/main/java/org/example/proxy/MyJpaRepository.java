package org.example.proxy;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyJpaRepository implements InvocationHandler {
	private EntityManager entityManager;

	public MyJpaRepository(EntityManager entityManager, Class pojoClass) {
		this.entityManager = entityManager;
		this.pojoClass = pojoClass;
	}

	private Class pojoClass;

	/**
	 * @param method 當前調用的方法 -> findById
	 * @param args 當前調用方法的參數 -> 1L
	 */
	// 具體實現數據庫操作的邏輯代碼
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("查詢");

		// JPA 統一實現類
		MyJpaProxy myJpaProxy = new MyJpaProxy(entityManager, pojoClass);
		// 透過反射確認調用的方法
		Method targetJpaMethod = myJpaProxy.getClass().getMethod(method.getName(), method.getParameterTypes());
		// 執行
		return targetJpaMethod.invoke(myJpaProxy, args);
	}
}
