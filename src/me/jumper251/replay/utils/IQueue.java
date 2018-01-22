package me.jumper251.replay.utils;


public interface IQueue<ElementType> {

	boolean isEmpty();
	
	void enqueue(ElementType element);
	
	ElementType dequeue();
	
	ElementType front();
}
