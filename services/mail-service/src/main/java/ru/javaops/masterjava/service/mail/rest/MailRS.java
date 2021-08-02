package ru.javaops.masterjava.service.mail.rest;


import javafx.scene.Group;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotBlank;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailServiceExecutor;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.web.WebStateException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Collections;

@Path("/")
public class MailRS {
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Test";
    }

    @POST
    @Path("send")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response send(@NotBlank @FormDataParam("users") String users,
                         @FormDataParam("subject") String subject,
                         @FormDataParam("body") String body,
                         @FormDataParam("attach")InputStream attachStream,
                         @FormDataParam("attach") FormDataContentDisposition fileDetail ) throws WebStateException {
        String t = subject;
        GroupResult result = MailServiceExecutor.sendBulk(MailWSClient.split(users), subject, body, Collections.emptyList());
        return Response.status(200).build();
    }
}