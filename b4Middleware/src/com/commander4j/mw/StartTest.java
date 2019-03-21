package com.commander4j.mw;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.MiddlewareConfig;

public class StartTest
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((StartTest.class));
	public MiddlewareConfig cfg;

	public boolean runTest()
	{
		boolean result = true;

		cfg = new MiddlewareConfig();

		cfg.loadMaps(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "config.xml");

		System.out.println("*************************");
		System.out.println("**   " + cfg.getMaps().size() + " MAPS LOADED    **");
		System.out.println("*************************");

		if ((cfg.getMapDirectoryErrorCount() == 0))
		{

			String mapId = "";
			String description = "";

			for (int test = 0; test < cfg.getMaps().size(); test++)
			{

				mapId = cfg.getMaps().get(test).getId();
				description = cfg.getMaps().get(test).getDescription();
				System.out.println(mapId + " - " + description);

				// Check all paths are valid

				File inputPath = new File(cfg.getMaps().get(test).getInboundInterface().getInputPath());
				if (inputPath.exists())
				{
					System.out.println("  " + cfg.getMaps().get(test).getInboundInterface().getId() + " " + cfg.getMaps().get(test).getInboundInterface().getInputPath());
					int outputMaps = cfg.getMaps().get(test).getNumberofOutboundInterfaces();

					for (int out = 0; out < outputMaps; out++)
					{
						File outputPath = new File(cfg.getMaps().get(test).getOutBoundInterface(out).getOutputPath());
						System.out.println("  " + cfg.getMaps().get(test).getOutBoundInterface(out).getId() + " " + cfg.getMaps().get(test).getOutBoundInterface(out).getOutputPath());
						if (outputPath.exists() == false)
						{
							System.out.println("*ERROR* Output Path does not exisit " + cfg.getMaps().get(test).getOutBoundInterface(out).getOutputPath());
							result = false;
						}
					}

				}
				else
				{
					System.out.println("*ERROR* Input Path does not exisit " + cfg.getMaps().get(test).getInboundInterface().getInputPath());
					result = false;
				}

				if (result = true)
				{

				}

				inputPath = null;
			}

		}
		else
		{
			result = false;
		}

		System.out.println("*************************");
		System.out.println("**    TEST COMPLETE    **");

		if (result == true)
		{
			System.out.println("**        PASS         **");
			System.out.println("*************************");
		}
		else
		{
			System.out.println("**        FAIL         **");
			System.out.println("*************************");
		}

		return result;
	}

	public static void main(String[] args)
	{
		StartTest test = new StartTest();
		if (test.runTest())
		{
			System.exit(0);
		}
		else
		{
			System.exit(1);
		}

	}

}
