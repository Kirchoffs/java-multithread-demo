# Notes

## Executor Framework
### Difference between Thread and Executor
The main difference between a Thread and an Executor in Java is that the latter provides a thread pool. 

### Difference between Executor, ExecutorService and Executors
Executor is the core interface for the abstraction of parallel execution, which separates tasks from execution.

ExecutorService is an extension of the Executor interface and provides a facility for returning a Future object and terminate, 
or shut down the thread pool. Once the shutdown is called, the thread pool will not accept new tasks but complete any pending task. 
It also provides the `submit()` method which extends `Executor.execute()` method and returns a Future.

Executors class is an utility class, which provides factory methods to create different kinds of thread pools.  
e.g.:  
`newSingleThreadExecutor()` creates a thread pool of just one thread;  
`newFixedThreadPool(int numOfThreads)` creates a thread pool of a fixed number of threads;  
`newCachedThreadPool()` creates new threads when needed but reuse the existing threads if they are available;

## ForkJoin Framework
### ForkJoinPool
The ForkJoinPool is the heart of the framework. It is an implementation of the ExecutorService that manages worker threads 
and provides us with tools to get information about the thread pool state and performance.

Worker threads can execute only one task at a time, but the ForkJoinPool does not create a separate thread for every 
single subtask. Instead, each thread in the pool has its own double-ended queue (or deque, pronounced "deck") that stores tasks.
This architecture is vital for balancing the thread’s workload with the help of the __work-stealing algorithm__.

|                                | Call from non-fork/join clients | Call from within fork/join computations         |
|--------------------------------|---------------------------------|-------------------------------------------------|
| Arrange async execution        | execute(forkJoinTask)           | forkJoinTask.fork()                             |
| Await and obtain result        | invoke(forkJoinTask)            | forkJoinTask.invoke()                           |
| Arrange exec and obtain Future | submit(forkJoinTask)            | forkJoinTask.fork() (ForkJoinTasks are Futures) |

### ForkJoinWorkerThread
A thread managed by a ForkJoinPool, which executes ForkJoinTasks. This class is subclassable solely for the sake of adding 
functionality, which means there are no overridable methods dealing with scheduling or execution. However, you can override initialization 
and termination methods surrounding the main task processing loop. If you do create such a subclass, you will also need to 
supply a custom `ForkJoinPool.ForkJoinWorkerThreadFactory` to use it in a ForkJoinPool.

### ForkJoinTask
Abstract base class for tasks that run within a ForkJoinPool. A ForkJoinTask is a thread-like entity that is much lighter 
weight than a normal thread. Huge numbers of tasks and subtasks may be hosted by a small number of actual threads in a ForkJoinPool, 
at the price of some usage limitations.  
- `RecursiveTask<V>` extends `ForkJoinTask<V>`
- `RecursiveAction` extends `ForkJoinTask<Void>`

Also, it provides some util functions:
```
ForkJoinTask.invokeAll(recursiveTaskList);
```