package kr.debop4j.web.data;

import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring MVC 에서 servlet 시작과 완료 시에 UnitOfWork를 시작하고, 완료하도록 합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 15.
 */
@Slf4j
public class UnitOfWorkInterceptor implements HandlerInterceptor {

    /**
     * Controller 가 수행되기 전에 호출됩니다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        UnitOfWorks.start();
        return true;
    }

    /**
     * Controller의 메소드가 수행이 완료되고, View 를 호출하기 전에 호출됩니다.
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        // Nothing to do
    }

    /**
     * View 작업까지 완료된 후 Client에 응답하기 바로 전에 호출됩니다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

        if (UnitOfWorks.isStarted()) {
            if (log.isDebugEnabled())
                log.debug("Hint: Session 변경 내용이 DB에 적용하기 위해서는 UnitOfWorks.getCurrent().flushSession() 를 수행해 주세요^^");

            UnitOfWorks.stop();

            if (log.isDebugEnabled())
                log.debug("Client 요청 처리를 완료하였으므로, UnitOfWork를 종료했습니다.");
        }
//        IUnitOfWork unitOfWork = UnitOfWorks.getCurrent();
//        if (unitOfWork != null) {
//            unitOfWork.close();
//            if (log.isDebugEnabled())
//                log.debug("Client 요청 처리를 완료하였으므로, UnitOfWork를 종료했습니다.");
//        }
    }
}
