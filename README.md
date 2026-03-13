# Lab CodeQL - Deteção de Vulnerabilidades

## C-Academy - Engenharia de Software Seguro

Projeto Java com vulnerabilidades intencionais para demonstração de análise estática com CodeQL.

> ⚠️ AVISO: Código vulnerável para fins educacionais. NÃO usar em produção!

---

## Vulnerabilidades Incluídas

| Ficheiro | CWE | Vulnerabilidade | Query CodeQL |
|----------|-----|-----------------|--------------|
| `UserController.java` | CWE-89 | SQL Injection | `java/sql-injection` |
| `SearchController.java` | CWE-79 | Cross-site Scripting | `java/xss` |
| `SystemService.java` | CWE-78 | Command Injection | `java/command-line-injection` |
| `FileService.java` | CWE-22 | Path Traversal | `java/path-injection` |
| `SecurityUtils.java` | CWE-502 | Insecure Deserialization | `java/unsafe-deserialization` |
| `SecurityUtils.java` | CWE-327 | Weak Cryptography | `java/weak-cryptographic-algorithm` |
| `DatabaseConfig.java` | CWE-798 | Hardcoded Credentials | `java/hardcoded-credential-*` |

---

## Estrutura

```
vulnerable-java-lab/
├── pom.xml
├── README.md
└── src/main/java/pt/isel/vulnerableapp/
    ├── controller/
    │   ├── UserController.java       # CWE-89
    │   └── SearchController.java     # CWE-79
    ├── service/
    │   ├── SystemService.java        # CWE-78
    │   └── FileService.java          # CWE-22
    ├── util/
    │   ├── SecurityUtils.java        # CWE-502, CWE-327
    │   └── DatabaseConfig.java       # CWE-798
    └── model/
        └── User.java
```