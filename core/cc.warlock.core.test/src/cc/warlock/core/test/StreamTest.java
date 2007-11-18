package cc.warlock.core.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.IWarlockStyle.StyleType;
import cc.warlock.core.client.internal.Stream;
import cc.warlock.core.client.internal.WarlockStyle;

public class StreamTest {

	protected static class StreamExt extends Stream {
		public StreamExt(String name) { super(name); }
		public static Stream createStream (String name) { return Stream.fromName(name); } 
	}
	
	protected static class Listener implements IStreamListener {
		public boolean cleared, receivedText, receivedCommand, prompted, echoed, donePrompting;
		public String echo, prompt, command;
		public WarlockString text;
		
		protected void handleEvent() {
			cleared = receivedText = receivedCommand = prompted = echoed = donePrompting = false;
			text = null;
			echo = prompt = null;
		}
		
		@Override
		public void streamReceivedCommand(IStream stream, String text) {
			handleEvent();
			this.receivedCommand = true;
			this.command = text;
		}
		
		@Override
		public void streamReceivedText(IStream stream, WarlockString text) {
			handleEvent();
			receivedText = true;
			this.text = text;
		}
		
		public void streamCleared(IStream stream) {
			handleEvent();
			cleared = true;
		}

		public void streamDonePrompting(IStream stream) {
			handleEvent();
			donePrompting = true;
		}

		public void streamEchoed(IStream stream, String text) {
			handleEvent();
			echoed = true;
			echo = text;
		}

		public void streamPrompted(IStream stream, String prompt) {
			handleEvent();
			prompted = true;
			this.prompt = prompt;
		}
	}
	
	protected static Stream stream;
	protected static final String STREAM_NAME = "testStream";
	protected static final String TEST_STRING= "testing stream send--\n";
	protected static final WarlockStyle TEST_STYLE = new WarlockStyle(new StyleType[] { StyleType.BOLD });
	protected static Listener listener;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		stream = StreamExt.createStream(STREAM_NAME);
		listener = new Listener();
		stream.addStreamListener(listener);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		stream.removeStreamListener(listener);
		stream = null;
	}

	@Test
	public void testClear() {
		stream.clear();
		Assert.assertTrue(listener.cleared);
	}

	@Test
	public void testSendString() {
		stream.send(TEST_STRING);
		Assert.assertTrue(listener.receivedText);
		Assert.assertEquals(listener.text.toString(), TEST_STRING);
		Assert.assertEquals(listener.text.getStyles().size(), 0);
	}

	@Test
	public void testSendWarlockString() {
		WarlockString string = new WarlockString(null);
		string.addStyle(TEST_STYLE);
		string.append(TEST_STRING);
		
		stream.send(string);
		Assert.assertTrue(listener.receivedText);
		Assert.assertEquals(listener.text.toString(), string.toString());
		Assert.assertEquals(listener.text.toString(), TEST_STRING);
		Assert.assertEquals(listener.text.getStyles().size(), 1);
		Assert.assertEquals(listener.text.getStyles().get(0).style, TEST_STYLE);
	}

	@Test
	public void testPrompt() {
		stream.prompt(">");
		Assert.assertTrue(listener.prompted);
		Assert.assertEquals(listener.prompt, ">");
	}

	@Test
	public void testIsPrompting() {
		stream.prompt(">");
		
		Assert.assertTrue(stream.isPrompting());
	}

	@Test
	public void testEcho() {
		stream.echo(TEST_STRING);
		
		Assert.assertTrue(listener.echoed);
		Assert.assertEquals(listener.echo, TEST_STRING);
	}

	@Test
	public void testGetName() {
		Assert.assertEquals(stream.getName().get(), STREAM_NAME);
	}

}
