package per.distributed.common;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import per.wilson.distributed.utils.ListUtils;

import java.util.List;

/**
 * CommonTest
 *
 * @author Wilson
 * @date 18-8-14
 */
class CommonTest {

    @Test
    void temp() {
        List<String> list = Lists.newArrayList("aaa","ab","ad","bbb","c");
        System.out.println(ListUtils.list(list,e -> e.startsWith("a")));
    }
}
