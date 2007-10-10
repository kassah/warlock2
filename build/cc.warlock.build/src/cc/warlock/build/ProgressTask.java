package cc.warlock.build;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class ProgressTask extends Task {
	protected static final int DEFAULT = -9;
	
	protected String name = "DEFAULT";
	protected String bar = "bar0";
	protected String message;
	protected int add = DEFAULT, set = DEFAULT, min = DEFAULT, max = DEFAULT;
	protected boolean done = false;
	
	@Override
	public void execute() throws BuildException {
		if (name == null)
		{
			throw new BuildException("No name was specified for this progress!");
		}
		
		if (bar == null)
		{
			throw new BuildException("No progress bar was specified for this progress!");
		}
		
		ProgressDialog dialog = ProgressDialog.getProgressDialog(name);
		
		if (dialog == null)
		{
			throw new BuildException("A dialog for the name \"" + name + "\" could not be found");
		}

		ProgressPanel panel = dialog.getPanel(bar);
		if (panel == null)
		{
			throw new BuildException("A progress bar for the name \"" + bar + "\" could not be  found");
		}
		
		if (message != null)
		{
			panel.setMessage(message);
		}
		
		if (min != DEFAULT)
			panel.setMin(min);
		if (max != DEFAULT)
			panel.setMax(max);
		
		if (add != DEFAULT)
		{
			performAdd(panel);
		}
		else if (set != DEFAULT)
		{
			performSet(panel);
		}
		else if (done)
		{
			performDone(dialog, panel);
		}
	}
	
	protected void performAdd (ProgressPanel panel)
		throws BuildException
	{
		int newAmount = panel.getCurrent() + add;
		if (newAmount <= panel.getMax())
		{
			panel.setCurrent(newAmount);
		}
	}
	
	protected void performSet (ProgressPanel panel)
		throws BuildException
	{
		if (set >= panel.getMin() && set <= panel.getMax())
		{
			panel.setCurrent(set);
		}
	}
	
	protected void performDone (ProgressDialog dialog, ProgressPanel panel)
		throws BuildException
	{
		panel.setCurrent(panel.getMax());
		
		boolean closeDialog = true;
		for (ProgressPanel p : dialog.getPanels())
		{
			if (p != panel)
			{
				if (p.getCurrent() != p.getMax()) closeDialog = false; break;
			}
		}
		
		if (closeDialog)
			dialog.setVisible(false);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getBar() {
		return bar;
	}

	public void setBar(String bar) {
		this.bar = bar;
	}

	public int getAdd() {
		return add;
	}

	public void setAdd(int add) {
		this.add = add;
	}

	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
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
	
}
