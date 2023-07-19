package edu.umich.wld;


public class BinnerException extends Exception 
	{
	private static final long serialVersionUID = 867177158577722670L;
	
	private String metworksMessageTag = "Error: ";
	private String binnerMsg; 
	
	public BinnerException(Exception e)
		{
		this(e.getMessage());
		}

	public BinnerException(String msg)
		{
		this(msg, false);
		}
	
	public BinnerException(String msg, boolean showSystemMessage) 
		{
		super(msg);
		setBinnerMessage(metworksMessageTag + msg + (showSystemMessage ? System.getProperty("line.separator") + getMessage() : ""));
		}

	public String getBinnerMessage()
		{
		return binnerMsg;
		}
	
	public void setBinnerMessage(String msg)
		{
		binnerMsg = msg;
		}
	}
