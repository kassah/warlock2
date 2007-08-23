package cc.warlock.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

import org.antlr.runtime.CharStream;

public class StormFrontCharStream implements CharStream {

	private int lineOffset = 0;
	private int position = 0;
	private int index = 0;
	private int curLine = 0;
	private ArrayList<String> lines = new ArrayList<String>();
	
	private StormFrontConnection connection;
	private BufferedReader reader;
	
	private StringBuffer buffer = new StringBuffer();
	private byte[] readBuffer = new byte[4096];
	
	private Stack<Integer> marks = new Stack<Integer>();
	
	public StormFrontCharStream(InputStream inputStream) {
		this.reader = new BufferedReader(new InputStreamReader(inputStream));
		readLine();
	}
	
	public int LT(int arg0) {
		// TODO Auto-generated method stub
		return LA(arg0);
	}

	public int getCharPositionInLine() {
		return position;
	}

	public int getLine() {
		return lineOffset + curLine;
	}

	public void setCharPositionInLine(int newPosition) {
		// TODO make sure this is a valid position
		seek(index + newPosition - position);
	}

	public void setLine(int newLine) {
		if (newLine < lineOffset) {
			assert(false);
			// TODO handle this
			return;
		}
		
		while(newLine > lineOffset + curLine) {
			readLine();
			curLine++;
		}
	}
	
	private void readLine() {
		try {
			lines.add(reader.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String substring(int arg0, int arg1) {
		// TODO docs say we don't need this. find out if that's true
		assert(false);
		return null;
	}

	public int LA(int lookahead) {
		if(position + lookahead >= 0) {
			int relevantLine = 0;
			int charsToRelevantLine = 0;
			int charsInRelevantLine;
			while(position + lookahead - charsToRelevantLine 
					>= (charsInRelevantLine = lines.get(curLine + relevantLine).length())) {
				charsToRelevantLine += charsInRelevantLine;
				relevantLine++;
				if(lines.size() < curLine + relevantLine + 1) {
					readLine();
				}
			}
			return lines.get(curLine + relevantLine).charAt(position + lookahead - charsToRelevantLine);
		}
		
		// TODO look in previous lines
		assert(false);
		return 0;
	}

	public void consume() {
		position++;
		index++;
		
		if(position >= lines.get(curLine).length()) {
			position = 0;
			readLine();
			curLine++;
		}
	}

	public int index() {
		return index;
	}

	public int mark() {
		marks.push(index);
		return index;
	}

	public void release(int arg0) {
		// TODO implement this so we don't leak memory
	}

	public void rewind() {
		if (marks.size() > 0) {
			moveBack(marks.pop());
		} else moveBack(0);
	}

	public void rewind(int newIndex) {
		moveBack(newIndex);
		release(index);
	}

	private void moveBack(int newIndex) {
		while(index > newIndex) {
			if(position == 0) {
				curLine--;
				position = lines.get(curLine).length() - 1;
			} else {
				position--;
			}
			index--;
		}
	}
	
	public void seek(int newIndex) {
		if(newIndex <= index) {
			moveBack(newIndex);
		} else {
			while(index < newIndex) {
				consume();
			}
		}
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
