package pt.isel.vulnerableapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * VULNERABILIDADE: CWE-798 - Use of Hard-coded Credentials
 * 
 * @author C-Academy - Engenharia de Software Seguro
 */
public class DatabaseConfig {

    // VULNERÁVEL: Credenciais hardcoded
    private static final String DB_HOST = "production-db.example.com";
    private static final String DB_USERNAME = "admin"; 
    private static final String DB_PASSWORD = "Sup3rS3cr3tP@ssw0rd!"; 

    // VULNERÁVEL: API Keys hardcoded
    private static final String API_KEY = "sk-1234567890abcdef"; 
    private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE"; 
    private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"; 

    /**
     * VULNERÁVEL: Conexão com credenciais hardcoded
     */
    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://" + DB_HOST + ":5432/app";
        return DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD);
    }

    /**
     * VULNERÁVEL: Credenciais inline
     */
    public Connection getConnectionInline() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://db.example.com:3306/mydb",
            "root",           
            "password123"     
        );
    }

    /**
     * VULNERÁVEL: Credenciais em Properties
     */
    public Connection getConnectionWithProperties() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "dbadmin"); 
        props.setProperty("password", "AdminP@ss2024!"); 
        
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/testdb",
            props
        );
    }

    /**
     * VULNERÁVEL: Token hardcoded
     */
    public String getAuthToken() {
        return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0"; 
    }

    /**
     * VULNERÁVEL: Encryption key hardcoded
     */
    public byte[] getEncryptionKey() {
        return "MySecretKey12345".getBytes(); 
    }

    /**
     * VULNERÁVEL: SSH private key hardcoded
     */
    public String getSSHPrivateKey() {
        return "-----BEGIN RSA PRIVATE KEY-----\n" + 
               "MIIEpAIBAAKCAQEA0Z3VS5JJcds3xfn...\n" +
               "-----END RSA PRIVATE KEY-----";
    }
}
