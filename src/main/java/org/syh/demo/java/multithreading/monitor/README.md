# Notes
```
synchronized
wait
notify
notifyAll
```

Lifecycle and status of a thread:

When a thread calls `wait` method, it releases the lock and goes to the __WAITING__ state. When another thread calls `notify` or `notifyAll` method (or timeout occurs), the waiting thread will be notified and try to acquire the lock again. If it acquires the lock, it will continue to execute (in the __RUNNING__ or __RUNNABLE__ state), otherwise it will continue to wait (in __BLOCKED__ state).

__WAITING__ never goes directly to __RUNNING__ or __RUNNABLE__ state, it goes to __BLOCKED__ state first (try to acquire the lock in this state).

```
Lock & Condition
await
signal
signalAll
```
