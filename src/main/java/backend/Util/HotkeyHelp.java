package main.java.backend.Util;

import java.util.ArrayList;

//@@author A0126125R
public class HotkeyHelp {

	String f1, f2, f3, f4, esc, tab, up, down, left, right, ctrlz, ctrly, shiftup, shiftdown;
	ArrayList<String> help;
	ArrayList<String> name;

	public HotkeyHelp() {
		f1 = "Press 'F1' to toggle help";
		f2 = "Press 'F2' to toggle between floating and overdue tasks (if any)";
		f3 = "Press 'F3' to get Today's Tasks & Events.";
		f4 = "Press 'F4' see completed Tasks, Events & Floats";
		esc = "Press 'esc' to exit program";
		tab = "Press 'Tab' to toggle between default and focus view";
		up = "Use 'up' arrow to navigate list in focus view";
		down = "Use 'down' arrow to navigate list in focus view";
		left = "Use 'left' arrow to change list in focus view";
		right = "Use 'right' arrow to change list in focus view";
		ctrlz = "Use 'ctrl'+'Z' to undo";
		ctrly = "use 'ctrl'+'Y' to redo";
		shiftup = "use 'shift' + 'up' arrow to get recent commands";
		shiftdown = "use 'shift' + 'down' arrow to get recent commands";

		addAll();
		resourceList();
	}

	private void resourceList() {
		name = new ArrayList<String>();
		name.add("Resources/f1.png");
		name.add("Resources/f2.png");
		name.add("Resources/f3.png");
		name.add("Resources/f4.png");
		name.add("Resources/esc.png");
		name.add("Resources/tab.png");
		name.add("Resources/up.png");
		name.add("Resources/down.png");
		name.add("Resources/left.png");
		name.add("Resources/right.png");
		name.add("Resources/ctrl.png");
		name.add("Resources/z.png");
		name.add("Resources/ctrl.png");
		name.add("Resources/y.png");
		name.add("Resources/shift.png");
		name.add("Resources/up.png");
		name.add("Resources/shift.png");
		name.add("Resources/down.png");
	}

	private void addAll() {
		help = new ArrayList<String>();
		help.add(f1);
		help.add(f2);
		help.add(f3);
		help.add(f4);
		help.add(esc);
		help.add(tab);
		help.add(up);
		help.add(down);
		help.add(left);
		help.add(right);
		help.add(ctrlz);
		help.add(ctrly);
		help.add(shiftup);
		help.add(shiftdown);
	}

	public ArrayList<String> retrieveHotkey() {
		return help;
	}

	public ArrayList<String> getResourceList() {
		return name;
	}
}
