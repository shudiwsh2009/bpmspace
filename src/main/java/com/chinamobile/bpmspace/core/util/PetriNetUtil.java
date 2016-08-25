package com.chinamobile.bpmspace.core.util;

import org.processmining.framework.log.LogEvent;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Transition;
import org.processmining.framework.ui.ComboBoxLogEvent;

public class PetriNetUtil {

	public static void makeVisible(PetriNet pn) {
		for (Transition t : pn.getTransitions()) {
			if (t.isInvisibleTask()
					&& (!t.getIdentifier().equals("") || t.getIdentifier()
							.startsWith("oryx_"))) {
				LogEvent event = new LogEvent(ComboBoxLogEvent.NONE,
						ComboBoxLogEvent.NONE);
				t.setLogEvent(event);
			}
		}
	}
}
