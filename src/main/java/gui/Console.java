package main.java.gui;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextArea;

//@@author A0126125R
public class Console extends OutputStream{
	private TextArea txt;
	
	public Console(TextArea consoleText) {
        this.txt = consoleText;
    }

	@Override
	public void write(int b) throws IOException {
		txt.appendText(String.valueOf((char) b));
		
	}

}
