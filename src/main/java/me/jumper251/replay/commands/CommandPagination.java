package me.jumper251.replay.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandPagination<T> {

	private List<T> content;
	
	private int elementsPerPage;
	
	public CommandPagination(List<T> content, int elementsPerPage) {
		this.content = content;
		this.elementsPerPage = elementsPerPage;
	}
	
	
	public void printPage(int page, IPaginationExecutor<T> executor) {
		for (T element : getElementsFor(page)) {
			executor.print(element);
		}
	}
	
	public List<T> getElementsFor(int page) {
		if (page <= 0 || page > getPages()) return new ArrayList<T>();
		
		int startIndex = (page - 1) * this.elementsPerPage;
		int endIndex = page * this.elementsPerPage;
		
		return this.content.subList(startIndex, endIndex <= this.content.size() ? endIndex : this.content.size());
	}
	
	public int getPages() {
		return (int) Math.ceil((content.size() / (double) elementsPerPage));
	}
	
	public void addElement(T element) {
		if (!this.content.contains(element)) this.content.add(element);
	}
	
	public int getElementsPerPage() {
		return elementsPerPage;
	}
	
	
}
