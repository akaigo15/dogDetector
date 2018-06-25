package net.akaigo15;

public class SendText implements StateExecutor {

  public void init() {

  }

  public boolean execute() {
    Main.LOG("Sending a text!");
    return true;
  }

}
