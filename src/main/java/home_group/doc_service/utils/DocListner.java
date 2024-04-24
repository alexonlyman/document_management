package home_group.doc_service.utils;

import ch.qos.logback.classic.Logger;
import home_group.doc_service.dto.DocumentInfo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocListner {

    private final JavaMailSender javaMailSender;
    private final DeserializeDoc deserializeDoc;
    Logger logger = (Logger) LoggerFactory.getLogger(DocListner.class);

    @KafkaListener(topics = "document", groupId = "homeGroup")
    public void listner(String message) {
        try {
            if (!message.isEmpty()) {
                sendEmail(message);
            } else {
                logger.warn("Received empty message");
            }
        } catch (MessagingException e) {
            logger.error("Error sending email for message: {}", message, e);
        }

    }

    public void sendEmail(String data) throws MessagingException {
        DocumentInfo info = deserializeMessage(data);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bondjigaralex@yandex.ru");
        message.setTo("chuvakchel@mail.ru");
        message.setSubject("new document created " + info.getDateTime());
        message.setText(info.getDescription());
        message.setText(info.getEmail());
        logger.info("sended data " + message.toString());
        javaMailSender.send(message);

    }

    private DocumentInfo deserializeMessage(String message) {
        try {
            return deserializeDoc.deserialize("document", message.getBytes());
        } catch (RuntimeException exception) {
            throw new JsonParseException();
        }
    }
}
