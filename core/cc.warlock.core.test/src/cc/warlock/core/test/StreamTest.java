package cc.warlock.core.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IStyledString;
import cc.warlock.core.client.IWarlockStyle.StyleType;
import cc.warlock.core.client.internal.Stream;
import cc.warlock.core.client.internal.StyledString;
import cc.warlock.core.client.internal.WarlockStyle;

public class StreamTest {

	protected static class StreamExt extends Stream {
		public StreamExt(String name) { super(name); }
		public static Stream createStream (String name) { return Stream.fromName(name); } 
	}
	
	protected static class Listener implements IStreamListener {
		public boolean cleared, receivedText, prompted, echoed, donePrompting;
		public String echo, prompt;
		public IStyledString text;
		
		protected void handleEvent() {
			cleared = receivedText = prompted = echoed = donePrompting = false;
			text = null;
			echo = prompt = null;
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

		public void streamReceivedText(IStream stream, IStyledString text) {
			handleEvent();
			receivedText = true;
			this.text = text;
		}
	}
	
	protected static Stream stream;
	protected static final String STREAM_NAME = "testStream";
	protected static final String TEST_STRING= "testing stream send--\n";
	protected static final WarlockStyle TEST_STYLE = new WarlockStyle(new StyleType[] { StyleType.BOLD }, "testStyle", null, 0, 0);
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
		Assert.assertEquals(listener.text.getBuffer().toString(), TEST_STRING);
		Assert.assertEquals(listener.text.getStyles().size(), 1);
	}

	@Test
	public void testSendStringIWarlockStyle() {
		stream.send(TEST_STRING, TEST_STYLE);
		Assert.assertTrue(listener.receivedText);
		Assert.assertEquals(listener.text.getBuffer().toString(), TEST_STRING);
		Assert.assertEquals(listener.text.getStyles().size(), 1);
		Assert.assertEquals(listener.text.getStyles().toArray()[0], TEST_STYLE);
	}

	@Test
	public void testSendIStyledString() {
		StyledString string = new StyledString();
		string.addStyle(TEST_STYLE);
		string.getBuffer().append(TEST_STRING);
		
		stream.send(string);
		Assert.assertTrue(listener.receivedText);
		Assert.assertEquals(listener.text.getBuffer().toString(), string.getBuffer().toString());
		Assert.assertEquals(listener.text.getBuffer().toString(), TEST_STRING);
		Assert.assertEquals(listener.text.getStyles().size(), 1);
		Assert.assertEquals(listener.text.getStyles().toArray()[0], TEST_STYLE);
	}

	@Test
	public void testPrompt() {
		stream.prompt(">");
		Assert.assertTrue(listener.prompted);
		Assert.assertEquals(listener.prompt, ">");
	}

	@Test
	public void testDonePrompting() {
		stream.donePrompting();
		
		Assert.assertTrue(listener.donePrompting);
	}

	@Test
	public void testIsPrompting() {
		stream.prompt(">");
		
		Assert.assertTrue(stream.isPrompting());
		
		stream.donePrompting();
		
		Assert.assertFalse(stream.isPrompting());
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
