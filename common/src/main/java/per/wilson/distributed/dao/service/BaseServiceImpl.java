package per.wilson.distributed.dao.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import per.wilson.distributed.dao.BaseDAO;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * BaseServiceImpl
 *
 * @author Wilson
 * @since 2018-07-10
 */
public class BaseServiceImpl<ID extends Serializable, T> implements BaseService<ID, T> {
    @Autowired
    protected BaseDAO<T> baseDAO;

    @Override
    @Cacheable(cacheNames = "cacheManager", key = "#key")
    public T getById(ID key) {
        return baseDAO.selectById(key);
    }

    @Override
//    @Cacheable(key = "#field.concat(#value)")
    public T getByField(String field, Object value) {
        return (T) Optional.ofNullable(baseDAO.selectList(new EntityWrapper<T>().eq(field, value).last("limit 0,1")))
                .map(e -> e.isEmpty() ? null : e.get(0))
                .orElse(null);
    }

    @Override
//    @Cacheable
    public T getByWrapper(EntityWrapper<T> wrapper) {
        return (T) Optional.ofNullable(baseDAO.selectList(wrapper.last("limit 0,1")))
                .map(e -> e.isEmpty() ? null : e.get(0))
                .orElse(null);
    }

    @Override
//    @CachePut(unless = "#result == null ")
    public List<T> listByWrapper(Wrapper<T> wrapper) {
        return baseDAO.selectList(wrapper);
    }

    @Override
    public Page<T> pageByWrapper(Page<T> page, Wrapper<T> wrapper) {
        wrapper = (Wrapper<T>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseDAO.selectPage(page, wrapper));
        return page;
    }

    @Override
//    @CacheEvict(allEntries = true)
    public boolean insert(T t) {
        return baseDAO.insert(t) == 1;
    }

    @Override
//    @CacheEvict(allEntries = true)
    public boolean insertBatch(List<T> list) {
        return list.isEmpty() || baseDAO.insertBatch(list) == list.size();
    }

    @Override
//    @CacheEvict(allEntries = true)
    public boolean deleteByField(String field, Object value) {
        return baseDAO.delete(new EntityWrapper<T>().eq(field, value)) >= 0;
    }

    @Override
//    @CacheEvict(allEntries = true)
    public boolean update(T t) {
        return baseDAO.updateById(t) == 1;
    }
}
