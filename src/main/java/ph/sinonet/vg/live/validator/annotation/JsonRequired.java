package ph.sinonet.vg.live.validator.annotation;

import java.lang.annotation.*;

/**
 * Created by kierpagdato on 8/3/16.
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRequired {
}
