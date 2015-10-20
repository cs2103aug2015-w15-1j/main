package main.java.backend.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CommandHelp {
	
	ArrayList<String> list;
	ArrayList<ArrayList<String>> fullList;
	ArrayList<String> names;
	
	public CommandHelp() throws FileNotFoundException{
		Scanner s = new Scanner(new File("src/main/java/backend/Parser/helplist.txt"));
		list = new ArrayList<String>();
		fullList = new ArrayList<ArrayList<String>>();
		names = new ArrayList<String>();
		while (s.hasNextLine()){
		    list.add(s.nextLine());
		}
		s.close();
		split();
		listNames();
	}
	
	private void split(){
		ArrayList<String> addDelete = new ArrayList<String>();
		ArrayList<String> doneUndone= new ArrayList<String>();
		ArrayList<String> undoSearch= new ArrayList<String>();
		ArrayList<String> renameEvent= new ArrayList<String>();
		ArrayList<String> desCate= new ArrayList<String>();
		ArrayList<String> showSort= new ArrayList<String>();
		ArrayList<String> everyExit= new ArrayList<String>();
		ArrayList<String> oneShot= new ArrayList<String>();
		int index =0;
			while(!list.get(index).equals("break")){
			addDelete.add(list.get(index));
			index++;
			}
			fullList.add(addDelete);
			index++;
			while(!list.get(index).equals("break")){
				doneUndone.add(list.get(index));
				index++;
			}
			fullList.add(doneUndone);
			index++;
			while(!list.get(index).equals("break")){
				undoSearch.add(list.get(index));
				index++;
			}
			fullList.add(undoSearch);
			index++;
			while(!list.get(index).equals("break")){
				renameEvent.add(list.get(index));
				index++;
			}
			fullList.add(renameEvent);
			index++;
			while(!list.get(index).equals("break")){
				desCate.add(list.get(index));
				index++;
			}
			fullList.add(desCate);
			index++;
			while(!list.get(index).equals("break")){
				showSort.add(list.get(index));
				index++;
			}
			fullList.add(showSort);
			index++;
			while(!list.get(index).equals("break")){
				everyExit.add(list.get(index));
				index++;
			}
			fullList.add(everyExit);
			index++;
			while(!list.get(index).equals("break")){
				oneShot.add(list.get(index));
				index++;
			}
			fullList.add(oneShot);
	}
	
	private void listNames(){
		names = new ArrayList<String>();
		names.add("");
		names.add("adding/deleteing");
		names.add("done/undone");
		names.add("Undo/Redo/Search");
		names.add("Rename task/ Edit deadline & event dates");
		names.add("Edit description & priority & reminders & category");
		names.add("Show/ Sort");
		names.add("Recurring/ Reset/ exit");
		names.add("Advance features: One Shot");
		names.add("");
	}

	public ArrayList<String> getCommandList(){
		return list;
	}
	public ArrayList<ArrayList<String>> getSplitList(){
		return fullList;
	}
	public ArrayList<String> getSplitNaming(){
		return names;
	}
}
