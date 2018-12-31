package me.jumper251.replay.utils.fetcher;



public abstract class Acceptor<T> implements Runnable{

	private Consumer<T> consumer;
	
	public Acceptor(Consumer<T> consumer) {
		this.consumer = consumer;
	}
	
	public abstract T getValue();
	
	@Override
	public void run() {
		consumer.accept(getValue());
	}
}
