package per.wilson.distributed.utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ListUtils
 *
 * @author Wilson
 * @date 18-8-14
 */
public class ListUtils {

    public  static<T> List<T> list(List<T>list, Predicate<T> predicate){
       return list.stream().filter(predicate).collect(Collectors.toList());
    }
}
