package com.kmvc.jeesite.common.service;
import com.kmvc.jeesite.common.persistence.DataEntity;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(
        readOnly = true
)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity<T>> extends BaseService {
    @Autowired
    protected D dao;

    public CrudService() {
    }

    public T get(String id) {
        return this.dao.get(id);
    }

    public T get(T entity) {
        return this.dao.get(entity);
    }

    public List<T> findList(T entity) {
        return this.dao.findList(entity);
    }

    public Page<T> findPage(Page<T> page, T entity) {
        entity.setPage(page);
        page.setList(this.dao.findList(entity));
        return page;
    }

    @Transactional(
            readOnly = false
    )
    public void save(T entity) {
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            this.dao.insert(entity);
        } else {
            entity.preUpdate();
            this.dao.update(entity);
        }

    }

    @Transactional(
            readOnly = false
    )
    public void delete(T entity) {
        this.dao.delete(entity);
    }
}