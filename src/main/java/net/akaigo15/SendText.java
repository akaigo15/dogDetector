package net.akaigo15;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SendText implements StateExecutor {

  private static final String PROPFILE = "email.properties";

  private List<String> recipients;
  private Properties properties;

  public void init() {

    properties = new Properties();
    try {
      properties.load(new FileReader(PROPFILE));
    } catch (IOException e) {
      throw new RuntimeException("Unable to read " + PROPFILE,e);
    }

    String unparesedRecipients = properties.getProperty("email.recpients");
    if(unparesedRecipients == null || unparesedRecipients.length() == 0) {
      throw new RuntimeException("Missing recipients");
    }
    String[] vals = unparesedRecipients.split(",");

    recipients = new ArrayList<>();
    for(String s : vals) {
      if(!s.contains("@")) {
        throw new RuntimeException("Bad recipient: " + s);
      }
      recipients.add(s);
    }
  }

  public boolean execute() {
    Main.LOG("Sending a text!");

    Email email = new SimpleEmail();
    email.setHostName(properties.getProperty("email.hostName"));
    email.setSmtpPort(Integer.parseInt(properties.getProperty("email.port")));
    email.setAuthentication(properties.getProperty("email.userName"), properties.getProperty("email.password"));
    email.setSSLOnConnect(Boolean.parseBoolean(properties.getProperty("email.setSSLOnConnect")));

    try {
      email.setFrom(properties.getProperty("email.from"));
      email.setSubject(properties.getProperty("email.subject"));
      email.setMsg(properties.getProperty("email.msg"));

      recipients.forEach(s-> addRecipiant(email, s));

      email.send();
    }
    catch (Exception e){
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private void addRecipiant(Email email, String s) {
    try {
      email.addTo(s);
    } catch (EmailException e) {
      throw new RuntimeException("Bad email address: " + s,e);
    }
  }



}
