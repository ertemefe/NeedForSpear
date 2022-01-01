package domain.objects;


public class Abilities {
    public static boolean unstoppableActive = false;
    public static boolean expansionActive = false;
    //private static final int LONGER_TIME = 30000;
    private final int L = 1200;
    private Paddle paddle = Paddle.getInstance(L / 10, L / 2);






    public Abilities() {

    }


    private void chanceGivingAbility(int remainingLives) {
        remainingLives++;
    }


    public void noblePhantasmExpansion() {
        if (!expansionActive) {
            expansionActive = true;
            paddle.startSlowness(30000);
            int initPaddleWidth = paddle.getWidth();
            int initCenterX = paddle.getX() + initPaddleWidth / 2;
            paddle.setX(initCenterX - initPaddleWidth);
            paddle.setWidth(initPaddleWidth * 2);
        }
    }




    public void magicalHex() {

    }


    public static void activateUnstoppableEnchantedSphere(Ball ball) {
        ball.startUnstoppable(30000);
        ball.setDamage(999);
        unstoppableActive = true;
    }

    public static void deactivateUnstoppableEnchantedSphere(Ball ball) {
        ball.setDamage(1);
        unstoppableActive = false;
    }


}
