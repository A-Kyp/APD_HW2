import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Tema2 {
    public static final String orders = "orders.txt";
    public static final String products = "order_products.txt";

    public static void main(String[] args) {
        //1. open input file
        //2. create output file
        //3. start the threads that handles orders [OrderTask] -> P threads in total
        //4. close files

        int P = Integer.parseInt(args[1]);
        Thread[] t = new Thread[P];
        String path = args[0];
        ForkJoinPool fjp = new ForkJoinPool(P);
        FileWriter wOrders = null;
        FileWriter wProducts = null;
        Scanner orders_in = null;

        // <<< 1 >>>
        File pdt = new File(path + "/" + products);
        File odr = new File(path + "/" + orders);
        try {
            orders_in = new Scanner(odr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // <<< 2 >>>
        File odr_out = new File("orders_out.txt");
        try {
            if(!odr_out.createNewFile()){
                System.out.println("Error creating orders out file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wOrders = new FileWriter("orders_out.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File pdt_out = new File("order_products_out.txt");
        try {
            if(!pdt_out.createNewFile()){
                System.out.println("Error creating products out file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wProducts = new FileWriter("order_products_out.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // <<< 3 >>>
        for (int i = 0; i < P; ++i) {
            t[i] = new OrderTask(orders_in, wOrders, wProducts, pdt, fjp);
            t[i].start();
        }

        for (int i = 0; i < P; ++i) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // <<< 4 >>>
        //close whatever was opened!
        fjp.shutdown();
        assert orders_in != null;
        orders_in.close();
        try {
            assert wOrders != null;
            wOrders.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert wProducts != null;
            wProducts.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
