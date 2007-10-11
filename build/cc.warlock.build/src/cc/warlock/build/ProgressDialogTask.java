package cc.warlock.build;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class ProgressDialogTask extends Task {

	protected String name = "DEFAULT", icon, title = "Build Progress", message = "Current build progress:";
	protected int min = 0, max = 100, current = 0;
	
	protected ArrayList<String> barNames = new ArrayList<String>();
	protected ArrayList<String> barMessages = new ArrayList<String>();
	protected ArrayList<Integer> mins = new ArrayList<Integer>();
	protected ArrayList<Integer> maxes = new ArrayList<Integer>();
	protected ArrayList<Integer> currents = new ArrayList<Integer>();
	
	
	protected String[] array (List<String> list)
	{
		return list.toArray(new String [list.size()]);
	}
	
	protected int[] array (List<Integer> list)
	{
		int[] array = new int[list.size()];
		int i = 0;
		for (Integer n : list)
			array[i++] = n;
		
		 return array;
	}
	
	@Override
	public void execute() throws BuildException {
		if (getProject().getProperty("hideProgress") != null)
		{
			return;
		}
		
		for (ProgressBarTask bar : bars)
		{
			barNames.add(bar.getName());
			barMessages.add(bar.getMessage());
			mins.add(bar.getMin());
			maxes.add(bar.getMax());
			currents.add(bar.getCurrent());
		}
		
		new ProgressDialog(name, icon, title, array(barNames), array(barMessages), array(mins), array(maxes), array(currents));
	}
	
	protected ArrayList<ProgressBarTask> bars = new ArrayList<ProgressBarTask>();
	
	public void addProgressBar (ProgressBarTask bar)
	{
		bars.add(bar);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	
	
}
