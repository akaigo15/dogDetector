package net.akaigo15;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.ArrayList;
import java.util.List;


public class SendText implements StateExecutor {

  private List<String> recipiants;


  public void init() {
    recipiants = new ArrayList<>();

    recipiants.add("");
    recipiants.add("");
  }

  public boolean execute() {
    Main.LOG("Sending a text!");

    Email email = new SimpleEmail();
    email.setHostName("");
    email.setSmtpPort(465);
    email.setAuthentication("", "");
    email.setSSLOnConnect(true);

    try {
      email.setFrom("");
      email.setSubject("");
      email.setMsg("");

      recipiants.forEach(s-> addRecipiant(email, s));

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
