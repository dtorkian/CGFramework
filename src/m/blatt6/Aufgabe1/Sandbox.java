package m.blatt6.Aufgabe1;
/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;
import math.Mat4;
import math.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import util.Color;
import util.Mesh;
import util.ShaderProgram;



public class Sandbox
{
	private ShaderProgram shaderProgram;
	private Mesh          triangleMesh;
	private Mat4          modelMatrix;
	private Mat4          viewMatrix;
	private Mat4          projectionMatrix;
	
	
	
	/**
	 * @param width The horizontal window size in pixels
	 * @param height The vertical window size in pixels
	 */
	public Sandbox( int width, int height )
	{
		shaderProgram    = new ShaderProgram( "Color_vs.glsl", "Color_fs.glsl" );
		
		modelMatrix      = new Mat4();
		viewMatrix       = Mat4.translation( 0.0f, 0.0f, -3.0f );
		projectionMatrix = Mat4.perspective( 60.0f, (float)width, (float)height, 0.01f, 500.0f );
		
		
		this.createTriangle();
		

		glEnable( GL_DEPTH_TEST );
	}
	
	
	/**
	 * @param deltaTime The time in seconds between the last two frames
	 */
	public void update( float deltaTime )
	{
		if( Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) )
			Main.exit = true;
		
		float cameraSpeed = 5.0f * deltaTime;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_W) )
			viewMatrix.mul( Mat4.translation(0.0f, 0.0f, cameraSpeed) );
		
		if( Keyboard.isKeyDown(Keyboard.KEY_S) )
			viewMatrix.mul( Mat4.translation(0.0f, 0.0f, -cameraSpeed) );
		
		if( Mouse.isButtonDown(0) )
		{
			float rotationScale = 0.01f;
			
			float deltaX = (float) Mouse.getDX();
			float deltaY = (float) Mouse.getDY();

			Mat4 rotationX = Mat4.rotation( Vec3.yAxis(),  deltaX * rotationScale );
			Mat4 rotationY = Mat4.rotation( Vec3.xAxis(), -deltaY * rotationScale );
			
			modelMatrix = rotationY.mul( rotationX ).mul( modelMatrix );
		}
	}
	
	
	public void draw()
	{
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		
		shaderProgram.useProgram();
		shaderProgram.setUniform( "uModel",      modelMatrix );
		shaderProgram.setUniform( "uView",       viewMatrix );
		shaderProgram.setUniform( "uProjection", projectionMatrix );
		
		triangleMesh.draw( GL_TRIANGLES );
	}
	
	
	private void createTriangle()
	{
		float[] positions = { 0.5f, -0.5f, 0.0f, 
							   1.5f, -0.5f, 0.0f,
							   1.0f,  0.5f, 0.0f, 
		
							 -1.5f,  -.5f,  0,
								-.5f,-.5f,0,
								-.5f,.5f,0,
								-1.5f,.5f,0};
		float[] colors ={1f,0f,0f,
				0f,1f,0f,
				0f,0f,1f,
				1f,0f,0f,
				0f,1f,0f,
				0f,0f,1f,
						.3f,.4f,.5f
						};
		
		
		int[] indices = { 0, 1, 2, 3, 4, 5, 3, 5, 6 };
		
		int attributeLocation = 0; // has to match the location set in the vertex shader
		int floatsPerPosition = 3; // x, y and z values per position
		
		triangleMesh = new Mesh( GL_STATIC_DRAW );
		triangleMesh.setAttribute( attributeLocation, positions, floatsPerPosition );
		triangleMesh.setAttribute(1, colors, 3);
		triangleMesh.setIndices( indices );
	}
}
