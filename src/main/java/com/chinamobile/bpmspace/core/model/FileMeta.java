package com.chinamobile.bpmspace.core.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

//ignore "bytes" when return json format
@JsonIgnoreProperties({ "bytes" })
public class FileMeta {
	private String fileName = null;
	private String fileSize = null;
	private String fileType = null;
	private byte[] bytes = null;
	private String inputFilePath = null;
	private String outputFileName = null;
	private String fileId = null;
	private String miningEvaluateData_modelIn = null;
	private String miningEvaluateData_logOut = null;
	private String miningEvaluateData_modelOut = null;
	private String miningEvaluateData_result = null;
	private boolean valid = true;

	public void setClean() {
		this.fileName = null;
		this.fileSize = null;
		this.fileType = null;
		this.inputFilePath = null;
		this.outputFileName = null;
		this.fileId = null;
		for (int i = 0; i < this.bytes.length; i++)
			this.bytes[i] = 0;
	}

	// set invalid
	public void setInvalid() {
		this.valid = false;
	}

	// get valid
	public boolean getValid() {
		return valid;
	}

	// miningEvaluateData_modelOut
	public String getMiningEvaluateData_modelOut() {
		return miningEvaluateData_modelOut;
	}

	public void setMiningEvaluateData_modelOut(
			String miningEvaluateData_modelOut) {
		this.miningEvaluateData_modelOut = miningEvaluateData_modelOut;
	}

	// miningEvaluateData_result
	public String getMiningEvaluateData_result() {
		return miningEvaluateData_result;
	}

	public void setMiningEvaluateData_result(String miningEvaluateData_result) {
		this.miningEvaluateData_result = miningEvaluateData_result;
	}

	// miningEvaluateData_logOut
	public String getMiningEvaluateData_logOut() {
		return miningEvaluateData_logOut;
	}

	public void setMiningEvaluateData_logOut(String miningEvaluateData_logOut) {
		this.miningEvaluateData_logOut = miningEvaluateData_logOut;
	}

	// miningEvaluateData_modelIn
	public String getMiningEvaluateData_modelIn() {
		return miningEvaluateData_modelIn;
	}

	public void setMiningEvaluateData_modelIn(String miningEvaluateData_modelIn) {
		this.miningEvaluateData_modelIn = miningEvaluateData_modelIn;
	}

	// fileId
	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String _logId) {
		this.fileId = _logId;
	}

	// inputFilePath
	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	// outputFileName
	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	// fileName
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// fileSize
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	// fileType
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	// bytes
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}