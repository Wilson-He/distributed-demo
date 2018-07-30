package per.wilson.distributed.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import per.wilson.distributed.dao.service.BaseServiceImpl;
import per.wilson.distributed.entity.model.User;
import per.wilson.distributed.service.UserService;

import javax.validation.constraints.Max;


/**
 * <p>
 * </p>
 *
 * @author Wilson
 * @since 2018-07-10
 */
@Service(timeout = 300)
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Override
    public int number(@Max(1) int number) {
        return number;
    }
}
