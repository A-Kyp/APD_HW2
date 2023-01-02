import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class OrderTask extends Thread{
    //1. read corresponding line -> !!! the need of synchronisation for read
        //1.2. if the order has 0 products -> get a new order

    //2. add with <invoke> a new task in the shared <fjp> for the current order -> threads need to
        // wait for the workers aka ProductTasks to finish their job before moving on

    //3. write at the out file -> !!! needs synchronisation on write

    final Scanner orders_in;
    final FileWriter orders_out;
    FileWriter products_out;
    File prodFile;
    ForkJoinPool fjp;
    String line;
    String[] args;

    public OrderTask(Scanner in, FileWriter ord_out, FileWriter prod_out, File prodFile,
                     ForkJoinPool fjp){
        this.orders_in = in;
        this.orders_out = ord_out;
        this.products_out = prod_out;
        this.prodFile = prodFile;
        this.fjp = fjp;
    }

    @Override
    public void run() {
        boolean keepGoing = true;
        while(keepGoing) {
            // <<< 1 >>>
            synchronized (orders_in) {
                if (!orders_in.hasNextLine()){
                    keepGoing = false;
                    break;
                }
                int ok = 1;
                while (orders_in.hasNextLine() && ok == 1) {
                    ok = 0;
                    line = orders_in.nextLine();
                    System.out.println(line);
                    args = line.split(",");

                    // <<< 1.2 >>>
                    if (Integer.parseInt(args[1]) == 0) {
                        ok = 1;
                        System.out.println("comanda cu 0 produse -> ignora");
                    }
                }
            }

            // <<< 2 >>>
            fjp.invoke(new ProductTask(products_out, args[0], Integer.parseInt(args[1]), prodFile));
            //finish
            // completing the
            // constructor

            // <<< 3 >>>
            synchronized (orders_out) {
                try {
                    orders_out.write(line + ",shipped\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
