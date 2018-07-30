package per.wilson.distributed.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BaseDAO
 *
 * @author Wilson
 * @since 2018-07-10
 */
@Repository
public interface BaseDAO<T> extends BaseMapper<T> {
    int insertBatch(@Param("list") List<T> list);
}
