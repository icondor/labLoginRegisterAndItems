package usermanagement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import usermanagement.db.DBFoodList;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/listfood")
public class ListFoodServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession s = req.getSession();
        Object o = s.getAttribute("id"); // daca pe sesiune exista obiectul numit id sau nu exista voi lua diferite decizii

        if(o!=null)
        {

            Integer i = (Integer)o;
            int iduser = (int)i;
            String search = req.getParameter("search");
            if(search==null)
                search="";

            DBFoodList db = new DBFoodList();
            List<MyFoodList> l = db.getFoodList(iduser, search);
            JSONObject json = new JSONObject();
            json.put("listFromBackend", l); // only food name
            String result = json.toString();
            returnJsonResponse(resp, result);
        }
        else
        {
            error(resp, "operation forbidden. user is not logged in.");
        }
    }

    private void returnJsonResponse(HttpServletResponse response, String jsonResponse) {
        response.setContentType("application/json");
        PrintWriter pr = null;
        try {
            pr = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert pr != null;
        pr.write(jsonResponse);
        pr.close();
    }

    private void error( HttpServletResponse resp, String mesaj) {

        try {
            PrintWriter pw = resp.getWriter();
            pw.println(mesaj);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}