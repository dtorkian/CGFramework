package m.blatt5;
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
import m11070051.blatt5.Main;
import m11070051.blatt5.Monkey;
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
		glClearColor(.2f, 1, .3f, .1f);
		shaderProgram.useProgram();
		shaderProgram.setUniform( "uModel",      modelMatrix );
		shaderProgram.setUniform( "uView",       viewMatrix );
		shaderProgram.setUniform( "uProjection", projectionMatrix );
		
		
		//triangleMesh.draw( GL_TRIANGLES );
		for (int i=0; i<Monkey.indices.length; i+=3){
			
		 float[] points = new float[9]; 
			points[0]=Monkey.positions[Monkey.indices[i]*3];
			points[1]=Monkey.positions[Monkey.indices[i]*3+1];
			points[2]=Monkey.positions[Monkey.indices[i]*3+2];
			points[3]=Monkey.positions[Monkey.indices[i+1]*3];
			points[4]=Monkey.positions[Monkey.indices[i+1]*3+1];
			points[5]=Monkey.positions[Monkey.indices[i+1]*3+2];
			points[6]=Monkey.positions[Monkey.indices[i+2]*3];
			points[7]=Monkey.positions[Monkey.indices[i+2]*3+1];
			points[8]=Monkey.positions[Monkey.indices[i+2]*3+2];
			
			Vec3 a = new Vec3(points[0], points[1], points[2]);
			Vec3 b = new Vec3(points[3], points[4], points[5]);
			Vec3 c = new Vec3(points[6], points[7], points[8]);
			
			Vec3 aSubC = Vec3.sub(c, a);
			Vec3 bSubC = Vec3.sub(c, b);

			Vec3 crossProduct = Vec3.cross(aSubC, bSubC);
			
			crossProduct = Vec3.normalize(crossProduct);
			
							
			if (crossProduct.x<0) crossProduct.x = crossProduct.x *-1; 
			if (crossProduct.y<0) crossProduct.y = crossProduct.y *-1; 
			if (crossProduct.z<0) crossProduct.z = crossProduct.z *-1; 
			
			if (crossProduct.x<.01) crossProduct.x *=10; 
			if (crossProduct.y<.01) crossProduct.y *=10; 
			if (crossProduct.z<.01) crossProduct.z *=10; 
			
			if (crossProduct.x<.001) crossProduct.x *=100; 
			if (crossProduct.y<.001) crossProduct.y *=100; 
			if (crossProduct.z<.001) crossProduct.z *=100; 
			
			if (crossProduct.x>1) crossProduct.x = crossProduct.x /crossProduct.x; 
			if (crossProduct.y>1) crossProduct.y = crossProduct.y /crossProduct.y; 
			if (crossProduct.z>1) crossProduct.z = crossProduct.z /crossProduct.z; 
			
			System.out.println(crossProduct);
			shaderProgram.setUniform( "uColor",    new Vec3(crossProduct.x, crossProduct.y, crossProduct.z)   );
			triangleMesh.drawRange(GL_TRIANGLES, i, 3);
		}
		
	}
	
	
	private void createTriangle()
	{
		float[] positions = Monkey.positions;
		
		int[] indices = Monkey.indices;
		
		int attributeLocation = 0; // has to match the location set in the vertex shader
		int floatsPerPosition = 3; // x, y and z values per position
		
		triangleMesh = new Mesh( GL_STATIC_DRAW );
		triangleMesh.setAttribute( attributeLocation, positions, floatsPerPosition );
		triangleMesh.setIndices( indices );
		
	}
}
