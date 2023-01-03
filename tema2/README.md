# APD Homework 2 ~ Black Friday orders manager in Java
### Chiper Alexandra-Diana
335CB 2022-2023

## I. Flow
### Main
1. open a shared ForkJoinPool with P lvl.2 - ProductTask workers
2. open input file
3. create output file
4. start the threads that handles orders (OrderTask) -> P threads in total
5. waits for the lvl.1 threads to finish by joining them
6. close whatever needs to be closed

### Order Task
Read line-by-line the (shared) input file with orders
1. read corresponding line -> !!! need synchronisation* for read
   1. if the order has 0 products -> get a new order
2. add with ***invoke*** a new task in the shared <fjp> for the current order -> threads need to 
   wait 
for the workers aka ProductTasks to finish their job before moving on
3. write at the out file -> !!! needs synchronisation* on write

```
* synchronisation problem is solved with synchronised blocks on the read/write resource
```

### ProductTask

1. receives id_order, total nr of products and the current number of processed products
2. search the input file for the specific product
3. when the product is found write at the output file -> !!! needs of synchronisation
4. if the current product is not the last one fork a new task

## Implementation details
- To ensure the fact that all lvl.1 threads wait for their whole order to be processed, the 
  blocking invoke method of the fjp was used
- In the lvl.2 threads the forked task are joined
- For lvl.2 parallelism the forked tasks use a shared scanner for picking up the read process 
  where the threads that searched for the previous product left off