package com.project.nhatrotot.configs.sendgrid;

import java.io.IOException;

import com.project.nhatrotot.model.UserEntity;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendGridUlt {
    private String apiKey;
    private String templateId;

    public void sendMail(Mail mail, Personalization personalization) {
        try {
            mail.addPersonalization(personalization);
            mail.setTemplateId(templateId);
            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
        }
    }
}
