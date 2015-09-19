package main.java.gui;

import java.util.Scanner;

import main.java.backend.Logic.Logic;

public class GUIStub {
	
	public static void main(String[] args) {
		Logic logicComponent = new Logic(args[0]);
		Scanner inputScanner = new Scanner(System.in);
		while(true){
			String processInput = logicComponent.executeCommand(inputScanner.nextLine());
			System.out.println(processInput);
		}
	}
	
}
