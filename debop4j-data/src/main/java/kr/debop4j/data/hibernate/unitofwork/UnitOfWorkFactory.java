package kr.debop4j.data.hibernate.unitofwork;

import kr.debop4j.core.Guard;
import kr.debop4j.core.Local;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Map;

/**
 * Hibernate 용 IUnitOfWork Factory 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 30.
 */
@Slf4j
public class UnitOfWorkFactory implements IUnitOfWorkFactory {

    public static final String CURRENT_HIBERNATE_SESSION = UnitOfWorkFactory.class.getName() + ".current.hibernate.session";

    protected final Object syncLock = new Object();
    protected SessionFactory sessionFactory;
    protected Map<String, SessionFactory> sessionFactories;

    @Override
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        Guard.shouldNotBeNull(sessionFactory, "sessionFactory");
        if (log.isInfoEnabled())
            log.info("SessionFactory를 설정합니다. sessionFactory=[{}]", sessionFactory);

        this.sessionFactory = sessionFactory;
    }

    @Override
    public Map<String, SessionFactory> getSessionFactories() {
        return this.sessionFactories;
    }

    @Override
    public void setSessionFactories(Map<String, SessionFactory> sessionFactories) {
        this.sessionFactories = sessionFactories;
    }

    @Override
    public Session getCurrentSession() {
        Session session = (Session) Local.get(CURRENT_HIBERNATE_SESSION);
        Guard.shouldBe(session != null, "Session이 현 Thread Context에서 생성되지 않았습니다. UnitOfWorks.getStart() 를 먼저 호출하셔야 합니다.");
        return session;
    }

    @Override
    public void setCurrentSession(Session session) {
        if (log.isDebugEnabled())
            log.debug("현 ThreadContext에서 사용할 Session을 설정합니다. session=[{}]", session);
        Local.put(CURRENT_HIBERNATE_SESSION, session);
    }


    @Override
    public void Init() {
        if (log.isInfoEnabled())
            log.info("Hibernate 용 UnitOfWorkFactory를 초기화합니다.");
    }

    @Override
    public IUnitOfWorkImplementor create(SessionFactory factory, IUnitOfWorkImplementor previous) {
        if (log.isDebugEnabled())
            log.debug("새로운 IUnitOfWorkImplementor 인스턴스를 생성합니다... factory=[{}], previous=[{}]",
                      factory, previous);

        if (factory == null)
            factory = this.sessionFactory;

        if (log.isDebugEnabled())
            log.debug("Local ThreadContext 에 Session을 설정합니다...");

        Session session = factory.openSession();
        Local.put(CURRENT_HIBERNATE_SESSION, session);

        return new UnitOfWorkAdapter(this, session, (UnitOfWorkAdapter) previous);
    }

    @Override
    public void closeUnitOfWork(IUnitOfWorkImplementor adapter) {
        Guard.shouldNotBeNull(adapter, "adapter");

        if (log.isDebugEnabled())
            log.debug("[{}]를 close 합니다.", adapter.getClass().getName());

        Session session = null;
        if (adapter.getPrevious() != null)
            session = adapter.getPrevious().getSession();

        setCurrentSession(session);
        UnitOfWorks.closeUnitOfWork(adapter);
    }
}
