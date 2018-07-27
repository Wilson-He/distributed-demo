package per.wilson.distributed.controller.impl;

import org.springframework.web.bind.annotation.RestController;
import per.wilson.distributed.controller.BusinessController;

/**
 * BusinessControllerImpl
 *
 * @author Wilson
 * @date 18-7-23
 */
@RestController
public class BusinessControllerImpl implements BusinessController{
    @Override
    public Object admin() {
        return "You have admin permission";
    }

    @Override
    public Object manager() {
        return "You have manager permission";
    }
}
