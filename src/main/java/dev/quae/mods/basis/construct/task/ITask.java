package dev.quae.mods.basis.construct.task;

public interface ITask {
    void tick();

    boolean isDone();

    interface Executor<T extends ITask> {
        default boolean tickTask(T task) {
            task.tick();
            return task.isDone();
        }
    }
}
