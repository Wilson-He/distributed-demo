package per.wilson.distributed.service;


import per.wilson.distributed.base.service.BaseService;
import per.wilson.distributed.model.entity.User;

import javax.validation.constraints.Max;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Wilson
 * @since 2018-07-10
 */
public interface UserService extends BaseService<User> {
    int number(@Max(1) int number);
}
