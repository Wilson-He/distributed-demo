package per.wilson.distributed.utils;

import org.springframework.beans.BeanUtils;

/**
 * ObjectUtils
 *
 * @author Wilson
 * @date 18-7-10
 */
public class ObjectUtils {
    public static <T> T copyProperties(Object source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
