package getJavaVersion;

public class GetMajorVersion
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		String ver = System.getProperty("java.version");
		
		if (ver.startsWith("1."))
		{
			ver = ver.substring(2,3);
		}
		else
		{
			ver = ver.substring(0,2);
		}
		System.out.println("Java Version = "+ver);
		System.exit(Integer.valueOf(ver));
	}

}
