package m.blatt10;
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
import static math.Mat4.*;

import java.util.ArrayList;

import math.Mat4;
import math.MathUtil;
import math.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import util.Color;
import util.Mesh;
import util.OBJContainer;
import util.OBJGroup;
import util.ShaderProgram;



public class Sandbox
{
	private ShaderProgram   shaderProgram;
	private ArrayList<Mesh> meshes;
	private Mat4            modelMatrix;
	private Mat4            viewMatrix;
	private int             windowWidth;
	private int             windowHeight;
	private float           stereoBase;
	private float           focalDistance;
	
	
	
	/**
	 * @param width The horizontal window size in pixels
	 * @param height The vertical window size in pixels
	 */
	public Sandbox( int width, int height )
	{
		windowWidth   = width;
		windowHeight  = height;
		stereoBase    = 0.065f;
		focalDistance = 2.5f;
		
		shaderProgram = new ShaderProgram( "Color_vsA10.glsl", "Color_fsA10.glsl" );
		
		modelMatrix   = new Mat4();
		viewMatrix    = Mat4.translation( 0.0f, 0.0f, -3.0f );
		meshes        = new ArrayList<Mesh>();
		
		
		this.loadObj( "stereo_scene.obj" );
		this.loadObj( "ground_plane.obj" );

		glEnable( GL_DEPTH_TEST );
	}
	
	
	/**
	 * @param deltaTime The time in seconds between the last two frames
	 */
	public void update( float deltaTime )
	{
		if( Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) )
			Main.exit();
		
		if( Keyboard.isKeyDown(Keyboard.KEY_F) )
			Main.toggleFullscreen();
		
		float cameraSpeed = 5.0f * deltaTime;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_W) )
			viewMatrix.mul( Mat4.translation(0.0f, 0.0f, cameraSpeed) );
		
		if( Keyboard.isKeyDown(Keyboard.KEY_S) )
			viewMatrix.mul( Mat4.translation(0.0f, 0.0f, -cameraSpeed) );
		
		if( Keyboard.isKeyDown(Keyboard.KEY_J) )
			focalDistance += 0.01f;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_K) )
			focalDistance -= 0.01f;
		
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
		
		float fov    = MathUtil.toRad( 60.0f );
		float near   = 0.1f;
		float far    = 50.0f;
		float aspect = (float) windowWidth / (float) windowHeight;
		
	    float top    = near * (float) Math.tan( fov / 2.0f );
	    float bottom = -top;
	    float right  = top * aspect;
	    float left   = -right;
	    
	    float offset = (stereoBase/2)*(near/focalDistance);
	    
	    float rightLeft = right+offset;
	    float leftLeft = left+offset;
	    
	    float rightRight = right-offset;
	    float leftRight = left-offset;

		Mat4 viewLeft  = mul( translation( 0.0f + stereoBase/2, 0.0f, 0.0f), viewMatrix );
		Mat4 viewRight = mul( translation( 0.0f - stereoBase/2, 0.0f, 0.0f), viewMatrix );
		
	    Mat4 projLeft  = perspective( leftLeft, rightLeft, top, bottom, near, far );
	    Mat4 projRight = perspective( leftRight, rightRight, top, bottom, near, far );
	    
		glViewport( 0, 0, windowWidth/2, windowHeight );
		this.drawMeshes( viewLeft, projLeft );
		
		glViewport( windowWidth/2, 0, windowWidth/2, windowHeight );
		this.drawMeshes( viewRight, projRight );
	}
	
	public void onResize( int width, int height )
	{
		windowWidth  = width;
		windowHeight = height;
	}
	
	
	public void drawMeshes( Mat4 viewMatrix, Mat4 projMatrix )
	{
		shaderProgram.useProgram();
		shaderProgram.setUniform( "uModel",      modelMatrix );
		shaderProgram.setUniform( "uView",       viewMatrix );  
		shaderProgram.setUniform( "uProjection", projMatrix );
		shaderProgram.setUniform( "focalDistance", focalDistance );

		for( Mesh mesh : meshes )
		{
			mesh.draw( GL_TRIANGLES );
		}
	}
	
	
	private void loadObj( String filename )
	{
		OBJContainer        objContainer = OBJContainer.loadFile( filename );
		ArrayList<OBJGroup> objGroups    = objContainer.getGroups();
		
		for( OBJGroup group : objGroups )
		{
			float[] positions = group.getPositions();
			float[] normals   = group.getNormals();
			int[]   indices   = group.getIndices();
			
			Mesh mesh = new Mesh( GL_STATIC_DRAW );
			mesh.setAttribute( 0, positions, 3 );
			mesh.setAttribute( 1, normals, 3 );
			mesh.setIndices( indices );
			
			meshes.add( mesh );
		}
	}
}
