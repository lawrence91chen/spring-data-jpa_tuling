package org.example.proxy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class MyJpaProxy implements PagingAndSortingRepository {
	private EntityManager entityManager;
	private Class pojoClass;

	public MyJpaProxy(EntityManager entityManager, Class pojoClass) {
		this.entityManager = entityManager;
		this.pojoClass = pojoClass;
	}

	@Override
	public Iterable findAll(Sort sort) {
		return null;
	}

	@Override
	public Page findAll(Pageable pageable) {
		return null;
	}

	@Override
	public Object save(Object entity) {
		return null;
	}

	@Override
	public Iterable saveAll(Iterable entities) {
		return null;
	}

	@Override
	public Optional findById(Object id) {
		// 基於 JPA
		return Optional.of(entityManager.find(pojoClass, id));
	}

	@Override
	public boolean existsById(Object o) {
		return false;
	}

	@Override
	public Iterable findAll() {
		return null;
	}

	@Override
	public Iterable findAllById(Iterable iterable) {
		return null;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(Object o) {

	}

	@Override
	public void delete(Object entity) {

	}

	@Override
	public void deleteAllById(Iterable iterable) {

	}

	@Override
	public void deleteAll(Iterable entities) {

	}

	@Override
	public void deleteAll() {

	}
}
