package per.wilson.distributed.config.shiro;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.shiro.authc.AuthenticationToken;
import per.wilson.distributed.utils.JWTUtils;

/**
 * JWTToken
 *
 * @author Wilson
 * @date 18-7-20
 */
public class JWTToken implements AuthenticationToken {

    public static final String AUTHORIZATION = "Authorization";

    private String token;

    private DefaultClaims body;

    private Header header;

    public JWTToken(String token) {
        this.token = token;
        Jwt jwt = JWTUtils.parseToJwt(token);
        this.body = (DefaultClaims) jwt.getBody();
        this.header = jwt.getHeader();

    }

    @Override
    public String getPrincipal() {
        return body.get("username", String.class);
    }

    @Override
    public Object getCredentials() {
        return body.get("password", String.class);
    }

    public DefaultClaims getBody() {
        return body;
    }

    public void setBody(DefaultClaims body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JWTToken{");
        sb.append("token='").append(token).append('\'');
        sb.append(", body=").append(body);
        sb.append(", header=").append(header);
        sb.append('}');
        return sb.toString();
    }
}
