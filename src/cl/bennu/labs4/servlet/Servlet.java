package cl.bennu.labs4.servlet;

import cl.bennu.labs4.scheluder.SQLScheluder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by _Camilo on 20-06-2014.
 */
public class Servlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        System.out.println("Iniciando servlet");
        super.init(config);
        SQLScheluder.getInstance().start();
    }

}
