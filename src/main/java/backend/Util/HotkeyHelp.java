package main.java.backend.Util;

import java.util.ArrayList;

public class HotkeyHelp {
	
	String f1,f2 ,f3, f4, f11, f12, tab,up,down,left,right,ctrlz;
	ArrayList<String> help;
	ArrayList<String> name;
	
	public HotkeyHelp() {
		f1 = "Press F1 to toggle help";
		f2 = "Press 'F2' to get list of existing categories.";
		f3 = "Press 'F3' to get Today's Tasks.";
		f4 = "Press 'F4' to get Today's Events";
		f11 = "Press 'F11' see completed Tasks, Events & Floats";
		f12 = "Press 'F12' to exit program";
		tab = "Press 'Tab' to toggle between default and focus view";
		up = "Use 'up' arrow to navigate list in focus view";
		down = "Use 'down' arrow to navigate list in focus view";
		left = "Use 'left' arrow to change list in focus view";
		right = "Use 'right' arrow to change list in focus view";
		ctrlz = "Use 'Ctrl'+'Z' to undo";
		
		addAll();
		resourceList();
	}
	
	private void resourceList() {
		name = new ArrayList<String>();
		name.add("Resources/f1.png");
		name.add("Resources/f2.png");
		name.add("Resources/f3.png");
		name.add("Resources/f4.png");
		name.add("Resources/f11.png");
		name.add("Resources/f12.png");
		name.add("Resources/tab.png");
		name.add("Resources/up.png");
		name.add("Resources/down.png");
		name.add("Resources/left.png");
		name.add("Resources/right.png");
		name.add("Resources/ctrl.png");
		name.add("Resources/z.png");
		
	}

	private void addAll() {
		help = new ArrayList<String>();
		help.add(f1);
		help.add(f2);
		help.add(f3);
		help.add(f4);
		help.add(f11);
		help.add(f12);
		help.add(tab);
		help.add(up);
		help.add(down);
		help.add(left);
		help.add(right);
		help.add(ctrlz);
	}
	
	public ArrayList<String> retrieveHotkey() {
		return help;
	}
	public ArrayList<String> getResourceList(){
		return name;
	}
}
