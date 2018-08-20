package per.wilson.distributed.service;


import per.wilson.distributed.dao.service.BaseService;
import per.wilson.distributed.entity.model.User;

import javax.validation.constraints.Max;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Wilson
 * @since 2018-07-10
 */
public interface UserService extends BaseService<Long, User> {
    int number(@Max(1) int number);

}
