# APD Homework 2 ~ Black Friday orders manager in Java + BONUS
### Chiper Alexandra-Diana
335CB 2022-2023

To efficiently read and assign the orders to the OrderTask threads a 
shared reference to the scanner was used such as each thread can read
the new "available" line from the input file for a dynamic distribution of
the tasks. The input file is thus read only once. This method can also 
balance the load between threads, as the ones that are faster or have smaller
orders to be processed can "steal" other orders from the busier threads.

A similar idea is used for the lvl.2 threads: the input file is opened only once
per order and only by the thread that handles the first product of that order.
For the resulting forked task, a reference to the input file is passed so that
the work of previous threads is saved and the resources are not wasted checking twice
for the next product.