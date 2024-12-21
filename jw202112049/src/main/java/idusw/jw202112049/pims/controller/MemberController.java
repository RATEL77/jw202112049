package idusw.jw202112049.pims.controller;

import idusw.jw202112049.pims.model.Member;
import idusw.jw202112049.pims.repository.MemberDAO;
import idusw.jw202112049.pims.repository.MemberDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name="memberController",
        urlPatterns = {"/members/login-form.do", "/members/login.do", "/members/logout.do",
                "/members/post-form.do", "/members/post.do", "/members/detail.do"})
public class MemberController extends HttpServlet {
    public void init() { }
    public void doProcess(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf('/') + 1); // action = post, get, get-list

        MemberDAO dao = new MemberDAOImpl();
        Member member = null;
        if(action.equals("login-form.do")) {
            request.getRequestDispatcher("./member-login-form.jsp").forward(request,response);
            // request.getRequestDispatcher("../members/member-login-form.jsp").forward(request,response);
        } else if(action.equals("login.do")) {
            // Repository or DAO, Model or DTO, Business Logic(Service)
            member = new Member();
            member.setEmail(request.getParameter("email"));
            member.setPw(request.getParameter("pw"));

            Member retMember = dao.read(member);
            if(retMember != null ) {
                // sessionScope.logined로 다른 jsp에서 사용 중
                session.setAttribute("logined", retMember);
                request.getRequestDispatcher("../main/index.jsp").forward(request, response);
            }
            else
                request.getRequestDispatcher("../status/error.jsp").forward(request,response);
        } else if(action.equals("logout.do")) {
            session.invalidate(); // session  객체 무효화
            request.getRequestDispatcher("../main/index.jsp").forward(request,response);
        } else if(action.equals("detail.do")) {
            // Repository or DAO, Model or DTO, Business Logic(Service)
            member = (Member) session.getAttribute("logined");
            // logined에는 email, pw, username ...

            Member retMember = dao.read(member);
            if(retMember != null ) {
                // sessionScope.logined로 다른 jsp에서 사용 중
                request.setAttribute("member", retMember);
                request.getRequestDispatcher("../members/member-update-form.jsp").forward(request, response);
            }
            else
                request.getRequestDispatcher("../status/error.jsp").forward(request,response);
        } else if(action.equals("post-form.do")) {
            request.getRequestDispatcher("./member-post-form.jsp").forward(request,response);
        } else if(action.equals("post.do")) {
            // Repository or DAO, Model or DTO, Business Logic(Service)
            member = new Member();
            member.setEmail(request.getParameter("email"));
            member.setPw(request.getParameter("pw"));
            member.setUsername(request.getParameter("username"));
            member.setPhone(request.getParameter("phone"));
            member.setAddress(request.getParameter("address"));
            // System.out.println(request.getParameter("address"));
            if(dao.create(member) > 0)
                request.getRequestDispatcher("../status/message.jsp").forward(request,response);
            else
                request.getRequestDispatcher("../status/error.jsp").forward(request,response);
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doProcess(request, response);
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doProcess(request, response);
    }

    public void destroy() {
    }
}