package per.wilson.distributed.config;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.MessageCodeFormatter;

/**
 * EnMessageCodeFormatter
 *
 * @author Wilson
 * @date 18-7-13
 */
@Component
public class EnMessageCodeFormatter implements MessageCodeFormatter {

    @Override
    public String format(String errorCode, @Nullable String objectName, @Nullable String field) {
        return null;
    }
}
