package cn.edu.thss.iise.xiaohan.bpcd.mtree.index;

public class Timer {

	public static class Times {
		public long real;

		Times(long real) {
			this.real = real;
		}
	};

	private long timeBegin;

	public Timer() {
		this.timeBegin = System.currentTimeMillis();
	}

	public Times getTimes() {
		long timeEnd = System.currentTimeMillis();
		return new Times(timeEnd - timeBegin);
	}
}
