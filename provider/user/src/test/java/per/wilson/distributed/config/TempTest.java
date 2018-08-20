package per.wilson.distributed.config;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * TempTest
 *
 * @author Wilson
 * @date 18-8-18
 */

public class TempTest {
    @Test
    void temp(){
        String[] arrA = new String[]{"aaa","bbb","我你他"};
        System.out.println(Arrays.toString(arrA));
        System.out.println(Arrays.deepToString(arrA));
    }
}
