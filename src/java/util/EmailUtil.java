package util;

//import com.sun.jdi.connect.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Transport;


import java.util.Properties;

public class EmailUtil {

    public static class SmtpConfig {

        public String host;
        public int port;
        public boolean starttls;
        public boolean ssl;
        public String username;
        public String password;
        public String from;
    }

    public static void sendEmail(SmtpConfig config, String to, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(config.starttls));
        props.put("mail.smtp.ssl.enable", String.valueOf(config.ssl));
        props.put("mail.smtp.host", config.host);
        props.put("mail.smtp.port", String.valueOf(config.port));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.username, config.password);
            }
        });
        session.setDebug(true); 
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject, "UTF-8");
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            System.out.println("=== DEBUG: Bắt đầu gửi email đến " + to + " ===");
            Transport.send(message);
            System.out.println("✅ Email gửi thành công đến " + to);
        } catch (Exception ex) {
            System.err.println("❌ Gửi email thất bại:");
            ex.printStackTrace();
        }
    }

    public static String buildResetPasswordEmail(String userFullName, String resetUrl, String token) {
        String safeName = userFullName == null ? "bạn" : userFullName;
        return "<div style=\"font-family:Arial,sans-serif;line-height:1.6;color:#333\">"
                + "<h2>Đặt lại mật khẩu</h2>"
                + "<p>Chào " + escapeHtml(safeName) + ",</p>"
                + "<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>"
                + "<p>Vui lòng nhấn vào nút bên dưới để đặt lại mật khẩu:</p>"
                + "<p><a href='" + resetUrl + "' style=\"display:inline-block;background:#667eea;color:#fff;padding:10px 16px;border-radius:6px;text-decoration:none\">Đặt lại mật khẩu</a></p>"
                + "<p>Nếu nút không hoạt động, hãy dùng liên kết sau:</p>"
                + "<p><a href='" + resetUrl + "'>" + resetUrl + "</a></p>"
                + "<p>Lưu ý: Token có hiệu lực trong 15 phút và chỉ dùng được 1 lần.</p>"
                + "<hr><small>Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này.</small>"
                + "</div>";
    }

    private static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
