package net.akaigo15;

public class Wait implements StateExecutor {

  private static final long WAIT_TIME_MILSEC = 2 * 60 * 1000;

  public void init() {

  }

  public boolean execute() {
    try {
      Thread.sleep(WAIT_TIME_MILSEC);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return true;
  }

}
