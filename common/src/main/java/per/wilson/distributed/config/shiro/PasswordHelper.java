package per.wilson.distributed.config.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import per.wilson.distributed.model.entity.User;

/**
 * Created by Wilson on 2017/6/21.
 */
public class PasswordHelper {
    private final static String ALGORITHM_NAME = "md5";
    private final static int HASH_ITERATIONS = 2;
    protected final static String SALT = "instrument";

    public static void encryptPassword(User manager) {
        manager.setPassword(new SimpleHash(ALGORITHM_NAME, manager.getPassword(), SALT, HASH_ITERATIONS).toHex());
    }

    public static String encryptPassword(String password) {
        return new SimpleHash(ALGORITHM_NAME, password, SALT, HASH_ITERATIONS).toHex();
    }

    public static String encryptPassword(char[] password) {
        return new SimpleHash(ALGORITHM_NAME, password, SALT, HASH_ITERATIONS).toHex();
    }

}
