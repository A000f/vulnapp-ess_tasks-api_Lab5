package pt.isel.vulnerableapp.service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * VULNERABILIDADE: CWE-78 - OS Command Injection
 * 
 * Este serviço demonstra vulnerabilidades de Command Injection onde
 * input do utilizador é passado diretamente para comandos do SO.
 * 
 * @author C-Academy - Engenharia de Software Seguro
 */
public class SystemService {

    /**
     * VULNERÁVEL: Command Injection via Runtime.exec()
     * 
     * Payload (Linux): ; cat /etc/passwd
     * Payload (Windows): & type C:\Windows\win.ini
     */
    public String pingHost(HttpServletRequest request) {
        String host = request.getParameter("host"); 
        
        try {
            String command = "ping -c 4 " + host;
            
            Process process = Runtime.getRuntime().exec(command); 
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            return output.toString();
            
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * VULNERÁVEL: Command Injection via ProcessBuilder
     * 
     * Payload: test.txt; rm -rf /tmp/*
     */
    public String readFile(HttpServletRequest request) {
        String filename = request.getParameter("file"); 
        
        try {
            ProcessBuilder pb = new ProcessBuilder("cat", filename); 
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            return output.toString();
            
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * VULNERÁVEL: Command Injection com shell
     * 
     * Payload: $(whoami)
     */
    public String executeBackup(HttpServletRequest request) {
        String directory = request.getParameter("dir"); 
        
        try {
            String[] command = {"/bin/sh", "-c", "tar -czf backup.tar.gz " + directory};
            
            Process process = Runtime.getRuntime().exec(command); 
            
            int exitCode = process.waitFor();
            return exitCode == 0 ? "Backup successful" : "Backup failed";
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * VULNERÁVEL: Command Injection em DNS lookup
     * 
     * Payload: google.com; id
     */
    public String dnsLookup(HttpServletRequest request) {
        String domain = request.getParameter("domain"); 
        
        try {
            Process process = Runtime.getRuntime().exec("nslookup " + domain); 
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            return output.toString();
            
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
