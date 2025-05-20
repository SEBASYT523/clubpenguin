package co.edu.unbosque.backclubpenguin.service;

import java.util.List;


public interface CRUDOperation<T> {


	public int create(T data);

	
	public List<T> getAll();


	public int deleteByUsername(String username);


	public int updateByUsername(String username, T newData);

	
	public long count();

	
	public boolean exist(String username);
}
