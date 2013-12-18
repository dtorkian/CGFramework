/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */

package m.blatt9;

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

import java.util.ArrayList;




import math.Mat4;
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
	private Mat4            projectionMatrix;
	private float           shine               = 0f;
	private float           shineDiffrence      = 0f;
	boolean p_Key                               = false;
	boolean ambiente                            = true;
	boolean diffuse                             = true;
	boolean specular                            = true;
	float   xCoordinateofLight                  = 0;
	float   yCoordinateofLight                  = 0;
	float   zCoordinateofLight                  = 0;
	
	/**
	 * @param width The horizontal window size in pixels
	 * @param height The vertical window size in pixels
	 */
	public Sandbox( int width, int height )
	{
		shaderProgram    = new ShaderProgram( "A9OBJ_Sample_vs.glsl", "A9OBJ_Sample_fs.glsl" );
		
		modelMatrix      = new Mat4();
		viewMatrix       = Mat4.translation( 0.0f, 0.0f, -6.0f );
		projectionMatrix = Mat4.perspective( 60.0f, (float)width, (float)height, 0.01f, 500.0f );
		meshes           = new ArrayList<Mesh>();
		
		
		this.createMesh();
		
		
		glEnable( GL_DEPTH_TEST );
	}
	
	
	/**
	 * @param deltaTime The time in seconds between the last two frames
	 */
	public void update( float deltaTime )
	{
		
		if( Keyboard.isKeyDown(Keyboard.KEY_A) && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) )
			xCoordinateofLight-=5;
		if( Keyboard.isKeyDown(Keyboard.KEY_D) && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) )
			xCoordinateofLight+=5;
		if( Keyboard.isKeyDown(Keyboard.KEY_W) && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) )
			yCoordinateofLight+=5;
		if( Keyboard.isKeyDown(Keyboard.KEY_S) && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) )
			yCoordinateofLight-=5;
		if( Keyboard.isKeyDown(Keyboard.KEY_Q) && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) )
			zCoordinateofLight+=5;
		if( Keyboard.isKeyDown(Keyboard.KEY_E) && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) )
			zCoordinateofLight-=5;
		
		
		if( Keyboard.isKeyDown(Keyboard.KEY_1) )
			ambiente = !ambiente;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_2) )
			diffuse = !diffuse;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_3) )
			specular = !specular;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) )
			Main.exit = true;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_ADD) )
			shineDiffrence++;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT) )
			shineDiffrence--;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_0) )
			shineDiffrence=0;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_P) )
			p_Key = !p_Key;
		
		float cameraSpeed = 5.0f * deltaTime;
		
		if( Keyboard.isKeyDown(Keyboard.KEY_W) && !((Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) )
			viewMatrix.mul( Mat4.translation(0.0f, 0.0f, cameraSpeed) );
		
		if( Keyboard.isKeyDown(Keyboard.KEY_S) && !((Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))))
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
		
		if (p_Key) shine=32+shineDiffrence;
		else shine=132+shineDiffrence;
	}
	
	
	public void draw()
	{
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		shaderProgram.useProgram();
		shaderProgram.setUniform("shine", shine);
		shaderProgram.setUniform("ambiente", ambiente==true ? 1 : 0);
		shaderProgram.setUniform("diffuse", diffuse==true ? 1 : 0);
		shaderProgram.setUniform("specular", specular==true ? 1 : 0);
		shaderProgram.setUniform( "uModel",      modelMatrix );
		shaderProgram.setUniform( "uView",       viewMatrix );
		shaderProgram.setUniform( "uProjection", projectionMatrix );
		shaderProgram.setUniform( "ligthPosition" , new Vec3(10+xCoordinateofLight,10+yCoordinateofLight,10+zCoordinateofLight));
		
		
		for( Mesh mesh : meshes )
		{
			mesh.draw( GL_TRIANGLES );
		}
	}
	
	
	private void createMesh()
	{
		OBJContainer        objContainer = OBJContainer.loadFile( "scene.obj" );
		ArrayList<OBJGroup> objGroups    = objContainer.getGroups();
		
		for( OBJGroup group : objGroups )
		{
			float[] positions = group.getPositions();
			float[] normals   = group.getNormals();
			int[]   indices   = group.getIndices();
			
			Mesh mesh = new Mesh( GL_STATIC_DRAW );
			mesh.setAttribute( 0, positions, 3 );
			mesh.setAttribute( 1, calculateVertexNormals(positions, indices), 3 );
		//	mesh.setAttribute( 1, normals, 3 );
			mesh.setIndices( indices );
			
			meshes.add( mesh );
		}
	}
	
	private float[] calculateVertexNormals( float[] positions, int[] indices){
		float[] vertexNorms = new float[(indices.length)];
	
		for (int i=0; i<indices.length; i+=3){
			
			 float[] points = new float[9]; 
				points[0]=positions[indices[i]*3];
				points[1]=positions[indices[i]*3+1];
				points[2]=positions[indices[i]*3+2];
				points[3]=positions[indices[i+1]*3];
				points[4]=positions[indices[i+1]*3+1];
				points[5]=positions[indices[i+1]*3+2];
				points[6]=positions[indices[i+2]*3];
				points[7]=positions[indices[i+2]*3+1];
				points[8]=positions[indices[i+2]*3+2];
				
				Vec3 a = new Vec3(points[0], points[1], points[2]);
				Vec3 b = new Vec3(points[3], points[4], points[5]);
				Vec3 c = new Vec3(points[6], points[7], points[8]);
				
				Vec3 aSubC = Vec3.sub(c, a);
				Vec3 bSubC = Vec3.sub(c, b);

				Vec3 faceNorm = Vec3.cross(aSubC, bSubC);
				faceNorm.normalize();
				a = Vec3.normalize(a);
				b = Vec3.normalize(b);
				c = Vec3.normalize(c);
			
				vertexNorms[indices[i]*3]+=faceNorm.x;
				vertexNorms[indices[i]*3+1]+=faceNorm.y;
				vertexNorms[indices[i]*3+2]+=faceNorm.z;
				vertexNorms[indices[i+1]*3]+=faceNorm.x;
				vertexNorms[indices[i+1]*3+1]+=faceNorm.y;
				vertexNorms[indices[i+1]*3+2]+=faceNorm.z;
				vertexNorms[indices[i+2]*3]+=faceNorm.x;
				vertexNorms[indices[i+2]*3+1]+=faceNorm.y;
				vertexNorms[indices[i+2]*3+2]+=faceNorm.z;
		}
		for (int i=0; i<vertexNorms.length; i+=3){
			Vec3 value = new Vec3(vertexNorms[i], vertexNorms[i+1], vertexNorms[i+2]);
			value.normalize();
			vertexNorms[i] = value.x;
			vertexNorms[i+1] = value.y;
			vertexNorms[i+2] = value.z;
		}
	    return vertexNorms;
	}
}
