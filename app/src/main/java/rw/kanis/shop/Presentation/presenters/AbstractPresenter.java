package rw.kanis.shop.Presentation.presenters;

import rw.kanis.shop.domain.executor.Executor;
import rw.kanis.shop.domain.executor.MainThread;

public abstract class AbstractPresenter {
    protected Executor mExecutor;
    protected MainThread mMainThread;

    public AbstractPresenter(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }
}
