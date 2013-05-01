package kr.debop4j.data.redis.ogm.annotation;

import java.lang.annotation.Annotation;

/**
 * IFinder
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 10. 오후 10:23
 */
public interface IFinder {

    public String findAnnotation(Annotation[] annotations, Object obj);

    public String findAnnotationBy(Annotation[] annotations, String am, Object obj);
}
