package vn.webapp.backend.auction.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.webapp.backend.auction.config.frontend.FrontendConfiguration;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailContent emailContent;

    SimpleMailMessage mailMessage = new SimpleMailMessage();

    @Value("${spring.mail.username}")
    private String emailUsername;

    private final FrontendConfiguration frontendConfiguration;

    @Async
    public void sendActivationEmail(String emailTo, String fullName, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String url = frontendConfiguration.getBaseUrl() + "/activation/" + token;

        String html = emailContent.setHtmlContent(fullName, "Kích hoạt tài khoản", url,
                "Cảm ơn bạn đã đăng ký tài khoản tại DGS.",
                "Vui lòng nhấn nút bên dưới để kích hoạt tài khoản: ");

        helper.setFrom(emailUsername);
        helper.setTo(emailTo);
        helper.setSubject("Kích hoạt tài khoản tại DGS.");
        helper.setText(html, true);

        javaMailSender.send(message);
    }

    @Async
    public void sendResetPasswordEmail(String to, String fullName, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String url = frontendConfiguration.getBaseUrl() + "/reset-mat-khau/" + token;

        String html = emailContent.setHtmlContent(fullName, "Đặt lại mật khẩu", url,
                "Bạn vừa yêu cầu đặt lại mật khẩu tại DGS",
                "Vui lòng nhấn nút bên dưới để đặt lại mật khẩu: ");

        helper.setFrom(emailUsername);
        helper.setTo(to);
        helper.setSubject("Đặt lại mật khẩu tại DGS.");
        helper.setText(html, true);

        javaMailSender.send(message);
    }

    @Async
    public void sendConfirmHoldingEmail(String to, String fullName, String assetName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String html = emailContent.setHtmlConfirmHoldingContent(fullName,assetName);

        helper.setFrom(emailUsername);
        helper.setTo(to);
        helper.setSubject("Xác nhận tài sản được gửi tới DGS thành công .");
        helper.setText(html, true);
        javaMailSender.send(message);
    }

    @Async
    public void sendBanParticipatingAccountEmail(String to, String fullName, String userName, String reason) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String html = emailContent.setHtmlBanParticipatingAccountContent(fullName,userName,reason);

        helper.setFrom(emailUsername);
        helper.setTo(to);
        helper.setSubject("Tài khoản DGS của bạn sẽ bị tạm khóa, không thể tham gia đấu giá!.");
        helper.setText(html, true);

        javaMailSender.send(message);
    }
}
