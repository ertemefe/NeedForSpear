package domain.objects;

public class Paddle {

    private static Paddle instance;

    private final int height = 20;
    private final int location_y = 450;
    private int width;
    private int location_x;
    private int angle;
    private int remainingFrozenTime = 0;

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }



    private Paddle() {}

    public static Paddle getInstance(int width, int location_x) {
        if (instance == null)
            instance = new Paddle();
        instance.width = width;
        instance.location_x = location_x;
        return instance;
    }

    public int getX() {
        return location_x;
    }

    public void setX(int location_x) {
        this.location_x = location_x;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getY() {
        return location_y;
    }

    public void moveRight(int dx) {
        location_x += dx;
    }

    public void moveLeft(int dx) {
        location_x -= dx;
    }

    public void rotate(String direction) {
        if (direction.equals("right")) {
            rotateRight();
        } else {
            rotateLeft();
        }
    }

    private void rotateLeft() {
        if (angle >= -80) {
            angle -= 10;
        } else {
            angle = -90;
        }
    }

    private void rotateRight() {
        if (angle <= 80) {
            angle += 10;
        } else {
            angle = 90;
        }
    }




}
