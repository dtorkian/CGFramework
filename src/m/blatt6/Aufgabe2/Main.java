package m.blatt6.Aufgabe2;
/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;



public class Main
{
	public static boolean exit = false;
	
	
	public static void main( String[] args )
	{
		int width  = 1280;
		int height = 720;
		
		try
		{
			if( LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX )
				Display.create( new PixelFormat(0,16,0,0), new ContextAttribs(3, 2).withProfileCore(true) );
			else
				Display.create( new PixelFormat(0,16,0,0), new ContextAttribs(3, 1) );
			
			Display.setDisplayMode( new DisplayMode(width, height) );
			Display.setVSyncEnabled( true );
			Display.setTitle( "CG FHK" );
			
			glViewport( 0, 0, width, height );
		}
		catch( LWJGLException e )
		{
			e.printStackTrace();
		}
		
		Sandbox sandbox   = new Sandbox( width, height );
		float   deltaTime = 0.0f;
		long    lastTime  = 0;
		
		while( !Display.isCloseRequested() && !Main.exit )
        {
        	long time = System.nanoTime();
        	deltaTime = (float)(time - lastTime) * 1e-9f;
			lastTime  = time;
			
			sandbox.update( deltaTime );
			sandbox.draw();
			
			Display.update();
        }
		
		Display.destroy();
	}
}
