public class Print {

    private final Object mon = new Object();
    private int counter = 1;

    public void printA() throws InterruptedException {
        synchronized (mon){
            int i = 0;
            while (i < 5){
                while (counter != 1){
                    mon.wait();
                }
                System.out.println("A");
                i++;
                counter = 2;
                mon.notifyAll();
            }
        }
    }

    public void printB() throws InterruptedException {
        synchronized (mon){
            int i = 0;
            while (i < 5){
                while (counter != 2){
                    mon.wait();
                }
                System.out.println("B");
                i++;
                counter = 3;
                mon.notifyAll();
            }
        }
    }

    public void printC() throws InterruptedException {
        synchronized (mon){
            int i = 0;
            while (i < 5){
                while (counter != 3){
                    mon.wait();
                }
                System.out.println("C");
                i++;
                counter = 1;
                mon.notifyAll();
            }
        }
    }
}
