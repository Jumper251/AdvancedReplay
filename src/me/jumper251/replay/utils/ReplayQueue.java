package me.jumper251.replay.utils;


public class ReplayQueue<ElementType> implements IQueue<ElementType> {
	
	
	private QueueNode head;
	private QueueNode tail;
	
	@Override
	public boolean isEmpty() {
		return this.head == null;
	}

	@Override
	public void enqueue(ElementType element) {
		if (element == null) return;
		
		final QueueNode node = new QueueNode(element);
		if (this.isEmpty()) {
			this.head = node;
		} else {
			this.tail.setNextNode(node);
		}
		
		this.tail = node;
		
	}

	@Override
	public ElementType dequeue() {
		if (this.isEmpty()) return null;
		
		QueueNode node = this.head;
		
		this.head = this.head.getNextNode();
		
		if (this.isEmpty()) {
			this.head = null;
			this.tail = null;
		}
		
		return node.getElement();
	}

	@Override
	public ElementType front() {
		return this.isEmpty() ? null : this.head.getElement();
	}
	
	private class QueueNode {
		
		
		private final ElementType element;
        private QueueNode nextNode = null;
        
        
        private QueueNode(ElementType element) {
            this.element = element;
        }
        
        
        private void setNextNode(QueueNode nextNode) {
            this.nextNode = nextNode;
        }
        
        
        private QueueNode getNextNode() {
            return this.nextNode;
        }
        
        
        private ElementType getElement() {
            return this.element;
        }
        
	}

}
