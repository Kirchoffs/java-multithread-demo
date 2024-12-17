# Notes

## Thread Pool
CompletableFuture executes these tasks in a thread obtained from the global `ForkJoinPool.commonPool()`.

But you can also create a thread pool and pass it to runAsync() and supplyAsync() methods to let them execute their tasks in a thread obtained from your thread pool.

All the methods in the CompletableFuture API has two variants - one which accepts an Executor as an argument and one which does not:
```
static CompletableFuture<Void>	runAsync(Runnable runnable)
static CompletableFuture<Void>	runAsync(Runnable runnable, Executor executor)
static <U> CompletableFuture<U>	supplyAsync(Supplier<U> supplier)
static <U> CompletableFuture<U>	supplyAsync(Supplier<U> supplier, Executor executor)
```

### Difference between Runnable and Supplier
```
@FunctionalInterface
public interface Runnable {
    void run();
}
```

```
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

## Callback
All the callback methods provided by CompletableFuture have two async variants:
```
// thenApply() variants
<U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
<U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
<U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
```

These async callback variations help you further parallelize your computations by executing the callback tasks in a separate thread.

## CompletableFuture
### thenRun & thenAccept & thenApply
- `thenRun(Runnable runnable)` does not accept the result of the previous stage and passes nothing to the next stage.
- `thenAccept(Consumer consumer)` accepts the result of the previous stage but does not pass anything to the next stage.
- `thenApply(Function function)` accepts the result of the previous stage and passes the result to the next stage.

### whenComplete & handle
- `whenComplete(BiConsumer action)` can handle both the result and the exception.
- `handle(BiFunction fn)` can handle both the result and the exception and return a new value.
