package kr.debop4j.data.hibernate.forTesting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

/**
 * kr.debop4j.data.hibernate.forTesting.UnitOfWorkTestContextBase
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
@Slf4j
public abstract class UnitOfWorkTestContextBase implements AutoCloseable {

    public final String CurrentHibernateSessionKey = UUID.randomUUID().toString();

    /**
     * IoC Container
     */
    protected ApplicationContext springContext;

    protected String springContextLocation;

    private UnitOfWorkTestContextDbStrategy dbStrategy;
    private MappingInfo mappingInfo;
    private int unitOfWorkNestingLevel = -1;


    @Override
    public void close() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
