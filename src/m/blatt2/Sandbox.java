package m.blatt2;

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

public class Sandbox {
	private ShaderProgram shaderProgram;
	private Mesh triangleMesh;
	private Mesh box;
	private Mat4 modelMatrix;
	private Mat4 projectionMatrix;
	private Mesh line;
	private Mesh lineBox;
	private Mesh mobius;
	private Mesh lineMobius;

	public Sandbox() {
		shaderProgram = new ShaderProgram("Color_vs.glsl", "Color_fs.glsl");

		modelMatrix = new Mat4();
		projectionMatrix = Mat4.perspective(60.0f, 16.0f, 9.0f, 0.01f, 10.0f);
		projectionMatrix.mul(Mat4.translation(0.0f, 0.0f, -3.0f));

		// this.createTriangle();

		int[] indices = { 0, 1, 2, 3, 4, 5 };
		float[] positions = { -1.5f, -0.5f, 0.0f, -0.5f, -0.5f, 0.0f, -1.0f,
				0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 1.0f, 0.5f, 0.0f, 1.5f, -0.5f,
				0.0f };
		createTriangle(positions, indices);

		float[] positionsBox = { -.5f,-.5f,-.5f,  .5f,-.5f,.5f, -.5f,-.5f,.5f, 
				                              .5f, -0.5f, -.5f,
				                              .5f, 0.5f, -.5f,
				                              .5f, 0.5f, .5f,
				                              -.5f, 0.5f, .5f,
				                              -.5f, -0.5f, .5f,
				                              -.5f, 0.5f, -.5f,
				                              -.5f, -0.5f, -.5f};
		int[] indicesBox ={ 0, 1, 2, 0, 3, 1, 1 , 3, 4, 4 , 5 , 1,5,6,1,6,7,1,4,3,8,8,3,9};
		createBox(positionsBox, indicesBox);
		createMoebiusStrip();

		glEnable(GL_DEPTH_TEST);
	}

	/**
	 * @param deltaTime
	 *            The time in seconds between the last two frames
	 */
	boolean sence1 = true;
	boolean sence2 = false;
	boolean sence3 = false;

	public void update(float deltaTime) {
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			sence1 = true;
			sence2 = false;
			sence3 = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
			sence1 = false;
			sence2 = true;
			sence3 = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
			sence1 = false;
			sence2 = false;
			sence3 = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			Main.exit = true;

		if (Mouse.isButtonDown(0)) {
			float rotationScale = 0.01f;

			float deltaX = (float) Mouse.getDX();
			float deltaY = (float) Mouse.getDY();

			Mat4 rotationX = Mat4
					.rotation(Vec3.yAxis(), deltaX * rotationScale);
			Mat4 rotationY = Mat4.rotation(Vec3.xAxis(), -deltaY
					* rotationScale);

			modelMatrix = rotationY.mul(rotationX).mul(modelMatrix);
		}
	}

	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		shaderProgram.useProgram();
		shaderProgram.setUniform("uModel", modelMatrix);
		shaderProgram.setUniform("uProjection", projectionMatrix);

		glEnable(GL_CULL_FACE);

		if (sence1) {
			glCullFace(GL_FRONT);
			shaderProgram.setUniform("uColor", Color.green());
			triangleMesh.draw(GL_TRIANGLES);
			glCullFace(GL_BACK);
			shaderProgram.setUniform("uColor", Color.red());
			triangleMesh.draw(GL_TRIANGLES);
			shaderProgram.setUniform("uColor", Color.blue());
			glLineWidth(5);
			line.draw(GL_LINES);
		}
		if (sence2) {
			glCullFace(GL_FRONT);
			shaderProgram.setUniform("uColor", Color.green());
			box.draw(GL_TRIANGLES);
			glCullFace(GL_BACK);
			shaderProgram.setUniform("uColor", Color.red());
			box.draw(GL_TRIANGLES);
			shaderProgram.setUniform("uColor", Color.blue());
			glLineWidth(5);
			lineBox.draw(GL_LINES);
		}
		
