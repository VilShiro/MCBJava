package org.fbs.mcb.data.user;

/**
 * This abstract class represents a runnable task for a bot. It provides methods to manage and execute the task in a separate thread.
 */
public abstract class BotRunnable {

    private Thread thread;

    /**
     * This method should be implemented by subclasses to define the logic of the bot's task.
     *
     * @param args An array of objects that can be passed to the task.
     */
    public abstract void invoke(Object... args);

    /**
     * Creates a new thread and sets it to execute the {@link #invoke(Object...)} method with the provided arguments.
     *
     * @param args The arguments to be passed to the {@link #invoke(Object...)} method.
     */
    final public void setThread(Object ... args){
        thread = new Thread(){
            @Override
            public void run() {
                invoke(args);
            }
        };
    }

    /**
     * Starts the execution of the task in the separate thread.
     */
    final public void run(){
        thread.start();
    }

    /**
     * Waits for the task to finish execution.
     *
     * @throws InterruptedException If the current thread is interrupted while waiting for the task to finish.
     */
    final public void join() throws InterruptedException {
        thread.join();
    }

    /**
     * Interrupts the task's execution.
     */
    final public void interrupt(){
        thread.interrupt();
    }

    /**
     * Checks if the task's execution has been interrupted.
     *
     * @return {@code true} if the task's execution has been interrupted; {@code false} otherwise.
     */
    final public boolean isInterrupted(){
        return thread.isInterrupted();
    }

}
