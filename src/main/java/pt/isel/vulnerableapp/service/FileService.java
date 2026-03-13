package pt.isel.vulnerableapp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * VULNERABILIDADE: CWE-22 - Path Traversal
 * 
 * Este serviço demonstra vulnerabilidades de Path Traversal onde
 * input do utilizador é usado para construir caminhos de ficheiros.
 * 
 * @author C-Academy - Engenharia de Software Seguro
 */
public class FileService {

    private static final String UPLOAD_DIR = "/var/www/uploads/";
    private static final String DOCUMENTS_DIR = "/var/www/documents/";

    /**
     * VULNERÁVEL: Path Traversal em download
     * 
     * Payload: ../../../etc/passwd
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String filename = request.getParameter("file"); 
        
        String filePath = UPLOAD_DIR + filename; // Path traversal
        
        File file = new File(filePath); 
        
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", 
                "attachment; filename=\"" + file.getName() + "\"");
            
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.sendError(404, "File not found");
        }
    }

    /**
     * VULNERÁVEL: Path Traversal em leitura
     * 
     * Payload: ....//....//etc/shadow
     */
    public String readDocument(HttpServletRequest request) {
        String docName = request.getParameter("doc"); 
        
        Path docPath = Paths.get(DOCUMENTS_DIR, docName); 
        
        try {
            return new String(Files.readAllBytes(docPath));
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * VULNERÁVEL: Path Traversal em upload
     * 
     * Payload no filename: ../../../var/www/html/shell.jsp
     */
    public String uploadFile(HttpServletRequest request, byte[] fileContent) {
        String filename = request.getParameter("filename"); 
        
        String uploadPath = UPLOAD_DIR + filename;
        
        try {
            File file = new File(uploadPath); 
            
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileContent);
            }
            
            return "File uploaded successfully";
            
        } catch (IOException e) {
            return "Upload failed: " + e.getMessage();
        }
    }

    /**
     * VULNERÁVEL: Path Traversal em leitura de logs
     * 
     * Payload: ../../../etc/passwd
     */
    public String viewLog(HttpServletRequest request) {
        String logFile = request.getParameter("log"); 
        
        String logPath = "/var/log/app/" + logFile;
        
        StringBuilder content = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(logPath))) { 
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
        
        return content.toString();
    }

    /**
     * VULNERÁVEL: Path Traversal em delete
     * 
     * Payload: ../../../important/config.xml
     */
    public boolean deleteFile(HttpServletRequest request) {
        String filename = request.getParameter("file"); 
        
        File file = new File(UPLOAD_DIR + filename); 
        
        return file.delete();
    }
}
