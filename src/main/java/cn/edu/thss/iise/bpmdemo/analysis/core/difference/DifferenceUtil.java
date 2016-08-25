package cn.edu.thss.iise.bpmdemo.analysis.core.difference;

import java.io.IOException;
import java.util.List;

public class DifferenceUtil {

	// ��������bpmn�ļ������ز��컯�����������
	public static List<String> difference(String file1, String file2)
			throws IOException {
		return HighLevelOP.dif(file1, file2);
	}

}