		if (sence3){
			glCullFace(GL_FRONT);
			shaderProgram.setUniform("uColor", Color.green());
			mobius.draw(GL_TRIANGLES);
			glCullFace(GL_BACK);
			shaderProgram.setUniform("uColor", Color.red());
			mobius.draw(GL_TRIANGLES);
			shaderProgram.setUniform("uColor", Color.blue());
			glLineWidth(1);
			lineMobius.draw(GL_LINES);
			
		}

	}

	private void createTriangle(float[] positions, int[] indices) {

		int attributeLocation = 0; // has to match the location set in the
									// vertex shader
		int floatsPerPosition = 3; // x, y and z values per position

		triangleMesh = new Mesh(GL_STATIC_DRAW);
		triangleMesh.setAttribute(attributeLocation, positions,
				floatsPerPosition);
		triangleMesh.setIndices(indices);

		line = new Mesh(GL_STATIC_DRAW);
		line.setAttribute(attributeLocation, positions, floatsPerPosition);
		line.setIndices(createLineIndices(indices));

	}

	private int[] createLineIndices(int triangleIndices[]) {
		int[] result = new int[triangleIndices.length * 2];
		for (int i = 0; i < triangleIndices.length / 3; i++) {
			int in = i * 3;
			int out = i * 6;
			result[0 + out] = triangleIndices[in + 0];
			result[1 + out] = triangleIndices[in + 1];
			result[2 + out] = triangleIndices[in + 0];
			result[3 + out] = triangleIndices[in + 2];
			result[4 + out] = triangleIndices[in + 1];
			result[5 + out] = triangleIndices[in + 2];

		}
		return result;
	}

	private void createBox(float[] positions, int[] indices) {
		int attributeLocation = 0; // has to match the location set in the
		// vertex shader
		int floatsPerPosition = 3; // x, y and z values per position

		box = new Mesh(GL_STATIC_DRAW);
		box.setAttribute(attributeLocation, positions, floatsPerPosition);
		box.setIndices(indices);

		lineBox = new Mesh(GL_STATIC_DRAW);
		lineBox.setAttribute(attributeLocation, positions, floatsPerPosition);
		lineBox.setIndices(createLineIndices(indices));

	}
	
	private void createMoebiusStrip(){
	    int detail=102*10;
		float[] positions = new float[detail*6];
		for (int i=0; i<detail; i++){
			
			float u=(float) (i*12/(float)detail)*(float)(2*Math.PI);
			float v=-.5f;
			int out=i*6;
			positions[out]=(float) ((1+(v/2)*Math.cos(u/2))*Math.cos(u/2));
			positions[out+1]= (float) ((1+(v/2)*Math.cos(u/2))*Math.sin(u/2));
			positions[out+2]= (float) ((v/2)*Math.sin(u/2));		
		    v=.5f;
		    positions[out+3]=(float) ((1+(v/2)*Math.cos(u/2))*Math.cos(u/2));
			positions[out+4]= (float) ((1+(v/2)*Math.cos(u/2))*Math.sin(u/2));
			positions[out+5]= (float) ((v/2)*Math.sin(u/2));	
		}
			
			
	
	
int[] indices = new int [detail];
//{ 0,1,2,1,2,3,2,3,4,3,4,5};

for (int i=0; i< (indices.length+3)/6; i++){
	int out=i*6;
	indices[out]=out;
	indices[out+1]=out+1;
	indices[out+2]=out+2;
	indices[out+3]=out+1;
	indices[out+4]=out+3;
	indices[out+5]=out+2;
	//System.out.println(out+3);
	
	
}


		int attributeLocation = 0; // has to match the location set in the
		// vertex shader
		int floatsPerPosition = 3; // x, y and z values per position

		mobius = new Mesh(GL_STATIC_DRAW);
		mobius.setAttribute(attributeLocation, positions, floatsPerPosition);
		mobius.setIndices(indices);

		lineMobius = new Mesh(GL_STATIC_DRAW);
		lineMobius.setAttribute(attributeLocation, positions, floatsPerPosition);
		lineMobius.setIndices(createLineIndices(indices));
		
	}

}
