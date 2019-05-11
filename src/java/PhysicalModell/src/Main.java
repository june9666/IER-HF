public class Main {

    public static void main(String[] args) {

        Press press = new Press();

        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(100);
                press.increaseTime();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
