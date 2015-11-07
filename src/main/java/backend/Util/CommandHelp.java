package main.java.backend.Util;

import java.util.ArrayList;

//@@author A0126125R
public class CommandHelp {
	
	ArrayList<ArrayList<String>> fullList;
	ArrayList<String> names;
	ArrayList<String> add;
	ArrayList<String> doneUndone;
	ArrayList<String> deleteRedo;
	ArrayList<String> showSort;
	ArrayList<String> everyExit;
	ArrayList<String> renameEvent;
	ArrayList<String> desCate;
	ArrayList<String> oneShot;
	

	public CommandHelp(){
		fullList = new ArrayList<ArrayList<String>>();
		names = new ArrayList<String>();
		add = new ArrayList<String>();
		doneUndone= new ArrayList<String>();
		deleteRedo= new ArrayList<String>();
		showSort= new ArrayList<String>();
		everyExit= new ArrayList<String>();
		renameEvent= new ArrayList<String>();
		desCate= new ArrayList<String>();
		oneShot= new ArrayList<String>();
		split();
		listNames();
	}
	
	private void split(){
		add.add("List of Command Keywords in TankTask:"
				+ "	\nAdding Tasks\n"
				+ "		\n	add"
				+ "		\n		Format: add [Task Name]"
				+ "		\n			Example: add Feed the fish"
				+ "\n"
				+ "		\n	Add ToDo"
				+ "		\n		Format: add [Task Name] by [Deadline]"
				+ "		\n			Example: add Feed the fish by today"
				+ "\n"
				+ "		\n	Add Event"
				+ "		\n		Format: add [Task Name] from [Start] to [End]"
				+ "		\n			Example: add Feed the fish from today 6pm to 6:15pm");
		doneUndone.add("List of Command Keywords in TankTask:"
				+ "	\nManaging your Tasks\n"
				+ "		\n	done"
				+ "		\n		Format 1: done [Task Index]"
				+ "		\n			Example: done 1"
				+ "		\n			"
				+ "		\n		Format 2: [Task Index] done"
				+ "		\n			Example: 1 done"
				+ "\n"
				+ "		\n	undone"
				+ "		\n		Format 1: undone [Task Index]"
				+ "		\n			Example: undone 1"
				+ "		\n			"
				+ "		\n		Format 2: [Task Index] undone"
				+ "		\n			Example: 1 undone");
		deleteRedo.add("List of Command Keywords in TankTask:"
				+ "	\nManaging your Tasks"
				+ "\n"
				+ "		\n	delete (Alternate form: del)"
				+ "		\n		Format 1: del [Task Index]"
				+ "		\n			Example: del 1"
				+ "		\n			"
				+ "		\n		Format 2: [Task Index] del"
				+ "		\n			Example: 1 del"
				+ "		\n			"
				+ "		\n	undo"
				+ "		\n		Format: undo"
				+ "\n"
				+ "		\n	redo"
				+ "		\n		Format: redo");
		showSort.add("List of Command Keywords in TankTask:"
				+ "\nManaging your Tasks "
				+ "\n"
				+ "		\n	search"
				+ "		\n		Format: search [Search Term(s)]"
				+ "		\n			Example: search fish"
				+ "\n"
				+ "		\n	show (Alternate form: showT, showE, showF, showO, showC, showD)"
				+ "		\n		Format 1: show [Task Type (Todo/Event/Floating/Overdue/Complete/Today)]"
				+ "		\n			Example: show event"
				+ "		\n			Example: showE"
				+ "	\n"
				+ "		\n		Format 2: show[X]"
				+ "		\n			Example: showE"
				+ "\n"
				+ "		\n	sort (Alternate form: sortD, sortN, sortP)"
				+ "		\n		Format 1: sort [Field (Deadline/Name/Priority)]"
				+ "		\n			Example: sort priority"
				+ "\n"
				+ "		\n		Format 2: sort[X]"
				+ "		\n			Example: sortP");
		everyExit.add("List of Command Keywords in TankTask:"
				+ "		\nManaging your Tasks"
				+ "\n"
				+ "		\n	every"
				+ "		\n		Format: every [interval][Frequency (day/week/month/year)]"
				+ "		\n			Example: add Taskname by today 3pm every day"
				+ "		\n			Example: add Eventname from tomorrow 7pm to 8pm every week"
				+ "		\n			Example: 1 every 2 month "
				+ "		\n			Example: 1 every year"
				+ "\n"
				+ "		\n	reset"
				+ "		\n		Format: [Task Index] reset [Field (all/dea/event/des/pri/rem/cat)]"
				+ "		\n			Example: 1 reset all"
				+ "		\n			Example: 1 reset des"
				+ "\n"
				+ "		\n	exit"
				+ "		\n		Format: exit");
		renameEvent.add("List of Command Keywords in TankTask:"
				+ "		\nEditing your Tasks"
				+ "\n"
				+ "		\n	rename"
				+ "		\n		Format: [Task Index] rename [New Task Name]"
				+ "		\n			Example: 1 rename Kill the fish"
				+ "\n"
				+ "		\n	deadline (Alternate form: 'by', 'dea')"
				+ "		\n		Format: [Task Index] by [Deadline]"
				+ "		\n			Example: 1 by 20 Oct 3pm"
				+ "		\n			Example: 1 by tmr"
				+ "		\n			Example: 1 by next tues"
				+ "\n"
				+ "		\n	event (Alternate form: 'from')"
				+ "		\n		Format: [Task Index] from [Start] to [End]"
				+ "		\n			Example: 1 from 20 Oct 3pm"
				+ "		\n			Example: 1 from 20 Oct 3pm to 5pm"
				+ "		\n			Example: 1 from 20 Oct 3pm to 21 Oct 8am");
		desCate.add("List of Command Keywords in TankTask:"
				+ "		\nEditing your Tasks"
				+ "\n"
				+ "		\n	description (Alternate form: des)"
				+ "		\n		Format: [Task Index] des [Description]"
				+ "		\n			Example: 1 des need to do this"
				+ "\n"
				+ "		\n	priority (Alternate form: pri)"
				+ "		\n		Format: [Task Index] pri [Priority (1-5)]"
				+ "		\n			Example: 1 pri 5"
				+ "\n"
				+ "		\n	reminder (Alternate form: rem)"
				+ "		\n		Format: [Task Index] rem [Reminder Date]"
				+ "		\n			Example: 1 rem 20 Oct 3pm");
		oneShot.add("List of Command Keywords in TankTask:"
				+ "\n"
				+ "		\n	One-shot Command Examples:"
				+ "\n"
				+ "		\n	Add ToDo (with description and priority)"
				+ "		\n		Format: add [Task Name] by [Deadline] des [Description] pri [Priority]"
				+ "		\n			Example: add Feed the fish by today des slightly less than the usual amount pri 4"
				+ "\n"
				+ "		\n	Add Event (with reminder)"
				+ "		\n		Format: add [Task Name] from [Start] to [End] rem [Reminder]"
				+ "		\n			Example: add Feed the fish from today 6pm to 6:15pm rem 6pm today"
				+ "\n"
				+ "		\n	Set/Edit Information"
				+ "		\n	Format [Task Index] [command1] [command1's content] [command 2] [command2's content]..."
				+ "		\n	(Commands available are deadline/event, description, priority, reminder, category, rename)"
				+ "		\n		Example: 1 pri 2 rem tomorrow 12pm cat hobbies des running out of fish food "
				+ "\n");
		
			fullList.add(add);
			fullList.add(doneUndone);
			fullList.add(deleteRedo);
			fullList.add(showSort);
			fullList.add(everyExit);
			fullList.add(renameEvent);
			fullList.add(desCate);
			fullList.add(oneShot);
	}
	
	private void listNames(){
		names = new ArrayList<String>();
		names.add("");
		names.add("Adding tasks");
		names.add("Done, undone");
		names.add("Delete, Undo, Redo");
		names.add("Search, Show, Sort");
		names.add("Recurring, Reset, exit");
		names.add("Rename task, Edit deadline & event dates");
		names.add("Edit description & priority & reminders & category");
		names.add("Advance features: One Shot");
		names.add("");
	}

	public ArrayList<ArrayList<String>> getSplitList(){
		return fullList;
	}
	public ArrayList<String> getSplitNaming(){
		return names;
	}
}