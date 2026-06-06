package com.shopHMsic.service;

import com.shopHMsic.entities.BaseEntity;
import com.shopHMsic.entities.Subcribe;
import com.shopHMsic.repository.CheckEmailRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public abstract class BaseService<E extends BaseEntity> {

    private static int SIZE_OF_PAGE = 20;

    @Autowired
    protected CheckEmailRepository checkEmailRepository;

    @PersistenceContext //Inject entityManager
    protected EntityManager entityManager;

    protected abstract Class<E> clazz();

    /**
     * Thực hiện lưu hoặc cập nhật bản ghi trong cơ sở dữ liệu.
     *
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
     *
     * @param
     */
    @Transactional
    public List<Subcribe> checkEmailSubcribe(Subcribe entity) {
        return checkEmailRepository.findByEmail(entity.getEmail());
    }


    public void delete(E entity) {
        entityManager.remove(entity);
    }

    public void deleteById(int primaryKey) {
        E entity = this.getById(primaryKey);
        delete(entity);
    }

    /**
     * Lấy bản ghi trong cơ sở dữ liệu theo khóa chính ID.
     *
     * @param
     * @return
     */
    public E getById(int primaryKey) {
        return entityManager.find(clazz(), primaryKey);
    }


    /**
     * Lấy tất cả bản ghi trong cơ sở dữ liệu.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        Table tbl = clazz().getAnnotation(Table.class);
        return (List<E>) entityManager.createNativeQuery("SELECT * FROM " + tbl.name(), clazz()).getResultList();
    }

    /**
     * thực thi câu lệnh truy vấn cơ sở dữ liệu
     *
     * @param sql  -> ví dụ chạy câu lệnh [SELECT * FROM tbl_category;]
     * @param page
     * @return
     */
    public PagerData<E> executeByNativeSQL(String sql, int page) {
        PagerData<E> result = new PagerData<E>();

        try {
            Query query = entityManager.createNativeQuery(sql, clazz());

            //trường hợp có thực hiện phân trang thì kết quả trả về
            //bao gồm tổng số page và dữ liệu page hiện tại
            if (page > 0) {
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

}
