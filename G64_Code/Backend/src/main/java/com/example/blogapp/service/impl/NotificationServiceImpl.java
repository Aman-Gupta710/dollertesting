package com.example.blogapp.service.impl;

import com.example.blogapp.config.AppConstants;
import com.example.blogapp.controller.AuthController;
import com.example.blogapp.entity.Post;
import com.example.blogapp.entity.Subscriber;
import com.example.blogapp.repository.SubscriberRepository;
import com.example.blogapp.service.NotificationService;
import freemarker.core.ParseException;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private Configuration freeMarkerConfig;

    @Autowired
    JavaMailSender mailSender;
    @Override
    @Async
    public void send(Post post) {

        System.out.println("\n----------\n" +" Notification Service : "+
                Thread.currentThread().getName()+ "\n----------");

        // Get all the users email
        List<Subscriber> subscribers = subscriberRepository.findAll();

        System.out.println("\n---------------------\n");
        int count = 0;
        for(Subscriber s: subscribers) {
            System.out.print(s.getEmail() + "  ");
            count++;
            if(count > 5) {
                System.out.println();
                count = 0;
            }
        }
        System.out.println("\n----------------------\n");

        for(Subscriber subscriber: subscribers) {
            try {
                if(subscriber.getEmail().equals(post.getUser().getEmail()))
                    continue;
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

                Map<String, String> model = new HashMap<>();

                model.put("name",subscriber.getUser().getName());
                model.put("postUser", post.getUser().getName());
                model.put("postCategory", post.getCategory().getCategoryTitle());
                model.put("postTitle", post.getTitle());
                model.put("postLink", AppConstants.frontendUrl.concat("/posts/") + post.getPostId());
                model.put("postImageLink", AppConstants.imageDownloadLink.concat(post.getImageName()));

                Template template = freeMarkerConfig.getTemplate("yellow-email-notification.ftl");
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);

                helper.setText(html, true);
                helper.setTo(subscriber.getEmail());
                helper.setSubject("New Post from " + post.getUser().getName());
                helper.setFrom("hello.campushub@gmail.com");
                mailSender.send(mimeMessage);
                log.info("Mail sent to : " + subscriber.getEmail());

            } catch (MessagingException ex) {
                log.error("Failed to send email : " + subscriber.getEmail(), ex);
                throw new IllegalStateException("Failed to send email");
            } catch (MailSendException ex) {
                log.error("Mail Server connection failed : " + subscriber.getEmail());
                throw new IllegalStateException("Failed to connect : " + subscriber.getEmail());
            } catch (TemplateException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
