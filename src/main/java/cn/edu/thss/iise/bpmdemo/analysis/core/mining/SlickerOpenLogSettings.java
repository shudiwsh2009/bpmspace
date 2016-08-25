package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import org.processmining.framework.log.LogFile;
import org.processmining.framework.log.LogFilter;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.framework.log.LogSummary;
import org.processmining.framework.log.filter.DefaultLogFilter;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.framework.ui.OpenLogSettings;
import org.processmining.framework.ui.slicker.logdialog.SlickerAdvancedLogFilterConfiguration;

public class SlickerOpenLogSettings extends OpenLogSettings {

	private static final long serialVersionUID = 4258730373866637365L;

	protected enum ActiveFilter {
		INITIAL, SIMPLE, ADVANCED;
	}

	protected ActiveFilter activeFilter = ActiveFilter.INITIAL;
	protected SlickerSimpleFilterSettings simpleFilterSettings;
	protected SlickerAdvancedLogFilterConfiguration advancedFilterSettings;

	// data attributes
	protected LogFile logFile;
	protected LogReader log;
	protected LogReader currentLog;
	protected LogFilter currentFilter;
	protected LogSummary summary;

	/**
	 * Creates a new open log settings with slicker look
	 * 
	 * @param logFile
	 *            Log file to wrap around
	 */
	public SlickerOpenLogSettings(LogFile logFile) {
		super(logFile);
		this.logFile = logFile;
		loadLogFromFile();
		simpleFilterSettings = new SlickerSimpleFilterSettings(log);
		currentFilter = simpleFilterSettings.getLogFilter();
	}

	protected void loadLogFromFile() {

		// load log file asynchronously in helper thread
		Throwable exception = null;
		try {
			log = LogReaderFactory.createInstance(new DefaultLogFilter(
					DefaultLogFilter.INCLUDE), logFile);
			currentLog = log;
			summary = log.getLogSummary();
		} catch (Throwable e) {
			// this usually signals that something has gone wrong while
			// reading the log;
			// we expect a malformed or otherwise erroneous input file,
			// so we abort here
			// and inform the user later (set flag).
			log = null;
			exception = e;
			e.printStackTrace();
		}

		if (exception == null) {
			logReadingFinished();
		} else {
			errorReadingLog(exception);
		}
	}

	protected void errorReadingLog(Throwable e) {
	}

	protected void logReadingFinished() {
	}

	public LogReader getLog() {
		try {
			if (this.currentLog == null) {
				// no filtered log set
				if (this.currentFilter == null) {
					// no filter set, use original
					this.currentLog = this.log;
				} else {
					// filter original
					this.currentLog = LogReaderFactory.createInstance(
							this.currentFilter, this.log);
				}
			} else {
				// check if current log has the current filter, otherwise create
				// new filtered logs
				if (this.currentFilter != null
						&& this.currentLog.getLogFilter().equals(
								this.currentFilter) == false) {
					this.currentFilter.setLowLevelFilter(this.log
							.getLogFilter());
					this.currentLog = LogReaderFactory.createInstance(
							this.currentFilter, this.log);
				}
			}
			// safety check: is the log empty
			if (this.currentLog.numberOfInstances() == 0) {
				this.currentLog = log;
			}
			return this.currentLog;
		} catch (Throwable e) {
			// oops
			e.printStackTrace();
			return null;
		}
	}

	public String getActiveLogFilterName() {
		if (this.activeFilter == ActiveFilter.INITIAL
				|| this.currentFilter == null) {
			return "unfiltered log";
		} else if (this.activeFilter == ActiveFilter.SIMPLE) {
			return "simple log filter";
		} else if (this.activeFilter == ActiveFilter.ADVANCED) {
			return "advanced log filter";
		} else {
			return "unknown";
		}
	}

	public LogReader getOriginalLog() {
		return this.log;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.processmining.framework.ui.OpenLogSettings#getFile()
	 */
	@Override
	public LogFile getFile() {
		return this.logFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.processmining.framework.ui.OpenLogSettings#getLogFilter()
	 */
	@Override
	public LogFilter getLogFilter() {
		return this.currentFilter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.processmining.framework.ui.OpenLogSettings#getLogSummary()
	 */
	@Override
	public LogSummary getLogSummary() {
		return this.summary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.processmining.framework.ui.OpenLogSettings#getSelectedLogReader()
	 */
	@Override
	public LogReader getSelectedLogReader() {
		return this.getLog();
	}

	@Override
	public ProvidedObject[] getProvidedObjects() {
		// TODO Auto-generated method stub
		return null;
	}

}
