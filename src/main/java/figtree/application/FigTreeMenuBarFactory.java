/*
 * FigTreeMenuBarFactory.java
 *
 * Copyright (C) 2012 Andrew Rambaut
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
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

package figtree.application;

import jam.framework.DefaultEditMenuFactory;
import jam.framework.DefaultHelpMenuFactory;
import jam.framework.DefaultMenuBarFactory;
import jam.mac.MacEditMenuFactory;
import jam.mac.MacHelpMenuFactory;
import jam.mac.MacWindowMenuFactory;
import figtree.application.menus.FigTreeDefaultFileMenuFactory;
import figtree.application.menus.FigTreeMacFileMenuFactory;
import figtree.application.menus.TreeMenuFactory;

public class FigTreeMenuBarFactory extends DefaultMenuBarFactory {

	public FigTreeMenuBarFactory() {
		if (jam.mac.Utils.isMacOSX()) {
			registerMenuFactory(new FigTreeMacFileMenuFactory());
			registerMenuFactory(new MacEditMenuFactory());
			registerMenuFactory(new TreeMenuFactory());

			registerMenuFactory(new MacWindowMenuFactory());
			registerMenuFactory(new MacHelpMenuFactory());
		} else {
			registerMenuFactory(new FigTreeDefaultFileMenuFactory());
			registerMenuFactory(new DefaultEditMenuFactory());
			registerMenuFactory(new TreeMenuFactory());
			registerMenuFactory(new DefaultHelpMenuFactory());
		}
	}

}