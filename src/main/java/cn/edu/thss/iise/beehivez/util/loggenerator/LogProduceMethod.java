/**
 * BeehiveZ is a business process model and instance management system.
 * Copyright (C) 2011  
 * Institute of Information System and Engineering, School of Software, Tsinghua University,
 * Beijing, China
 *
 * Contact: jintao05@gmail.com 
 *
 * This program is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation with the version of 2.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package cn.edu.thss.iise.beehivez.util.loggenerator;

import org.processmining.framework.models.petrinet.PetriNet;

/**
 * super class of all log produce algorithm
 * 
 * @author zhp,htf
 * 
 */
public abstract class LogProduceMethod {
	protected LogIO logIO = null;

	public LogProduceMethod() {
		logIO = new LogIO();
	}

	/**
	 * 
	 * @param fileDir
	 *            where the log info store
	 * @param caseCount
	 *            how many cases will be executed
	 * @param pn
	 */
	public abstract void generateLog(String fileDir, int caseCount,
			PetriNet pn, double completeness, int multiple);

	public abstract void generateLog(String fileDir, int caseCount,
			PetriNet pn, double completeness);

	public abstract void generateLog(String fileDir, PetriNet pn);

	public abstract void generateLog(String fileDir, PetriNet pn,
			CompleteParameters comPara, NoiseParameters noiPara);

	public abstract String getLogType();
}
