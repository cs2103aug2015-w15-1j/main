package main.java.backend.Util;

import java.util.ArrayList;

public class hotkeyHelp {
	String f2,f3,f4,f11,f12,tab;
	ArrayList<String> help;
	public hotkeyHelp(){
		f2 = "Press F2 to get list of existing categories.";
		f3 = "Press F3 to get Today's Tasks.";
		f4 = "Press F4 to get Today's Events";
		f11 = "Press F11 see completed Tasks, Events & Floats";
		f12 = "Press F12 to exit program";
		tab = "Press Tab to change view";
		addAll();
	}
	
	private void addAll(){
		help = new ArrayList<String>();
		help.add(f2);
		help.add(f3);
		help.add(f4);
		help.add(f11);
		help.add(f12);
		help.add(tab);
	}
	
	public ArrayList<String> retrieveHotkey(){
		return help;
	}
}
