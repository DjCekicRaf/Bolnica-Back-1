package raf.si.bolnica.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.properties.EmailProperties;
import raf.si.bolnica.user.repositories.UserRepository;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailProperties emailProperties;

    @Override
    public boolean sendEmail(String usersEmail, String password) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailProperties.getEmail(), emailProperties.getPassword());
            }
        });
        Message message = prepareMessage(session, emailProperties.getEmail(), usersEmail,password);
        try {
            Transport.send(message);
            System.out.println("Mail is sent");
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    private Message prepareMessage(Session session, String myAccountEmail, String recepient,String newPassword){

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("[RAF - BOLNICA] Zaboravljena lozinka");
            message.setText("Poštovani,\n" +
                    "\n" +
                    "U nastavku email-a se nalazi vaša nova lozinka kojom se možete prijaviti na IBIS bolnički sistem:\n" +
                    "\n" +
                    newPassword+
                    "\n\n" +
                    "Sve najbolje,\n" +
                    "IBIS Administrator");
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
