package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.util.ArrayList;

public class Trace {

	private String id;
	private ArrayList<Event> events;
	private ArrayList<String> tasksPerTrace; // ��¼ÿ��������������
	private ArrayList<String> qxbTasks = new ArrayList<String>(); // ��¼trace�����Ȱ�ģʽ����
	private ArrayList<String> conTasks = new ArrayList<String>(); // ��¼trace�л�ǩ��������ģʽ����
	private ArrayList<String> recTasks = new ArrayList<String>(); // ��¼trace�л�ǩ���ݹ飩ģʽ����
	private ArrayList<String> mcTasks = new ArrayList<String>(); // ��¼trace������ģʽ����

	public Trace() {
		tasksPerTrace = new ArrayList<String>();
	}

	public Trace(String newId, ArrayList<Event> newEvents,
			ArrayList<String> newTasksPerTrace) {
		this.id = newId;
		this.events = newEvents;
		this.tasksPerTrace = newTasksPerTrace;
	}

	public void setId(String newId) {
		this.id = newId;
	}

	public void setEvents(ArrayList<Event> newEvents) {
		this.events = newEvents;
	}

	public void setTasksPerTrace(ArrayList<String> newTasksPerTrace) {
		this.tasksPerTrace = newTasksPerTrace;
	}

	public void setQxbTasks(ArrayList<String> newQxbTasks) {
		this.qxbTasks = newQxbTasks;
	}

	public void setConTasks(ArrayList<String> newConTasks) {
		this.conTasks = newConTasks;
	}

	public void setRecTasks(ArrayList<String> newRecTasks) {
		this.recTasks = newRecTasks;
	}

	public void setMcTasks(ArrayList<String> newMcTasks) {
		this.mcTasks = newMcTasks;
	}

	public String getId() {
		return this.id;
	}

	public ArrayList<Event> getEvents() {
		return this.events;
	}

	public ArrayList<String> getTasksPerTrace() {
		return this.tasksPerTrace;
	}

	public ArrayList<String> getQxbTasks() {
		return this.qxbTasks;
	}

	public ArrayList<String> getConTasks() {
		return this.conTasks;
	}

	public ArrayList<String> getRecTasks() {
		return this.recTasks;
	}

	public ArrayList<String> getMcTasks() {
		return this.mcTasks;
	}

	/*
	 * public void addTask(String task){ this.tasksPerTrace.add(task); }
	 */

}
