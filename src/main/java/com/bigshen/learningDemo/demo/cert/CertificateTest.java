package com.bigshen.learningDemo.demo.cert;

import javax.security.auth.x500.X500Principal;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author byj
 * @date 2023/12/26
 * @Description
 */
public class CertificateTest {


    public static void main(String[] args) throws CertificateException, NoSuchProviderException {
        X509Certificate x509Certificate = CertParseUtil.parseBase64Cert("MIIGnjCCBYagAwIBAgIMb3gAAAAAAAAAAABuMA0GCSqGSIb3DQEBCwUAMCQxCzAJBgNVBAYTAkNOMRUwEwYDVQQDDAxrb2Fsc3ViY2Fyc2EwHhcNMjMxMjI2MDgxMzUzWhcNMjUxMjI1MDgxMzUyWjAgMQswCQYDVQQGEwJDTjERMA8GA1UEAwwI5rWL6K+VMDQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCe/dgULIUmUkand3sF0hPu88ijuCYag97N5/0o/YOhvoGosJkyYFQ7a5CWpcudb4IwY8rDWppGk1kFst2MaHJQ70FXOZA7V6JLkt9SRRzRdHnRj/0AZscnqKYgd+lN6EORwpIx+MjGwHjNh/bqUqBEJkVuMXbRZAWouBX47H7ULLFgS0HnOzS4jXzW3nwWnNLxmZPMdQArHLm6w3+AwIEIEVpueN0N1JAEBeKTNxhYzCMv4Z67q2B1cNCND0X/6T9oZ9+SD+ylB/U5CwYnF8vC+9jZp/sK0BKwCjvFANiCnrFRUnJQJkhO4EVVuMpeTy1+cUm7TUer3vQHt4GuhMvjAgMBAAGjggPSMIIDzjAdBgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwQwDgYDVR0PAQH/BAQDAgDAMBEGCWCGSAGG+EIBAQQEAwIAgDAfBgNVHSMEGDAWgBRYRZQkmGgae9qurJx8Wdl3XpYBeTCCAbgGA1UdHwSCAa8wggGrMIGkoIGhoIGehoGbbGRhcDovLy9DTj1rb2Fsc3ViY2Fyc2FwYXJ0MGdyb3VwMCxDTj1rb2Fsc3ViY2Fyc2Esb3U9Q1JMRGlzdHJpYnV0ZVBvaW50cyxkYz1leGFtcGxlLGRjPW9yZz9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2U/b2JqZWN0Y2xhc3M9Y1JMRGlzdHJpYnV0aW9uUG9pbnQwgbWggbKgga+GgaxsZGFwOi8vbGRhcC5zZXJ2ZXI6Mzg5L0NOPWtvYWxzdWJjYXJzYXBhcnQwZ3JvdXAwLENOPWtvYWxzdWJjYXJzYSwgT1U9Q1JMRGlzdHJpYnV0ZVBvaW50cywgZGM9ZXhhbXBsZSxkYz1vcmc/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP29iamVjdGNsYXNzPWNSTERpc3RyaWJ1dGlvblBvaW50MEqgSKBGhkRodHRwOi8vY2VydC5wdWJsaXNoLnNlcnZlcjo4ODgwL2Rvd25sb2FkL2tvYWxzdWJjYXJzYXBhcnQwZ3JvdXAwLmNybDCCAYwGCCsGAQUFBwEBBIIBfjCCAXowgY4GCCsGAQUFBzAChoGBbGRhcDovLy9DTj1rb2Fsc3ViY2Fyc2EsQ049a29hbHN1YmNhcnNhLE9VPWNBQ2VydGlmaWNhdGVzLGRjPWV4YW1wbGUsZGM9b3JnP2NBQ2VydGlmaWNhdGU/YmFzZT9vYmplY3RDbGFzcz1jZXJ0aWZpY2F0aW9uQXV0aG9yaXR5MIGfBggrBgEFBQcwAoaBkmxkYXA6Ly9sZGFwLnNlcnZlcjozODkvQ049a29hbHN1YmNhcnNhLENOPWtvYWxzdWJjYXJzYSwgT1U9Y0FDZXJ0aWZpY2F0ZXMsIGRjPWV4YW1wbGUsZGM9b3JnP2NBQ2VydGlmaWNhdGU/YmFzZT9vYmplY3RDbGFzcz1jZXJ0aWZpY2F0aW9uQXV0aG9yaXR5MEUGCCsGAQUFBzAChjlodHRwOi8vY2VydC5wdWJsaXNoLnNlcnZlcjo4ODgwL2Rvd25sb2FkL2tvYWxzdWJjYXJzYS5jZXIwHQYDVR0OBBYEFO/0zpmnF94Ewm3YJkgI32CQ8HVCMA0GCSqGSIb3DQEBCwUAA4IBAQCTMMQuU5u3qb0KwFhm56E+DUG3ggIexOHgONdLZYQSt18th8nSVkLXsJcyQkgYwJBoyYY1xCyiIrF/qltNGZfKRuTYbYIy6Zn/7RoE215mERmYrcqrgmrbZN7x2qDJCRvJ8lmaCTRTnRVxL4NTrGZ9HbGp/PlWnKapga97pyQU+SkjQmEuwP21A4aCe+uaDY7hby91SsJsgSjdlOD+nUWYGdFjN20NPoUdVx2DZr/o6YOXOrw2vrxpldx4SXhJNLKUcnNYIeJMR/BsRR9bJVgY0urTsWN1/IswGjalntMgN3jAOskvPKToIVtPDcXm/Vpp0AuP8Pu7wapr22fnepBD");
        String issueerDN = x509Certificate.getIssuerDN().getName();
        String sn = x509Certificate.getSerialNumber().toString(16);
        String publicKey = x509Certificate.getPublicKey().toString();

        test("C:\\Users\\Lenovo\\Desktop\\backup\\test\\my_ca.crt");
    }
    private static void test(String path) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            try (InputStream in = Files.newInputStream(Paths.get(path))) {
                X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(in);
                String name = certificate.getIssuerDN().getName();
                System.out.printf("%s\n", certificate.getIssuerX500Principal().getName(X500Principal.RFC1779));
                System.out.printf("%s\n", certificate.getIssuerX500Principal().getName(X500Principal.RFC2253));
                System.out.printf("%s\n", certificate.getIssuerX500Principal().getName(X500Principal.CANONICAL));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
