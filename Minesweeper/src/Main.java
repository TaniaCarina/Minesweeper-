public class Main implements Runnable{

    Interfata interfata = new Interfata();

    public static void main(String[] args) {
        new Thread(new Main()).start();

    }

    @Override
    public void run() {
        while(true){
            interfata.repaint();
            if(!Interfata.isReset){
                interfata.ifWin();
            }
        }
    }
}