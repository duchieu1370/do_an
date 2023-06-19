package com.devpro.shop16.service;

import com.devpro.shop16.entities.*;
import com.devpro.shop16.repository.CheckEmailRepository;
import com.devpro.shop16.repository.ContactRepository;
import com.devpro.shop16.repository.OrderRepository;
import com.devpro.shop16.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public abstract class BaseService<E extends BaseEntity>{

	private static int SIZE_OF_PAGE = 20;

	@Autowired
	protected CheckEmailRepository checkEmailRepository;

	@Autowired
	protected ContactRepository contactRepository;

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected OrderRepository orderRepository;

	@PersistenceContext //Inject entityManager
	protected EntityManager entityManager;

	protected abstract Class<E> clazz();

	/**
	 * Thực hiện lưu hoặc cập nhật bản ghi trong cơ sở dữ liệu.
	 * @param entity
	 * @return
	 */
	@Transactional
	public E saveOrUpdate(E entity) {
//		checkEmailRepository.findByEmail();
		if (entity.getId() == null || entity.getId() <= 0) {
			entity.setCreatedDate(new Date());
			entityManager.persist(entity); // thêm mới
			return entity;
		} else {
			return entityManager.merge(entity); // cập nhật
		}
	}

	/**
	 * kiểm tra trùng email
	 * @param
	 */
	@Transactional
	public List<Subcribe> checkEmailSubcribe(Subcribe entity) {
		return checkEmailRepository.findByEmail(entity.getEmail());
	}

	@Transactional
	public List<Contact> checkEmailContact(Contact entityContact) {
		return contactRepository.findByEmailContact(entityContact.getEmail());
	}

	@Transactional
	public List<User> checkEmailRegister(User entityUser) {
		return userRepository.findByEmailRegister(entityUser.getEmail());
	}

	@Transactional
	public List<User> checkUserNameRegister(User entityUser){
		return userRepository.findByUserNameRegister(entityUser.getUsername());
	}

	@Transactional
	public List<Saleorder> checkEmailOrder(Saleorder entitySaleOrder) {
		return orderRepository.findByEmailOrder(entitySaleOrder.getCustomer_email());
	}

	/**
	 * xóa bản ghi trong cơ sở dữ liệu
	 * @param entity
	 */
	public void delete(E entity) {
		entityManager.remove(entity);
	}

	/**
	 * xóa bản ghi trong cơ sở dữ liệu theo khóa chính id
	 * @param
	 */
	public void deleteById(int primaryKey) {
		E entity = this.getById(primaryKey);
		delete(entity);
	}
	
	/**
	 * Lấy bản ghi trong cơ sở dữ liệu theo khóa chính ID.
	 * @param
	 * @return
	 */
	public E getById(int primaryKey) {
		return entityManager.find(clazz(), primaryKey);
	}

	/**
	 * thực thi câu lệnh truy vấn cơ sở dữ liệu và trả về duy nhất 1 kết quả
	 * @param sql -> ví dụ chạy câu lệnh [SELECT * FROM tbl_category WHERE name='Java']
	 * @param
	 * @return
	 */
	public E getOneByNativeSQL(String sql) {
		try {
			return executeByNativeSQL(sql, 0).getData().get(0);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Lấy tất cả bản ghi trong cơ sở dữ liệu.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		Table tbl = clazz().getAnnotation(Table.class);
		return (List<E>) entityManager.createNativeQuery("SELECT * FROM " + tbl.name(), clazz()).getResultList();
	}
	
	/**
	 * thực thi câu lệnh truy vấn cơ sở dữ liệu
	 * @param sql -> ví dụ chạy câu lệnh [SELECT * FROM tbl_category;]
	 * @param page
	 * @return
	 */
	public PagerData<E> executeByNativeSQL(String sql, int page) {
		PagerData<E> result = new PagerData<E>();
		
		try {
			Query query = entityManager.createNativeQuery(sql, clazz());
			
			//trường hợp có thực hiện phân trang thì kết quả trả về
			//bao gồm tổng số page và dữ liệu page hiện tại
			if(page > 0) {
				result.setCurrentPage(page);
				result.setTotalItems(query.getResultList().size());
				
				query.setFirstResult((page - 1) * SIZE_OF_PAGE);
				query.setMaxResults(SIZE_OF_PAGE);
			}
			
			result.setData(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * thực thi câu lệnh cập nhật cơ sở dữ liệu
	 * @param sql -> ví dụ chạy câu lệnh [DELETE FROM tbl_category;] hoặc [UPDATE tbl_category SET Name = 'Alfred Schmidt' WHERE Id = 1;]
	 * @return
	 */
	public int executeUpdateOrDeleteByNativeSQL(String sql) {
		try {
			return entityManager.createNativeQuery(sql).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
