package org.duzer.webapp;
/**
 * Created by duzer on 31.01.2017.
 */

// Import required java libraries
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HWFilter implements Filter {

    //logging init
    final static Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    public void init(FilterConfig arg0) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        //HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(true);

        // проверяем и устанавливаем параметр сессии - столбец по которому сортируется вывод книг, по умолчанию - автор
        String param = (String) session.getAttribute("sesCurOrder");
        if (param == null) {
            session.setAttribute("sesCurOrder", "BookAuthor");
            logger.debug("Filter: null session parameter sesCurOrder in user-session. Set it to 'BookAuthor'");
            logger.debug("Filter: Test: sesCurOrder is "+((String) session.getAttribute("sesCurOrder")));
        } else {
            logger.debug("Filter: sesCurOrder is "+((String) session.getAttribute("sesCurOrder")));
        }

        // проверяем и устанавливаем параметр сессии - порядок отображения столбца авторов или наименований
        param = (String) session.getAttribute("sesOrder");
        if (param == null) {
            session.setAttribute("sesOrder", "ASC");
            logger.debug("Filter: null session parameter sesOrder in user-session. Set it to 'ASC'");
        } else {
            logger.debug("Filter: sesOrder is "+((String) session.getAttribute("sesOrder")));
        }

        // передаем дальше
        chain.doFilter(req, res);
    }

    public void destroy() {}

}
