package per.wilson.distributed.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import per.wilson.distributed.entity.model.User;

import java.util.Objects;

/**
 * Created by Wilson on 2017/6/19.
 */
@Component
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Logger log = LoggerFactory.getLogger(this.getClass());

   /* private Cache<String, AtomicInteger> passwordRetryCache;

    @Autowired
    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        System.err.println(cacheManager);
        passwordRetryCache = cacheManager.getCache("shiro-activeSessionCache");
    }*/

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //retry count + 1
   /*     AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw 设置前
            throw new ExcessiveAttemptsException();
        }*/
        User user = (User) info.getPrincipals().asList().get(0);
        /*  if (matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }*/
        return Objects.equals(user.getPassword(), token.getCredentials());
    }
}
