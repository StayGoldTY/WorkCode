package main;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.demo.dzxx.restfulDemo;




public class EsbMethodServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String EsbType = req.getParameter("EsbType");
            restfulDemo rd = new restfulDemo(EsbType);
            if (EsbType == "1") {
                rd.GetData(req,EsbType);
            }
        } catch (Exception e) {

        }

    }
}


