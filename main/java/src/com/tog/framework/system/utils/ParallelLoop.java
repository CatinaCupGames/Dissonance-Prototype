package com.tog.framework.system.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelLoop<E> {
	
	private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
	
	private int threads;
	
	public ParallelLoop() {
		this(MAX_THREADS);
	}
	
	public ParallelLoop(int threads) {
		this.threads = threads;
	}
	
	public void loop(List<E> array, final ParrallelRunner<E> caller) {
		ExecutorService exe = Executors.newFixedThreadPool(threads);
		try {
			for (final E o : array) {
				exe.submit(new Runnable() {
					@Override
					public void run() {
						caller.run(o);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			exe.shutdown();
		}
	}
	
	public static <E> void parallelLoop(List<E> array, final ParrallelRunner<E> caller) {
		ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
		try {
			for (final E o : array) {
				exe.submit(new Runnable() {
					@Override
					public void run() {
						caller.run(o);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			exe.shutdown();
		}
	}
	
	public interface ParrallelRunner<E> {
		public void run(E object);
	}
}