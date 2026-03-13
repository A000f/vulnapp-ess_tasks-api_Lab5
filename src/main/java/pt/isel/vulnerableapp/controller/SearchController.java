package pt.isel.vulnerableapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * VULNERABILIDADE: CWE-79 - Cross-site Scripting (XSS)
 * 
 * Este controlador demonstra vulnerabilidades de XSS onde input do utilizador
 * é refletido diretamente na resposta HTML sem encoding.
 * 
 * @author C-Academy - Engenharia de Software Seguro
 */
public class SearchController {

    /**
     * VULNERÁVEL: Reflected XSS
     * 
     * Payload: <script>alert('XSS')</script>
     */
    public void search(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String searchTerm = request.getParameter("q"); 
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        print(out, searchTerm);
        out.close();
    }

    private void print(PrintWriter pw, String searchTerm) {
        pw.println("<!DOCTYPE html>");
        pw.println("<html><body>");
        pw.println("<h1>Resultados para: " + searchTerm + "</h1>"); 
        pw.println("</body></html>");
    }

    /**
     * VULNERÁVEL: XSS em mensagem de erro
     * 
     * Payload: <img src=x onerror=alert('XSS')>
     */
    public void showError(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String errorMessage = request.getParameter("error"); 
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<div class='error'>Erro: " + errorMessage + "</div>"); 
    }

    /**
     * VULNERÁVEL: XSS em atributo HTML
     * 
     * Payload: " onclick="alert('XSS')" x="
     */
    public void showProfile(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String name = request.getParameter("name"); 
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<input type='text' value='" + name + "'>"); 
    }

    /**
     * VULNERÁVEL: XSS em contexto JavaScript
     * 
     * Payload: ';alert('XSS');//
     */
    public void dynamicContent(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String content = request.getParameter("content"); 
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<script>");
        out.println("var data = '" + content + "';"); 
        out.println("</script>");
    }
}
