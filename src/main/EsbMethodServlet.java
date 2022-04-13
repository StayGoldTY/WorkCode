package main;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
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
            Map<String, String> map = req.getParameterMap();
            String ret = rd.GetAllFileData(req,EsbType);
            resp.setContentType("text/html;charset=utf-8");//第二句，设置浏览器端解码
            resp.getWriter().write(ret);

        } catch (Exception e) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write(JSON.toJSONString(e.getMessage()));
        }

    }
}


