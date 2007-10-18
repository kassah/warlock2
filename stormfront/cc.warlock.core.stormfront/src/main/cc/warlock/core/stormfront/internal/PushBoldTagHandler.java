package cc.warlock.core.stormfront.internal;

import java.util.regex.Pattern;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class PushBoldTagHandler extends DefaultTagHandler {

	protected IWarlockStyle style;
	protected static final Pattern linkPattern = Pattern.compile("(www\\.|http://).+");
	
	public PushBoldTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "pushBold", "popBold" };
	}
	
	public void handleEnd() {
		if (getCurrentTag().equals("pushBold"))
		{
			handler.getCurrentStream().sendStyle(WarlockStyle.createBoldStyle());
		}
		
		else if (getCurrentTag().equals("popBold"))
		{
			handler.getCurrentStream().sendStyle(WarlockStyle.createEndBoldStyle());
			// we'll need to figure out another way to auto linkify http links
//			IStyledString buffer = handler.peekBuffer();
//			
//			style.setLength(buffer.getBuffer().length());
//			buffer.addStyle(style);
//			
//			String matchText = buffer.getBuffer().toString();
//			
//			Matcher linkMatcher = linkPattern.matcher(matchText.trim());
//			if (linkMatcher.matches())
//			{
//				style.addStyleType(IWarlockStyle.StyleType.LINK);
//				String address = linkMatcher.group();
//				if (address.startsWith("www")) { address = "http://" + address; }
//				
//				try {
//					style.setLinkAddress(new URL(address));
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}
//			}
//			
//			handler.sendAndPopBuffer();
			style = null;
		}
	}
}
