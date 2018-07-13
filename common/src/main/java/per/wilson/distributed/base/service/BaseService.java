package per.wilson.distributed.base.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * BaseService
 *
 * @author Wilson
 * @since 2018-07-10
 */
public interface BaseService<T> {

    T getByField(String field, Object value);

    T getByWrapper(Wrapper<T> wrapper);

    List<T> listByWrapper(Wrapper<T> wrapper);

    Page<T> pageByWrapper(Page<T> page, Wrapper<T> wrapper);

    boolean insert(T t);

    boolean insertBatch(List<T> list);

    boolean deleteByField(String field, Object value);

    boolean update(T t);
}
