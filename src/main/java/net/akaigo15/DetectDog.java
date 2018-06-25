package net.akaigo15;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.util.ArrayList;
import java.util.List;

public class DetectDog implements StateExecutor {

  private int isLow;
  private int lastLow;
  private int count;
  private int consecutiveHigh;

  private List<Integer> acceptableValues;

  private GpioPinDigitalInput input;

  private static final int DOG_DETECT_DELAY_MILSEC = 1000;
  private static final int DETECT_DOG_WINDOW = 10;
  private static final int DOG_ABSENT_WINDOW = 5;


  public void init() {
    isLow = 0;
    lastLow = 0;
    count = 0;
    consecutiveHigh = 0;

    acceptableValues = new ArrayList<Integer>();
    acceptableValues.add(0);
    acceptableValues.add(1);
    acceptableValues.add(2);

    input = GpioFactory.getInstance().provisionDigitalInputPin(RaspiPin.GPIO_02);
  }


  public boolean execute() {
    isLow = 0;
    lastLow = 0;
    count = 0;
    consecutiveHigh = 0;

    while(isLow < DETECT_DOG_WINDOW) {
      PinState state = input.getState();


      //Detect if low, if not detect if consecutively high
      if(state == PinState.LOW) {
        isLow++;
        Main.LOG("Dog has been detected " + isLow + " times");
        lastLow = count;
      } else {
        if(!acceptableValues.contains(count - lastLow)) {
          consecutiveHigh++;
          Main.LOG("Dog not detected " + consecutiveHigh + " times last difference was: " + (count - lastLow));
        }
      }

      if(consecutiveHigh == DOG_ABSENT_WINDOW) {
        return false;
      }

      try {
        Thread.sleep(DOG_DETECT_DELAY_MILSEC);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ++count;
    }
    return true;
  }
}
