/**
 * BeehiveZ is a business process model and instance management system.
 * Copyright (C) 2011  
 * Institute of Information System and Engineering, School of Software, Tsinghua University,
 * Beijing, China
 *
 * Contact: wangwenxingbuaa@gmail.com 
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
package cn.edu.thss.iise.beehivez.server.metric.tar;

import java.util.HashMap;
import java.util.HashSet;

import org.processmining.framework.models.petrinet.PetriNet;

/**
 * @author Wenxing Wang
 * 
 * @date Mar 27, 2011
 * 
 */
public class ComputeSimilarity {
	private ExtensiveTAR _tar1 = null;
	private ExtensiveTAR _tar2 = null;

	private double finalSimilarity = 0;
	private double tarSimilarity = 0;
	private double tar0Similarity = 0;
	private double tarReSimilarity = 0;
	private double tarImSimilarity = 0;

	public ComputeSimilarity(PetriNet pn1, PetriNet pn2) {
		_tar1 = new ExtensiveTAR(pn1);
		_tar2 = new ExtensiveTAR(pn2);
	}

	public double compute() {
		HashMap<String, HashSet<String>> allTARS = new HashMap<String, HashSet<String>>();
		double same;
		double all;

		// for tar
		double tarCoefficient = 0;
		HashMap<String, HashSet<String>> allSameTar = new HashMap<String, HashSet<String>>(
				_tar1._tar);
		HashMap<String, HashSet<String>> allTar = new HashMap<String, HashSet<String>>(
				_tar2._tar);
		for (String label : _tar1._tar.keySet()) {
			if (_tar2._tar.keySet().contains(label)) {
				allSameTar.get(label).retainAll(_tar2._tar.get(label));
				allTar.get(label).addAll(_tar1._tar.get(label));
			} else {
				allSameTar.remove(label);
				allTar.put(label, _tar1._tar.get(label));
			}
		}

		same = 0;
		all = 0;
		for (String label : allSameTar.keySet()) {
			same += allSameTar.get(label).size();
		}
		for (String label : allTar.keySet()) {
			all += allTar.get(label).size();
		}
		if (all == 0) {
			tarSimilarity = 0;
		} else {
			tarSimilarity = same / all;
		}
		tarCoefficient = all;

		// for tar0
		double tar0Coefficient = 0;
		HashMap<String, HashSet<String>> allSameTar0 = new HashMap<String, HashSet<String>>(
				_tar1._tar0);
		HashMap<String, HashSet<String>> allTar0 = new HashMap<String, HashSet<String>>(
				_tar2._tar0);
		for (String label : _tar1._tar0.keySet()) {
			if (_tar2._tar0.keySet().contains(label)) {
				allSameTar0.get(label).retainAll(_tar2._tar0.get(label));
				allTar0.get(label).addAll(_tar1._tar0.get(label));
			} else {
				allSameTar0.remove(label);
				allTar0.put(label, _tar1._tar0.get(label));
			}
		}

		same = 0;
		all = 0;
		for (String label : allSameTar0.keySet()) {
			same += allSameTar0.get(label).size();
		}
		for (String label : allTar0.keySet()) {
			all += allTar0.get(label).size();
		}
		if (all == 0) {
			tar0Similarity = 0;
		} else {
			tar0Similarity = same / all;
		}
		tar0Coefficient = all;

		// for tar'
		double tarReCoefficient = 0;
		HashMap<String, HashSet<String>> allSameTarRe = new HashMap<String, HashSet<String>>(
				_tar1._tarRe);
		HashMap<String, HashSet<String>> allTarRe = new HashMap<String, HashSet<String>>(
				_tar2._tarRe);
		for (String label : _tar1._tarRe.keySet()) {
			if (_tar2._tarRe.keySet().contains(label)) {
				allSameTarRe.get(label).retainAll(_tar2._tarRe.get(label));
				allTarRe.get(label).addAll(_tar1._tarRe.get(label));
			} else {
				allSameTarRe.remove(label);
				allTarRe.put(label, _tar1._tarRe.get(label));
			}
		}

		same = 0;
		all = 0;
		for (String label : allSameTarRe.keySet()) {
			same += allSameTarRe.get(label).size();
		}
		for (String label : allTarRe.keySet()) {
			all += allTarRe.get(label).size();
		}
		if (all == 0) {
			tarReSimilarity = 0;
		} else {
			tarReSimilarity = same / all;
		}
		tarReCoefficient = all;

		// for tar+
		double tarImCoefficient = 0;
		HashMap<String, HashSet<String>> allSameTarIm = new HashMap<String, HashSet<String>>(
				_tar1._tarIm);
		HashMap<String, HashSet<String>> allTarIm = new HashMap<String, HashSet<String>>(
				_tar2._tarIm);
		for (String label : _tar1._tarIm.keySet()) {
			if (_tar2._tarIm.keySet().contains(label)) {
				allSameTarIm.get(label).retainAll(_tar2._tarIm.get(label));
				allTarIm.get(label).addAll(_tar1._tarIm.get(label));
			} else {
				allSameTarIm.remove(label);
				allTarIm.put(label, _tar1._tarIm.get(label));
			}
		}

		same = 0;
		all = 0;
		for (String label : allSameTarIm.keySet()) {
			same += allSameTarIm.get(label).size();
		}
		for (String label : allTarIm.keySet()) {
			all += allTarIm.get(label).size();
		}
		if (all == 0) {
			tarImSimilarity = 0;
		} else {
			tarImSimilarity = same / all;
		}
		tarImCoefficient = all;

		all = 0;
		all = tarCoefficient + tar0Coefficient + tarReCoefficient
				+ tarImCoefficient;
		if (all == 0) {
			return 0;
		}

		tarCoefficient /= all;
		tar0Coefficient /= all;
		tarReCoefficient /= all;
		tarImCoefficient /= all;

		finalSimilarity = tarCoefficient * tarSimilarity + tar0Coefficient
				* tar0Similarity + tarReCoefficient * tarReSimilarity
				+ tarImCoefficient * tarImSimilarity;

		return finalSimilarity;
	}
}
