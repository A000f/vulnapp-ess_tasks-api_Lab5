package pt.isel.vulnerableapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import io.javalin.http.Context;

/**
 * VULNERABILIDADE: CWE-89 - SQL Injection
 * 
 * Este controlador demonstra vulnerabilidades de SQL Injection
 * onde input do utilizador é concatenado diretamente em queries SQL.
 * 
 * @author C-Academy - Engenharia de Software Seguro
 */
public class UserController {

    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    /**
     * VULNERÁVEL: SQL Injection via concatenação de strings
     * 
     * Payload de ataque: ' OR '1'='1' --
     */
    public void getUserByUsername(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String username = request.getParameter("username"); 
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // VULNERÁVEL: Concatenação direta
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            
            ResultSet rs = stmt.executeQuery(query); 
            
            PrintWriter out = response.getWriter();
            while (rs.next()) {
                out.println("User: " + rs.getString("username"));
                out.println("Email: " + rs.getString("email"));
            }
            
        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    /**
     * VULNERÁVEL: SQL Injection em autenticação
     * 
     * Payload: admin'--
     */
    public boolean authenticateUser(HttpServletRequest request) {
        String username = request.getParameter("username"); 
        String password = request.getParameter("password"); 
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT COUNT(*) FROM users WHERE username = '" + username 
                         + "' AND password = '" + password + "'";
            
            ResultSet rs = stmt.executeQuery(query); 
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * VULNERÁVEL: SQL Injection em DELETE
     * 
     * Payload: 1 OR 1=1
     */
    public void deleteUser(HttpServletRequest request) {
        String userId = request.getParameter("id"); 
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            String query = "DELETE FROM users WHERE id = " + userId;
            
            stmt.executeUpdate(query); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * VULNERÁVEL: CWE-22 - Path Traversal via Zip Slip
     *
     * Extrai ficheiros de um ZIP sem validar os caminhos das entradas,
     * permitindo escrita arbitrária de ficheiros fora do diretório destino.
     *
     * Payload de ataque: entrada ZIP com nome "../../etc/cron.d/backdoor"
     */
    public void addProfilePicture(Context ctx) {
        String userId = ctx.pathParam("userId");
        String destinationDir = "/app/profiles/" + userId;

        try {
            InputStream zipInput = ctx.uploadedFile("profileZip").content();
            ZipInputStream zis = new ZipInputStream(zipInput);
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                // ❌ VULNERABILIDADE: Usa diretamente entry.getName() SEM validação
                File profilePic = new File(destinationDir, entry.getName());

                // Cria diretórios parent sem validação
                profilePic.getParentFile().mkdirs();

                // Extrai o ficheiro sem validar o path
                Files.copy(zis, profilePic.toPath(),
                          StandardCopyOption.REPLACE_EXISTING);
            }

            zis.close();
            ctx.status(200).json("Profile pictures uploaded successfully");
        } catch (Exception e) {
            ctx.status(500).result("Error uploading profile pictures: " + e.getMessage());
        }
    }
}
