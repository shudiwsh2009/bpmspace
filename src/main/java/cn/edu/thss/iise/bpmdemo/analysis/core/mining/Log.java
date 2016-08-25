package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Log {

	private ArrayList<Trace> traces;
	private ArrayList<String> tasks;
	private ArrayList<String> performers;

	// private static int i=0;

	public Log(String filePath) {
		tasks = new ArrayList<String>();
		performers = new ArrayList<String>();
		traces = readFile(filePath);
	}

	public void setTraces(ArrayList<Trace> newTraces) {
		this.traces = newTraces;
	}

	public ArrayList<Trace> getTraces() {
		return this.traces;
	}

	public ArrayList<String> getTasks() {
		return this.tasks;
	}

	public ArrayList<String> getPerformers() {
		return this.performers;
	}

	public ArrayList<Trace> readFile(String filePath) {
		/*
		 * String suffix = ""; if ((filePath != null) && (filePath.length() >
		 * 0)) { int dot = filePath.lastIndexOf('.'); if ((dot > -1) && (dot <
		 * (filePath.length() - 1))) { suffix = filePath.substring(dot + 1); } }
		 */

		traces = new ArrayList<Trace>();
		if (filePath.endsWith("xlsx")) {
			traces = loadXlsx(filePath);
		} else if (filePath.endsWith("xls")) {
			traces = loadXls(filePath);
		}
		return traces;
	}

	/**
	 * 读取excel07
	 * 
	 * @param filePath
	 * @return
	 */
	public ArrayList<Trace> loadXlsx(String filePath) {
		XSSFWorkbook workbook = null; // XSSFWorkbook表示以xlsx为后缀名的文件
		XSSFSheet sheet = null;
		ArrayList<Trace> traces = new ArrayList<Trace>();
		try {
			workbook = new XSSFWorkbook(new FileInputStream(filePath));
			sheet = workbook.getSheetAt(0);
			traces = resolvingExcelSheet(sheet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return traces;
	}

	/**
	 * 读取excel03
	 * 
	 * @param filePath
	 * @return
	 */
	public ArrayList<Trace> loadXls(String filePath) {
		HSSFWorkbook hssfWorkbook = null;
		ArrayList<Trace> traces = new ArrayList<Trace>();
		ArrayList<Event> events = new ArrayList<Event>();
		ArrayList<String> tasksPerTrace = new ArrayList<String>();
		String traceID = ""; // 文档id
		String temp = "";
		// String number = "0"; // 序号
		Trace trace = new Trace();
		Event event;
		try {
			hssfWorkbook = new HSSFWorkbook(new FileInputStream(filePath));
			// 循环工作表Sheet
			for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}

				HSSFRow firstRow = hssfSheet.getRow(0); // 第一行是标题 1.文档id 2.处理环节
														// 3.处理人...
				// 循环行Row
				for (int rowNum = 1; rowNum < hssfSheet
						.getPhysicalNumberOfRows(); rowNum++) { // i=0
																// 第0行是各列标题，如"文档id"等；
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow == null) {
						continue;
					}
					temp = hssfRow.getCell(1).toString(); // 得到traceID
					if (traceID != "" && !temp.equals(traceID)) {
						trace = new Trace(traceID,
								new ArrayList<Event>(events),
								new ArrayList<String>(tasksPerTrace));
						traces.add(trace);
						events.clear();
					}
					traceID = hssfRow.getCell(1).toString();
					event = resolvingExcelHSSFRow(firstRow, hssfRow,
							tasksPerTrace);
					events.add(event);
				}
				trace = new Trace(traceID, new ArrayList<Event>(events),
						new ArrayList<String>(tasksPerTrace));
				traces.add(trace);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return traces;
	}

	/**
	 * 将HSSFRow解析成String类型数组(返回一个event)
	 */
	public Event resolvingExcelHSSFRow(HSSFRow firstRow, HSSFRow row,
			ArrayList<String> tasksPerTrace) {
		Event event = new Event();
		int numberOfColumns = firstRow.getPhysicalNumberOfCells(); // 由于有些行有空格，采用第一行计算列数
		String columnName = "";
		HSSFCell cell = null;

		for (int i = 2; i < numberOfColumns; i++) { // 0.序号;1.文档id
			columnName = firstRow.getCell(i).getStringCellValue();
			cell = row.getCell(i);
			if (cell != null) {
				if (columnName.equals("处理环节")) {
					if (!tasks.contains(getHSSFCellValue(cell))) {
						tasks.add(getHSSFCellValue(cell));
					}
					if (tasksPerTrace == null
							|| !tasksPerTrace.contains(getHSSFCellValue(cell))) {
						tasksPerTrace.add(getHSSFCellValue(cell));
						// trace.addTask(getHSSFCellValue(cell));
					}
					event.setIdentifier(getHSSFCellValue(cell));
				} else if (columnName.equals("处理人")) {
					if (!performers.contains(getHSSFCellValue(cell))) {
						performers.add(getHSSFCellValue(cell));
					}
					event.setPerformer(getHSSFCellValue(cell));
				} else if (columnName.equals("到达时间")) {
					event.setBeginTime(getHSSFCellValue(cell));
				} else if (columnName.equals("发出时间")) {
					event.setEndTime(getHSSFCellValue(cell));
				} else if (columnName.equals("提交路径")) {
					event.setRoute(getHSSFCellValue(cell));
				} else if (columnName.equals("后续处理环节")) {
					event.setNextTask(getHSSFCellValue(cell));
				}
			}
		}
		return event;
	}

	/**
	 * 获取HSSFcell值
	 */
	public String getHSSFCellValue(HSSFCell cell) {
		String cellValue = "";
		if ((cell.getCellType()) == HSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				SimpleDateFormat sFormat = new SimpleDateFormat(
						"yyyy-M-d H:mm", Locale.CHINA);
				cellValue = sFormat.format(date);
			} else {
				cellValue = cell.getNumericCellValue() + "";
			}
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue();
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			cellValue = cell.getBooleanCellValue() + "";
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
			cellValue = cell.getCellFormula() + "";
		}
		return cellValue;
	}

	/**
	 * 从excel读取所有的trace
	 * 
	 * @param sheet
	 * @return
	 */
	public ArrayList<Trace> resolvingExcelSheet(XSSFSheet sheet) {
		ArrayList<Trace> traces = new ArrayList<Trace>();
		ArrayList<Event> events = new ArrayList<Event>();
		ArrayList<String> tasksPerTrace = new ArrayList<String>();
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		XSSFRow firstRow = sheet.getRow(0);
		XSSFRow row = null;
		String traceID = "";
		String temp = "";
		Trace trace = new Trace();
		Event event;

		if (sheet != null) {
			for (int i = 1; i < numberOfRows; i++) { // i=0 第0行是各列标题，如"文档id"等；
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				temp = row.getCell(0).toString(); // 得到traceID

				if (temp == "") { // 是空行
					trace = new Trace(traceID, new ArrayList<Event>(events),
							new ArrayList<String>(tasksPerTrace));
					traces.add(trace);
					events.clear();
					continue;
				}
				traceID = temp;
				event = resolvingExcelXSSFRow(firstRow, row, tasksPerTrace);
				events.add(event);
			}
			trace = new Trace(traceID, new ArrayList<Event>(events),
					new ArrayList<String>(tasksPerTrace));
			traces.add(trace);
		}
		return traces;
	}

	/**
	 * 将XSSFRow解析成String类型数组(返回一个event)
	 */
	public Event resolvingExcelXSSFRow(XSSFRow firstRow, XSSFRow row,
			ArrayList<String> tasksPerTrace) {
		Event event = new Event();
		int numberOfColumns = row.getPhysicalNumberOfCells();
		String columnName = "";
		XSSFCell cell = null;

		for (int i = 1; i < numberOfColumns; i++) {
			columnName = firstRow.getCell(i).getStringCellValue();
			cell = row.getCell(i);
			if (cell != null) {
				if (columnName.equals("处理环节")) {
					event.setIdentifier(getXSSFCellValue(cell));
					if (!tasks.contains(getXSSFCellValue(cell))) {
						tasks.add(getXSSFCellValue(cell));
					}
					if (tasksPerTrace == null
							|| !tasksPerTrace.contains(getXSSFCellValue(cell))) {
						tasksPerTrace.add(getXSSFCellValue(cell));
					}
				} else if (columnName.equals("处理人")) {
					event.setPerformer(getXSSFCellValue(cell));
					if (!performers.contains(getXSSFCellValue(cell))) {
						performers.add(getXSSFCellValue(cell));
					}

				} else if (columnName.equals("到达时间")) {
					event.setBeginTime(getXSSFCellValue(cell));
				} else if (columnName.equals("发出时间")) {
					event.setEndTime(getXSSFCellValue(cell));
				} else if (columnName.equals("提交路径")) {
					event.setRoute(getXSSFCellValue(cell));
				} else if (columnName.equals("后续处理环节")) {
					event.setNextTask(getXSSFCellValue(cell));
				}
			}
		}

		return event;
	}

	/**
	 * 获取XSSFcell值
	 */
	public String getXSSFCellValue(XSSFCell cell) {
		String cellValue = "";
		if ((cell.getCellType()) == XSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-M-d H:mm");
				cellValue = sFormat.format(date);
			} else {
				cellValue = cell.getNumericCellValue() + "";
			}
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			cellValue = cell.getBooleanCellValue() + "";
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
			cellValue = cell.getCellFormula() + "";
		}
		return cellValue;
	}

}
