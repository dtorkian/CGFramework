package m.blatt3;
/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
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

	private Mat4          sceneOriantationMatrix;
	private Mat4          projectionMatrix;
	private Mat4		  modelMatrix;
	private float 		  time;
	
	
	public Sandbox()
	{
		shaderProgram    = new ShaderProgram( "Color_vs.glsl", "Color_fs.glsl" );
		
		sceneOriantationMatrix      = new Mat4();
		projectionMatrix = Mat4.perspective( 60.0f, 20.0f, 10.0f, 0.01f, 10.0f );
		projectionMatrix.mul( Mat4.translation(0.0f, 0.0f, -3.0f) );
		
		modelMatrix = new Mat4();
		time=0;
		
		
	//	this.createTriangle();
		
		triangleMesh= Sphere.createMesh(0.4f, 30, 30, 0,0,0);


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
			
			sceneOriantationMatrix = rotationY.mul( rotationX ).mul( sceneOriantationMatrix );
		}
	}
	
	private void planet(float x, float y, float z, float scale, Vec3 color, float speed){
		modelMatrix = Mat4.translation(0,0,0);
	
		
		modelMatrix = Mat4.mul(Mat4.rotation(Vec3.xAxis(), time), modelMatrix);
		
		modelMatrix= modelMatrix.mul(Mat4.scale(scale));
		modelMatrix = Mat4.mul(Mat4.translation(x,y,z),modelMatrix);
		Mat4 rotationY = Mat4.rotation( Vec3.yAxis(),  time*speed );
		modelMatrix = rotationY.mul(modelMatrix);
		
		
		
		shaderProgram.setUniform( "uModel",      Mat4.mul(sceneOriantationMatrix, modelMatrix) );
		shaderProgram.setUniform( "uProjection", projectionMatrix );
		shaderProgram.setUniform( "uColor",      color );
		
		triangleMesh.draw( GL_TRIANGLES );
		
	}
	
	private void mond(float xplanet, float yplanet, float zplanet, float scale, Vec3 color, float speed, OrientationOfMond ausrichtung){
		modelMatrix = Mat4.translation(xplanet,yplanet,zplanet);
		modelMatrix= modelMatrix.mul(Mat4.scale(scale));
		
		if (ausrichtung == OrientationOfMond.Vertikal){
			modelMatrix = Mat4.mul(Mat4.translation(.0f,.2f,0),modelMatrix);
			modelMatrix = Mat4.mul(Mat4.rotation(Vec3.xAxis(), time*4), modelMatrix);
			Mat4 rotationY = Mat4.rotation( Vec3.yAxis(),  time*speed );
			modelMatrix = rotationY.mul(modelMatrix);
		}
		if (ausrichtung == OrientationOfMond.Horizonal){
			modelMatrix = Mat4.mul(Mat4.translation(-xplanet+.3f,-yplanet,-zplanet),modelMatrix);
			modelMatrix = Mat4.mul(Mat4.rotation(Vec3.yAxis(), time*4), modelMatrix);
			modelMatrix = Mat4.mul(Mat4.translation(xplanet,yplanet,zplanet),modelMatrix);
			modelMatrix = Mat4.mul(Mat4.rotation(Vec3.yAxis(), time*speed), modelMatrix);
		}

		shaderProgram.setUniform( "uModel",      Mat4.mul(sceneOriantationMatrix, modelMatrix) );
		shaderProgram.setUniform( "uProjection", projectionMatrix );
		shaderProgram.setUniform( "uColor",      color );
		
		triangleMesh.draw( GL_TRIANGLES );
		
	}
	
	
	public void draw()
	{
		
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		
		shaderProgram.useProgram();
	
		planet(0,0,0,1,Color.yellow(),1);
		planet(1.3f,0,0,.2f,Color.red(),1);
		mond(  1.3f,0,0,.1f , Color.white(), 1, OrientationOfMond.Vertikal);
		mond( 1.3f,0,0,.1f , Color.white(), 1, OrientationOfMond.Horizonal);
	
		planet(.9f,0,0,.4f,Color.blue(),2);
		mond(.9f,0,0,.1f , Color.white(), 2, OrientationOfMond.Horizonal);
	
		
		time+=.01f;
		
	}
	
}
