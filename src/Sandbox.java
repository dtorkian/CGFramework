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
	private Mat4          projectionMatrix;
	
	
	
	public Sandbox()
	{
		shaderProgram    = new ShaderProgram( "Color_vs.glsl", "Color_fs.glsl" );
		
		modelMatrix      = new Mat4();
		projectionMatrix = Mat4.perspective( 60.0f, 16.0f, 9.0f, 0.01f, 10.0f );
		projectionMatrix.mul( Mat4.translation(0.0f, 0.0f, -3.0f) );
		
		
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
		shaderProgram.setUniform( "uProjection", projectionMatrix );
		shaderProgram.setUniform( "uColor",      Color.green() );
		
		triangleMesh.draw( GL_TRIANGLES );
	}
	
	
	private void createTriangle()
	{
		float[] positions = { -0.5f, -0.5f, 0.0f, 
							   0.5f, -0.5f, 0.0f,
							   0.0f,  0.5f, 0.0f };
		
		int[] indices = { 0, 1, 2 };
		
		int attributeLocation = 0; // has to match the location set in the vertex shader
		int floatsPerPosition = 3; // x, y and z values per position
		
		triangleMesh = new Mesh( GL_STATIC_DRAW );
		triangleMesh.setAttribute( attributeLocation, positions, floatsPerPosition );
		triangleMesh.setIndices( indices );
	}
}
