package netology.ru;
public class Main {
    public static void main(String[] args) {
        MultiTreadServer multiTreadServer = new MultiTreadServer(64);
        multiTreadServer.start(9999);
    }
}