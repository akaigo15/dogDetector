package net.akaigo15;


public class Main {

    private enum State {
        INIT,
        DETECT_DOG,
        SEND_TEXT,
        WAIT
    }

    private static final boolean DEBUG = true;
    private static final long RESTART_DELAY_MILSEC = 2 * 60 * 1000;

    private static State state = State.INIT;

    private static DetectDog detectDog;
    private static Wait wait;
    private static SendText sendText;


    public static void main( String[] args ) {


        while(true) {

            try {
                LOG("Current state: " + state);

                switch(state) {

                    case INIT: {
                        init();
                        state = State.DETECT_DOG;
                        break;
                    }

                    case DETECT_DOG: {
                        if(detectDog.execute()) {
                            state = State.SEND_TEXT;
                        }
                        break;
                    }

                    case SEND_TEXT: {
                        if(sendText.execute()) {
                            state = State.WAIT;
                        } else {
                            state = State.INIT;
                        }

                        break;
                    }

                    case WAIT: {
                        if(wait.execute()) {
                            state = State.DETECT_DOG;
                        }
                        break;
                    }

                    default: {
                        throw new IllegalStateException("Illegal state: " + state);
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();

                try {
                    Thread.sleep(RESTART_DELAY_MILSEC);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                state = State.INIT;
            }

        }
    }

    private static void init() {
        detectDog = new DetectDog();
        wait = new Wait();
        sendText = new SendText();

        detectDog.init();
        wait.init();
        sendText.init();

    }

    public static void LOG(String s) {
        if(DEBUG) {
            System.out.println(s);
        }
    }
}
