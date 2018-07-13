package per.wilson.distributed.constant;

/**
 * GlobalConstant
 *
 * @author Wilson
 * @date 18-7-10
 */
public interface GlobalConstant {
    String BASE_PACKAGE = "per.wilson.distributed";
    String MODEL_PACKAGE = BASE_PACKAGE + ".model";
    String DAO_PACKAGE = BASE_PACKAGE + ".dao";
    String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
    String MAPPER_PATH = "classpath*:mappers/*.xml";
}
